package com.jrfom.icelotto.dao.sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.jrfom.icelotto.dao.MoneyDrawResultDao;
import com.jrfom.icelotto.model.MoneyDrawResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.threeten.bp.Instant;

@Repository
public class MoneyDrawResultRepository implements MoneyDrawResultDao {
  private static final Logger log = LoggerFactory.getLogger(MoneyDrawResultRepository.class);

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  @Transactional("jdbcTransactionManager")
  public MoneyDrawResult create(final MoneyDrawResult drawResult) {
    log.debug("Creating new money draw result record: `{}`", drawResult.toString());
    SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate);
    insert.usingGeneratedKeyColumns("id")
      .withTableName("money_draw_results")
      .usingColumns(new String[] {
        "awarded",
        "amount_won",
        "draw_number"
      });

    final Number key = insert.executeAndReturnKey(new HashMap<String, Object>(){{
      put("awarded", drawResult.getAwarded().getEpochSecond());
      put("amount_won", drawResult.getAmountWon());
      put("draw_number", drawResult.getDrawNumber());
    }});
    final Long id = key.longValue();

    insert = new SimpleJdbcInsert(this.jdbcTemplate);
    insert.withTableName("money_draw_drawings")
      .usingColumns(new String[] {
        "drawing_id",
        "money_draw_result_id"
      });
    insert.execute(new HashMap<String, Object>(){{
      put("drawing_id", drawResult.getDrawing().getId());
      put("money_draw_result_id", id);
    }});

    insert = new SimpleJdbcInsert(this.jdbcTemplate);
    insert.withTableName("money_draw_pools")
      .usingColumns(new String[] {
        "pool_id",
        "money_draw_result_id"
      });
    insert.execute(new HashMap<String, Object>(){{
      put("pool_id", drawResult.getPrizePool().getId());
      put("money_draw_result_id", id);
    }});

    insert = new SimpleJdbcInsert(this.jdbcTemplate);
    insert.withTableName("money_draw_winners")
      .usingColumns(new String[] {
        "winner_id",
        "money_draw_result_id"
      });
    insert.execute(new HashMap<String, Object>(){{
      put("winner_id", drawResult.getUser().getId());
      put("money_draw_result_id", id);
    }});

    return this.findById(id);
  }

  @Override
  @Transactional("jdbcTransactionManager")
  public void delete(Long drawResultId) throws Exception {
    log.debug("Deleting money draw result with id: `{}`", drawResultId);
    MoneyDrawResult drawResult = this.findById(drawResultId);
    if (drawResult == null) {
      throw new Exception("Money draw result with id `" + drawResultId + "` does not exist");
    }

    this.jdbcTemplate.update("delete from money_draw_drawings where money_draw_result_id = ?", drawResultId);
    this.jdbcTemplate.update("delete from money_draw_pools where money_draw_result_id = ?", drawResultId);
    this.jdbcTemplate.update("delete from money_daw_winners where money_draw_result_id = ?", drawResultId);
    this.jdbcTemplate.update("delete from money_draw_results where id = ?", drawResultId);
  }

  @Override
  public List<MoneyDrawResult> findAll() {
    log.debug("Finding all money draw results");
    return this.jdbcTemplate.query(
      "select * from money_draw_results order by id asc",
      new MoneyDrawResultRowMapper()
    );
  }

  @Override
  public List<MoneyDrawResult> findAllForDrawing(Long drawingId) {
    log.debug("Finding all money draw results for drawing with id: `{}`", drawingId);
    return this.jdbcTemplate.query(
      "select a.* " +
        "from money_draw_results a " +
        "join money_draw_drawings b " +
        "on b.money_draw_result_id = a.id " +
        "and b.drawing_id = ?",
      new MoneyDrawResultRowMapper(),
      drawingId
    );
  }

  @Override
  public List<MoneyDrawResult> findAllForUser(Long userid) {
    log.debug("Finding all money draw results for user with id: `{}`", userid);
    return this.jdbcTemplate.query(
      "select a.* " +
        "from money_draw_results a " +
        "join money_draw_winners b " +
        "on b.money_draw_result_id = a.id " +
        "and b.winner_id = ?",
      new MoneyDrawResultRowMapper(),
      userid
    );
  }

  @Override
  public MoneyDrawResult findById(Long id) {
    log.debug("Finding money draw result by id: `{}`", id);
    MoneyDrawResult result = null;

    try {
      result = this.jdbcTemplate.queryForObject(
        "select * from money_draw_results a where a.id = ?",
        new MoneyDrawResultRowMapper(),
        id
      );
    } catch (DataAccessException e) {
      log.error("Could not find a money draw result with id: `{}`", id);
      log.debug(e.toString());
    }

    return result;
  }

  @Override
  public MoneyDrawResult findByPoolId(Long id) {
    log.debug("Finding money draw result for pool with id: `{}`", id);
    return this.jdbcTemplate.queryForObject(
      "select a.* " +
        "from money_draw_results a " +
        "join money_draw_pools b " +
        "on b.money_draw_result_id = a.id " +
        "and b.pool_id = ?",
      new MoneyDrawResultRowMapper(),
      id
    );
  }

  @Override
  @Transactional("jdbcTransactionManager")
  public MoneyDrawResult save(MoneyDrawResult drawResult) {
    log.debug("Updating money draw result record identified by: `{}`", drawResult.getId());
    Long id = drawResult.getId();
    this.jdbcTemplate.update(
      "update money_draw_results set awarded = ?, amount_won = ?, draw_number = ? where id = ?",
      drawResult.getAwarded().getEpochSecond(),
      drawResult.getAmountWon(),
      drawResult.getDrawNumber(),
      id
    );

    this.jdbcTemplate.update(
      "update money_draw_drawings set drawing_id = ? where money_draw_result_id = ?",
      drawResult.getDrawing().getId(),
      id
    );
    this.jdbcTemplate.update(
      "update money_draw_pools set pool_id = ? where money_draw_result_id = ?",
      drawResult.getPrizePool().getId(),
      id
    );
    this.jdbcTemplate.update(
      "update money_draw_winners set winner_id = ? where money_draw_result_id = ?",
      drawResult.getUser().getId(),
      id
    );

    return this.findById(id);
  }

  private class MoneyDrawResultRowMapper implements RowMapper<MoneyDrawResult> {
    @Override
    public MoneyDrawResult mapRow(ResultSet rs, int rowNum) throws SQLException {
      MoneyDrawResult result = new MoneyDrawResult();
      result.setId(rs.getLong("id"));
      result.setAwarded(Instant.ofEpochSecond(rs.getLong("awarded")));
      result.setAmountWon(rs.getInt("amount_won"));
      result.setDrawNumber(rs.getInt("draw_number"));

      return result;
    }
  }
}