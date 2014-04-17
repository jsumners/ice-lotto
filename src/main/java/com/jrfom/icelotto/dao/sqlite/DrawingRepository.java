package com.jrfom.icelotto.dao.sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.jrfom.icelotto.dao.DrawingDao;
import com.jrfom.icelotto.dao.PrizePoolDao;
import com.jrfom.icelotto.exception.DrawingNotFoundException;
import com.jrfom.icelotto.model.Drawing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.threeten.bp.Instant;

@Repository
public class DrawingRepository implements DrawingDao {
  private static final Logger log = LoggerFactory.getLogger(DrawingRepository.class);

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private PrizePoolDao prizePoolDao;

  @Override
  public Drawing create(final Drawing drawing) {
    log.debug("Creating new drawing record: `{}`", drawing.toString());
    SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate);
    insert.usingGeneratedKeyColumns("id")
      .withTableName("drawings")
      .usingColumns(new String[] {
        "small_pool",
        "large_pool",
        "scheduled",
        "started",
        "ended",
        "in_progress",
        "duplicated"
      });

    Number key = insert.executeAndReturnKey(new HashMap<String, Object>(){{
      put("small_pool", (drawing.getSmallPool() == null) ? null : drawing.getSmallPool().getId());
      put("large_pool", (drawing.getLargePool() == null) ? null : drawing.getLargePool().getId());
      put("scheduled", (drawing.getScheduled() == null) ? null : drawing.getScheduled().getEpochSecond());
      put("started", (drawing.getStarted() == null) ? null : drawing.getStarted().getEpochSecond());
      put("ended", (drawing.getEnded() == null) ? null : drawing.getEnded().getEpochSecond());
      put("in_progress", (drawing.isInProgress()) ? 1 : 0);
      put("duplicated", (drawing.isDuplicated()) ? 1 : 0);
    }});

    return this.findById(key.longValue());
  }

  @Override
  public void delete(Long drawingId) throws DrawingNotFoundException {
    log.debug("Deleting drawing recorder with id: `{}`", drawingId);
    Drawing drawing = this.findById(drawingId);
    if (drawing == null) {
      throw new DrawingNotFoundException();
    }

    this.jdbcTemplate.update("delete from drawings where id = ?", drawingId);
  }

  @Override
  public List<Drawing> findAll() {
    log.debug("Finding all drawings");
    return this.jdbcTemplate.query(
      "select * from drawings order by id asc",
      new DrawingRowMapper()
    );
  }

  @Override
  public Drawing findById(Long id) {
    log.debug("Finding drawing identified by id: `{}`", id);
    return this.jdbcTemplate.queryForObject(
      "select * from drawings where id = ?",
      new DrawingRowMapper(),
      id
    );
  }

  @Override
  public Drawing nextDrawing() {
    log.debug("Finding next scheduled drawing");
    return this.jdbcTemplate.queryForObject(
      "select * " +
        "from drawings a " +
        "where a.ended is null " +
        "and a.scheduled = (select min(z.scheduled) from drawings z where z.ended is null) " +
        "order by a.scheduled asc",
      new DrawingRowMapper()
    );
  }

  @Override
  public Drawing previousDrawing() {
    log.debug("Finding last held drawing");
    return this.jdbcTemplate.queryForObject(
      "select * " +
        "from drawings a " +
        "where a.ended = (select max(z.ended) from drawings z)",
      new DrawingRowMapper()
    );
  }

  @Override
  public Drawing save(Drawing drawing) {
    log.debug("Updating drawing record identified by: `{}`", drawing.getId());

    this.jdbcTemplate.update(
      "update drawings set " +
        "small_pool = ?, large_pool = ?, scheduled = ?, started = ?, " +
        "ended = ?, in_progress = ?, duplicated = ? where id = ?",
      (drawing.getSmallPool() == null) ? null : drawing.getSmallPool().getId(),
      (drawing.getLargePool() == null) ? null : drawing.getLargePool().getId(),
      (drawing.getScheduled() == null) ? null : drawing.getScheduled().getEpochSecond(),
      (drawing.getStarted() == null) ? null : drawing.getStarted().getEpochSecond(),
      (drawing.getEnded() == null) ? null : drawing.getEnded().getEpochSecond(),
      (drawing.isInProgress()) ? 1 : 0,
      (drawing.isDuplicated()) ? 1 : 0,
      drawing.getId()
    );

    return this.findById(drawing.getId());
  }

  private class DrawingRowMapper implements RowMapper<Drawing> {
    @Override
    public Drawing mapRow(ResultSet rs, int rowNum) throws SQLException {
      Drawing result = new Drawing();
      result.setId(rs.getLong("id"));

      Long scheduled = rs.getLong("scheduled");
      Long started = rs.getLong("started");
      Long ended = rs.getLong("ended");
      result.setScheduled((scheduled == 0) ? null : Instant.ofEpochSecond(scheduled));
      result.setStarted((started == 0) ? null : Instant.ofEpochSecond(started));
      result.setEnded((ended == 0) ? null : Instant.ofEpochSecond(ended));

      result.setInProgress(rs.getInt("in_progress") > 0);
      result.setDuplicated(rs.getInt("duplicated") > 0);

      Object smallPoolId = rs.getObject("small_pool");
      Object largePoolId = rs.getObject("large_pool");

      result.setSmallPool(
        (smallPoolId == null) ? null : prizePoolDao.findById(((Integer) smallPoolId).longValue())
      );

      result.setLargePool(
        (largePoolId == null) ? null : prizePoolDao.findById(((Integer) largePoolId).longValue())
      );

      return result;
    }
  }
}