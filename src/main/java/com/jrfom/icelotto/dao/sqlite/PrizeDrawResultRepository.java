package com.jrfom.icelotto.dao.sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.jrfom.icelotto.dao.PrizeDrawResultDao;
import com.jrfom.icelotto.model.PrizeDrawResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.threeten.bp.Instant;

@Repository
public class PrizeDrawResultRepository implements PrizeDrawResultDao {
  private static final Logger log = LoggerFactory.getLogger(PrizeDrawResultRepository.class);

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public PrizeDrawResult create(final PrizeDrawResult drawResult) {
    log.debug("Creating new record for prize draw result: `{}`", drawResult.toString());
    SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate);
    insert.usingGeneratedKeyColumns("id")
      .withTableName("prize_draw_results")
      .usingColumns(new String[] {
        "awarded",
        "item_draw_number",
        "user_draw_number"
      });

    Number key = insert.executeAndReturnKey(new HashMap<String, Object>(){{
      put("awarded", drawResult.getAwarded().getEpochSecond());
      put("item_draw_number", drawResult.getItemDrawNumber());
      put("user_draw_number", drawResult.getUserDrawNumber());
    }});
    final Long id = key.longValue();

    insert = new SimpleJdbcInsert(this.jdbcTemplate);
    insert.withTableName("drawn_prize_items")
      .usingColumns(new String[] {
        "prize_item_id",
        "prize_draw_result_id"
      });
    insert.execute(new HashMap<String, Object>(){{
      put("prize_item_id", drawResult.getPrizeItem().getId());
      put("prize_draw_result_id", id);
    }});

    insert = new SimpleJdbcInsert(this.jdbcTemplate);
    insert.withTableName("drawn_prize_tiers")
      .usingColumns(new String[] {
        "prize_tier_id",
        "prize_draw_result_id"
      });
    insert.execute(new HashMap<String, Object>(){{
      put("prize_tier_id", drawResult.getPrizeTier().getId());
      put("prize_draw_result_id", id);
    }});

    insert = new SimpleJdbcInsert(this.jdbcTemplate);
    insert.withTableName("drawn_prize_winners")
      .usingColumns(new String[] {
        "winner_id",
        "prize_draw_result_id"
      });
    insert.execute(new HashMap<String, Object>(){{
      put("winner_id", drawResult.getUser().getId());
      put("prize_draw_result_id", id);
    }});

    return this.findById(id);
  }

  @Override
  public void delete(Long resultId) throws Exception {
    log.debug("Deleting prize draw result with id: `{}`", resultId);
    PrizeDrawResult drawResult = this.findById(resultId);
    if (drawResult == null) {
      throw new Exception("Could not find prize draw result with id: " + resultId);
    }

    this.jdbcTemplate.update(
      "delete from drawn_prize_winners where prize_draw_result_id = ?",
      resultId
    );

    this.jdbcTemplate.update(
      "delete from drawn_prize_tiers where prize_draw_result_id = ?",
      resultId
    );

    this.jdbcTemplate.update(
      "delete from drawn_prize_items where prize_draw_result_id = ?",
      resultId
    );

    this.jdbcTemplate.update(
      "delete from prize_draw_results where id = ?",
      resultId
    );
  }

  @Override
  public List<PrizeDrawResult> findAll() {
    log.debug("Finding all prize draw results");
    return this.jdbcTemplate.query(
      "select * from prize_draw_results order by id asc",
      new DrawResultRowMapper()
    );
  }

  @Override
  public List<PrizeDrawResult> findAllForUser(Long userId) {
    log.debug("Finding all prize draw results for user with id: `{}`", userId);
    return this.jdbcTemplate.query(
      "select a.* " +
        "from prize_draw_results a " +
        "join drawn_prize_winners b " +
        "on b.prize_draw_result_id = a.id " +
        "and b.winner_id = ?",
      new DrawResultRowMapper(),
      userId
    );
  }

  @Override
  public PrizeDrawResult findById(Long id) {
    log.debug("Finding prize draw result with id: `{}`", id);
    return this.jdbcTemplate.queryForObject(
      "select * from prize_draw_results where id = ?",
      new DrawResultRowMapper(),
      id
    );
  }

  @Override
  public PrizeDrawResult findByPrizeItemId(Long prizeItemId) {
    log.debug("Finding prize draw result for prize item with id: `{}`", prizeItemId);
    return this.jdbcTemplate.queryForObject(
      "select a.* " +
        "from prize_draw_results a " +
        "join drawn_prize_items b " +
        "on b.prize_draw_result_id = a.id " +
        "and b.prize_item_id = ?",
      new DrawResultRowMapper(),
      prizeItemId
    );
  }

  @Override
  public PrizeDrawResult findByPrizeTierId(Long tierId) {
    log.debug("Finding prize draw result for tier with id: `{}`", tierId);
    return this.jdbcTemplate.queryForObject(
      "select a.* " +
        "from prize_draw_results a " +
        "join drawn_prize_tiers b " +
        "on b.prize_draw_result_id = a.id " +
        "and b.prize_tier_id = ?",
      new DrawResultRowMapper(),
      tierId
    );
  }

  @Override
  public PrizeDrawResult save(PrizeDrawResult drawResult) {
    log.debug("Updating record for prize draw result identified by: `{}`", drawResult.getId());
    final Long id = drawResult.getId();

    this.jdbcTemplate.update(
      "update prize_draw_results " +
        "set awarded = ?, item_draw_number = ?, user_draw_number = ? " +
        "where id = ?",
      drawResult.getAwarded().getEpochSecond(),
      drawResult.getItemDrawNumber(),
      drawResult.getUserDrawNumber(),
      id
    );

    this.jdbcTemplate.update(
      "update drawn_prize_items set prize_item_id = ? where prize_draw_result_id = ?",
      drawResult.getPrizeItem().getId(),
      id
    );

    this.jdbcTemplate.update(
      "update drawn_prize_tiers set prize_tier_id = ? where prize_draw_result_id = ?",
      drawResult.getPrizeTier().getId(),
      id
    );

    this.jdbcTemplate.update(
      "update drawn_prize_winners set winner_id = ? where prize_draw_result_id = ?",
      drawResult.getUser().getId(),
      id
    );

    return this.findById(id);
  }

  private class DrawResultRowMapper implements RowMapper<PrizeDrawResult> {
    @Override
    public PrizeDrawResult mapRow(ResultSet rs, int rowNum) throws SQLException {
      PrizeDrawResult drawResult = new PrizeDrawResult();
      drawResult.setId(rs.getLong("id"));
      drawResult.setAwarded(Instant.ofEpochSecond(rs.getLong("awarded")));
      drawResult.setItemDrawNumber(rs.getInt("item_draw_number"));
      drawResult.setUserDrawNumber(rs.getInt("user_draw_number"));

      return drawResult;
    }
  }
}