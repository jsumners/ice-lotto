package com.jrfom.icelotto.dao.sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.jrfom.icelotto.dao.EntriesDao;
import com.jrfom.icelotto.model.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.threeten.bp.Instant;

@Repository
public class EntriesRepository implements EntriesDao {
  private static final Logger log = LoggerFactory.getLogger(EntriesRepository.class);

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public Entry create(final Entry entry) {
    log.debug("Creating new entry record: `{}`", entry.toString());
    SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate);
    insert.usingGeneratedKeyColumns("id")
      .withTableName("entries")
      .usingColumns(new String[] {
        "amount",
        "entered_date"
      });

    final Number key = insert.executeAndReturnKey(new HashMap<String, Object>(){{
      put("amount", entry.getAmount());
      put("entered_date", entry.getEnteredDate().getEpochSecond());
    }});

    insert = new SimpleJdbcInsert(this.jdbcTemplate);
    insert.withTableName("drawing_entries")
      .usingColumns(new String[] {
        "drawing_id",
        "entry_id"
      });
    insert.execute(new HashMap<String, Object>(){{
      put("drawing_id", entry.getDrawingId());
      put("entry_id", key.longValue());
    }});

    insert = new SimpleJdbcInsert(this.jdbcTemplate);
    insert.withTableName("tier_entries")
      .usingColumns(new String[] {
        "tier_id",
        "entry_id"
      });
    insert.execute(new HashMap<String, Object>(){{
      put("tier_id", entry.getPrizeTierId());
      put("entry_id", key.longValue());
    }});

    insert = new SimpleJdbcInsert(this.jdbcTemplate);
    insert.withTableName("user_entries")
      .usingColumns(new String[] {
        "user_id",
        "entry_id"
      });
    insert.execute(new HashMap<String, Object>(){{
      put("user_id", entry.getUser().getId());
      put("entry_id", key.longValue());
    }});

    return this.findById(key.longValue());
  }

  @Override
  public void delete(Long entryId) throws Exception {
    log.debug("Deleting entry with id: `{}`", entryId);
    Entry entry = this.findById(entryId);
    if (entry == null) {
      throw new Exception("Entry with id `" + entryId + "` does not exist");
    }

    // TODO: make sure this entry is not needed for auditing (in the application not here)
    this.jdbcTemplate.update("delete from entries where id = ?", entryId);
    this.jdbcTemplate.update("delete from drawing_entries where entry_id = ?", entryId);
    this.jdbcTemplate.update("delete from tier_entries where entry_id = ?", entryId);
    this.jdbcTemplate.update("delete from user_entries where entry_id = ?", entryId);
  }

  @Override
  public List<Entry> findAll() {
    log.debug("Finding all entries");
    return this.jdbcTemplate.query(
      "select a.*, b.drawing_id, c.tier_id " +
        "from entries a " +
        "join drawing_entries b " +
        "on b.entry_id = a.id " +
        "join tier_entries c " +
        "on c.entry_id = a.id " +
        "order by a.id asc",
      new EntryRowMapper()
    );
  }

  @Override
  public List<Entry> findAllForDrawing(Long drawingId) {
    log.debug("Finding all entries for drawing with id: `{}`", drawingId);
    return this.jdbcTemplate.query(
      "select a.*, b.drawing_id, c.tier_id " +
        "from entries a " +
        "join drawing_entries b " +
        "on b.entry_id = a.id " +
        "and b.drawing_id = ? " +
        "join tier_entries c " +
        "on c.entry_id = a.id",
      new EntryRowMapper(),
      drawingId
    );
  }

  @Override
  public List<Entry> findAllForDrawingAndUser(Long drawingId, Long userId) {
    log.debug("Finding all entries for [drawingId: `{}`, userId: `{}`]", drawingId, userId);
    return this.jdbcTemplate.query(
      "select a.*, b.drawing_id, c.tier_id " +
        "from entries a " +
        "join drawing_entries b " +
        "on b.entry_id = a.id " +
        "and b.drawing_id = ? " +
        "join tier_entries c " +
        "on c.entry_id = a.id " +
        "join user_entries d " +
        "on d.entry_id = a.id " +
        "and d.user_id = ?",
      new EntryRowMapper(),
      drawingId,
      userId
    );
  }

  @Override
  public List<Entry> findAllForPool(Long poolId) {
    log.debug("Finding all entries for pool with id: `{}`", poolId);
    return this.jdbcTemplate.query(
      "select a.*, b.darwing_id, d.tier_id " +
        "from entries a " +
        "join drawing_entries b " +
        "on b.entry_id = a.id " +
        "join drawings c " +
        "on c.id = b.drawing_id " +
        "and c.small_pool = ? " +
        "or c.large_pool = ? " +
        "join tier_entries d " +
        "on d.entry_id = a.id",
      new EntryRowMapper(),
      poolId, poolId
    );
  }

  @Override
  public List<Entry> findAllForTier(Long tierId) {
    log.debug("Finding all entries for tier with id: `{}`", tierId);
    return this.jdbcTemplate.query(
      "select a.*, b.tier_id, c.drawing_id " +
        "from entries a " +
        "join tier_entries b " +
        "on b.entry_id = a.id " +
        "and b.tier_id = ? " +
        "join drawing_entries c " +
        "on c.entry_id = a.id",
      new EntryRowMapper(),
      tierId
    );
  }

  @Override
  public Entry findById(Long id) {
    log.debug("Finding entry with id: `{}`", id);
    final Entry entry = this.jdbcTemplate.queryForObject(
      "select a.*, b.drawing_id, c.tier_id " +
        "from entries a " +
        "join drawing_entries b " +
        "on b.entry_id = a.id " +
        "join tier_entries c " +
        "on c.entry_id = a.id " +
        "where id = ?",
      new EntryRowMapper(),
      id
    );

    return entry;
  }

  @Override
  public Entry save(Entry entry) {
    log.debug("Updating entry record for entry with id: `{}`", entry.getId());
    Long entryId = entry.getId();

    this.jdbcTemplate.update(
      "update entries set amount = ?, entered_date = ? where id = ?",
      entry.getAmount(),
      entry.getEnteredDate().getEpochSecond(),
      entryId
    );

    this.jdbcTemplate.update(
      "update drawing_entries set drawing_id = ? where entry_id = ?",
      entry.getDrawingId(),
      entryId
    );

    this.jdbcTemplate.update(
      "update tier_entries set tier_id = ? where entry_id = ?",
      entry.getPrizeTierId(),
      entryId
    );

    this.jdbcTemplate.update(
      "update user_entries set user_id = ? where entry_id = ?",
      entry.getUser().getId(),
      entryId
    );

    return this.findById(entryId);
  }

  private class EntryRowMapper implements RowMapper<Entry> {
    @Override
    public Entry mapRow(ResultSet resultSet, int i) throws SQLException {
      Entry result = new Entry();
      result.setId(resultSet.getLong("id"));
      result.setAmount(resultSet.getInt("amount"));
      result.setDrawingId(resultSet.getLong("drawing_id"));
      result.setPrizeTierId(resultSet.getLong("tier_id"));
      result.setEnteredDate(Instant.ofEpochSecond(resultSet.getLong("entered_date")));

      return result;
    }
  }
}