package com.jrfom.icelotto.dao.sqlite;

import java.util.HashMap;
import java.util.List;

import com.jrfom.icelotto.dao.PrizeTierDao;
import com.jrfom.icelotto.exception.PrizeTierNotFoundException;
import com.jrfom.icelotto.model.PrizeTier;
import com.jrfom.icelotto.util.Stringer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PrizeTierRepository implements PrizeTierDao {
  private static final Logger log = LoggerFactory.getLogger(PrizeTierRepository.class);

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final String baseSql;

  public PrizeTierRepository() {
    this.baseSql = Stringer.readClasspathFileToString("scripts/sqlite/prize_tiers_base.sql");
  }

  @Override
  public PrizeTier create(final PrizeTier prizeTier) {
    log.debug("Creating new prize tier row: `{}`", prizeTier.toString());
    SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate);
    insert.usingGeneratedKeyColumns("id")
      .withTableName("prize_tiers")
      .usingColumns(new String[] {
        "drawn",
        "draw_result",
        "item1",
        "item2",
        "item3",
        "item4",
        "item5",
        "item6",
        "item7",
        "item8",
        "item9",
        "item10"
      });

    Number key = insert.executeAndReturnKey(new HashMap<String, Object>(){{
      put("drawn", prizeTier.isDrawn());
      put("draw_result", prizeTier.getPrizeDrawResult());
      put("item1", (prizeTier.getItem1() == null) ? null : prizeTier.getItem1().getId());
      put("item2", (prizeTier.getItem2() == null) ? null : prizeTier.getItem2().getId());
      put("item3", (prizeTier.getItem3() == null) ? null : prizeTier.getItem3().getId());
      put("item4", (prizeTier.getItem4() == null) ? null : prizeTier.getItem4().getId());
      put("item5", (prizeTier.getItem5() == null) ? null : prizeTier.getItem5().getId());
      put("item6", (prizeTier.getItem6() == null) ? null : prizeTier.getItem6().getId());
      put("item7", (prizeTier.getItem7() == null) ? null : prizeTier.getItem7().getId());
      put("item8", (prizeTier.getItem8() == null) ? null : prizeTier.getItem8().getId());
      put("item9", (prizeTier.getItem9() == null) ? null : prizeTier.getItem9().getId());
      put("item10", (prizeTier.getItem10() == null) ? null : prizeTier.getItem10().getId());
    }});

    return this.findById(key.longValue());
  }

  @Override
  public void delete(Long prizeTierId) throws PrizeTierNotFoundException {
    log.debug("Deleting prize tier with id: `{}`", prizeTierId);
    PrizeTier prizeTier = this.findById(prizeTierId);
    if (prizeTier == null) {
      throw new PrizeTierNotFoundException();
    }

    // TODO: verify that the tier is not needed for auditing
    this.jdbcTemplate.update("delete from prize_tiers where id = ?", prizeTierId);
  }

  @Override
  public List<PrizeTier> findAll() {
    log.debug("Finding all prize tiers");
    return this.jdbcTemplate.query(
      this.baseSql,
      new BeanPropertyRowMapper<PrizeTier>(PrizeTier.class)
    );
  }

  @Override
  public PrizeTier findById(Long id) {
    log.debug("Finding prize tier with id: `{}`", id);
    return this.jdbcTemplate.queryForObject(
      this.baseSql + " where id = ?",
      new BeanPropertyRowMapper<PrizeTier>(PrizeTier.class),
      id
    );
  }

  @Override
  public PrizeTier save(PrizeTier prizeTier) {
    log.debug("Updating prize tier recorder identified by: `{}`", prizeTier.getId());

    this.jdbcTemplate.update(
      "update prize_tiers set " +
        "drawn = ?, " +
        "draw_result = ?, " +
        "item1 = ?, " +
        "item2 = ?, " +
        "item3 = ?, " +
        "item4 = ?, " +
        "item5 = ?, " +
        "item6 = ?, " +
        "item7 = ?, " +
        "item8 = ?, " +
        "item9 = ?, " +
        "item10 = ?",
      prizeTier.isDrawn(),
      (prizeTier.getPrizeDrawResult() == null) ? null : prizeTier.getPrizeDrawResult().getId(),
      (prizeTier.getItem1() == null) ? null : prizeTier.getItem1().getId(),
      (prizeTier.getItem2() == null) ? null : prizeTier.getItem2().getId(),
      (prizeTier.getItem3() == null) ? null : prizeTier.getItem3().getId(),
      (prizeTier.getItem4() == null) ? null : prizeTier.getItem4().getId(),
      (prizeTier.getItem5() == null) ? null : prizeTier.getItem5().getId(),
      (prizeTier.getItem6() == null) ? null : prizeTier.getItem6().getId(),
      (prizeTier.getItem7() == null) ? null : prizeTier.getItem7().getId(),
      (prizeTier.getItem8() == null) ? null : prizeTier.getItem8().getId(),
      (prizeTier.getItem9() == null) ? null : prizeTier.getItem9().getId(),
      (prizeTier.getItem10() == null) ? null : prizeTier.getItem10().getId()
    );

    return this.findById(prizeTier.getId());
  }
}