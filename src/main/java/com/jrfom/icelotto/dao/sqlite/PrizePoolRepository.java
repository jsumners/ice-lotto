package com.jrfom.icelotto.dao.sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.jrfom.icelotto.dao.MoneyDrawResultDao;
import com.jrfom.icelotto.dao.PrizePoolDao;
import com.jrfom.icelotto.dao.PrizeTierDao;
import com.jrfom.icelotto.exception.PrizePoolNotFoundException;
import com.jrfom.icelotto.model.PrizePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PrizePoolRepository implements PrizePoolDao {
  private static final Logger log = LoggerFactory.getLogger(PrizePoolRepository.class);

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private MoneyDrawResultDao moneyDrawResultDao;

  @Autowired
  private PrizeTierDao prizeTierDao;

  @Override
  @Transactional("jdbcTransactionManager")
  public PrizePool create(final PrizePool prizePool) {
    log.debug("Creating new prize pool record: `{}`", prizePool.toString());
    SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate);
    insert.usingGeneratedKeyColumns("id")
      .withTableName("prize_pools")
      .usingColumns(new String[] {
        "tier1",
        "tier2",
        "tier3",
        "tier4",
        "tier5",
        "tier6",
        "tier7",
        "tier8",
        "tier9",
        "tier10",
        "drawn",
        "draw_result"
      });

    Number key = insert.executeAndReturnKey(new HashMap<String, Object>(){{
      put("tier1", (prizePool.getTier1() == null) ? null : prizePool.getTier1().getId());
      put("tier2", (prizePool.getTier2() == null) ? null : prizePool.getTier2().getId());
      put("tier3", (prizePool.getTier3() == null) ? null : prizePool.getTier3().getId());
      put("tier4", (prizePool.getTier4() == null) ? null : prizePool.getTier4().getId());
      put("tier5", (prizePool.getTier5() == null) ? null : prizePool.getTier5().getId());
      put("tier6", (prizePool.getTier6() == null) ? null : prizePool.getTier6().getId());
      put("tier7", (prizePool.getTier7() == null) ? null : prizePool.getTier7().getId());
      put("tier8", (prizePool.getTier8() == null) ? null : prizePool.getTier8().getId());
      put("tier9", (prizePool.getTier9() == null) ? null : prizePool.getTier9().getId());
      put("tier10", (prizePool.getTier10() == null) ? null : prizePool.getTier10().getId());
      put("drawn", prizePool.isDrawn());
      put("draw_result", (prizePool.getMoneyDrawResult() == null) ? null : prizePool.getMoneyDrawResult().getId());
    }});

    return this.findById(key.longValue());
  }

  @Override
  @Transactional("jdbcTransactionManager")
  public void delete(Long prizePoolId) throws PrizePoolNotFoundException {
    log.debug("Deleting prize pool with id: `{}`", prizePoolId);
    PrizePool prizePool = this.findById(prizePoolId);
    if (prizePool == null) {
      throw new PrizePoolNotFoundException();
    }

    if (prizePool.getMoneyDrawResult() != null) {
      try {
        this.moneyDrawResultDao.delete(prizePool.getMoneyDrawResult().getId());
      } catch (Exception e) {
        log.error("Could not delete associated money draw result: {}", e.getMessage());
        log.debug(e.toString());
        return;
      }
    }
    this.jdbcTemplate.update("delete from prize_pools where id = ?", prizePoolId);
  }

  @Override
  public List<PrizePool> findAll() {
    log.debug("Finding all prize pools");
    return this.jdbcTemplate.query(
      "select * from prize_pools order by id asc",
      new PoolRowMapper()
    );
  }

  @Override
  public PrizePool findById(Long id) {
    log.debug("Finding prize pool with id: `{}`", id);
    return this.jdbcTemplate.queryForObject(
      "select * from prize_pools where id = ?",
      new PoolRowMapper(),
      id
    );
  }

  @Override
  @Transactional("jdbcTransactionManager")
  public PrizePool save(PrizePool prizePool) {
    log.debug("Updating prize pool record identified by: `{}`", prizePool.getId());
    final Long id = prizePool.getId();

    this.jdbcTemplate.update(
      "update prize_pools set " +
        "tier1 = ?, tier2 = ?, tier3 = ?, tier4 = ?, tier5 = ?, " +
        "tier6 = ?, tier7 = ?, tier8 = ?, tier9 = ?, tier10 = ?, " +
        "drawn = ?, draw_result = ? where id = ?",
      (prizePool.getTier1() == null) ? null : prizePool.getTier1().getId(),
      (prizePool.getTier2() == null) ? null : prizePool.getTier2().getId(),
      (prizePool.getTier3() == null) ? null : prizePool.getTier3().getId(),
      (prizePool.getTier4() == null) ? null : prizePool.getTier4().getId(),
      (prizePool.getTier5() == null) ? null : prizePool.getTier5().getId(),
      (prizePool.getTier6() == null) ? null : prizePool.getTier6().getId(),
      (prizePool.getTier7() == null) ? null : prizePool.getTier7().getId(),
      (prizePool.getTier8() == null) ? null : prizePool.getTier8().getId(),
      (prizePool.getTier9() == null) ? null : prizePool.getTier9().getId(),
      (prizePool.getTier10() == null) ? null : prizePool.getTier10().getId(),
      (prizePool.isDrawn()) ? 1 : 0,
      (prizePool.getMoneyDrawResult() == null) ? null : prizePool.getMoneyDrawResult().getId(),
      id
    );

    return this.findById(id);
  }

  private class PoolRowMapper implements RowMapper<PrizePool> {
    @Override
    public PrizePool mapRow(ResultSet rs, int rowNum) throws SQLException {
      PrizePool result = new PrizePool();
      result.setTier1(prizeTierDao.findById(rs.getLong("tier1")));
      result.setTier2(prizeTierDao.findById(rs.getLong("tier2")));
      result.setTier3(prizeTierDao.findById(rs.getLong("tier3")));
      result.setTier4(prizeTierDao.findById(rs.getLong("tier4")));
      result.setTier5(prizeTierDao.findById(rs.getLong("tier5")));
      result.setTier6(prizeTierDao.findById(rs.getLong("tier6")));
      result.setTier7(prizeTierDao.findById(rs.getLong("tier7")));
      result.setTier8(prizeTierDao.findById(rs.getLong("tier8")));
      result.setTier9(prizeTierDao.findById(rs.getLong("tier9")));
      result.setTier10(prizeTierDao.findById(rs.getLong("tier10")));
      result.setMoneyDrawResult(moneyDrawResultDao.findById(rs.getLong("draw_result")));

      Boolean drawn = rs.getInt("drawn") > 0;
      result.setDrawn(drawn);

      return result;
    }
  }
}