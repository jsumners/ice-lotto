package com.jrfom.icelotto.dao.sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.jrfom.icelotto.dao.PrizeDrawResultDao;
import com.jrfom.icelotto.dao.PrizeItemDao;
import com.jrfom.icelotto.dao.PrizeTierDao;
import com.jrfom.icelotto.exception.PrizeTierNotFoundException;
import com.jrfom.icelotto.model.GameItem;
import com.jrfom.icelotto.model.ItemRarity;
import com.jrfom.icelotto.model.PrizeItem;
import com.jrfom.icelotto.model.PrizeTier;
import com.jrfom.icelotto.util.Stringer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PrizeTierRepository implements PrizeTierDao {
  private static final Logger log = LoggerFactory.getLogger(PrizeTierRepository.class);

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private PrizeItemDao prizeItemDao;

  @Autowired
  private PrizeDrawResultDao prizeDrawResultDao;

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
      new TierRowMapper()
    );
  }

  @Override
  public PrizeTier findById(Long id) {
    log.debug("Finding prize tier with id: `{}`", id);
    return this.jdbcTemplate.queryForObject(
      this.baseSql + " where a.id = ?",
      new TierRowMapper(),
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

  private class TierRowMapper implements RowMapper<PrizeTier> {
    @Override
    public PrizeTier mapRow(ResultSet rs, int rowNum) throws SQLException {
      PrizeTier tier = new PrizeTier();
      tier.setId(rs.getLong("id"));
      tier.setDrawn(rs.getInt("drawn") > 0);

      Integer drawResultId = (Integer) rs.getObject("draw_result");
      tier.setPrizeDrawResult(
        (drawResultId == null) ? null : prizeDrawResultDao.findById(drawResultId.longValue())
      );

      // Yes, this sucks, but it's a helluva lot more peformant that the
      // bullshit that Hibernate does.
      Integer item1PrizeId = (Integer) rs.getObject("item1.prize_id");
      if (item1PrizeId != null) {
        PrizeItem prizeItem = new PrizeItem();
        GameItem gameItem = new GameItem();

        gameItem.setId(rs.getLong("item1.id"));
        gameItem.setDescription(rs.getString("item1.description"));
        gameItem.setImageUrl(rs.getString("item1.image_url"));
        gameItem.setMinLevel(rs.getInt("item1.min_level"));
        gameItem.setName(rs.getString("item1.name"));
        gameItem.setRarity(ItemRarity.values()[rs.getInt("item1.rarity")]);

        prizeItem.setId(item1PrizeId.longValue());
        prizeItem.setCount(rs.getInt("item1.count"));
        prizeItem.setGameItem(gameItem);

        tier.setItem1(prizeItem);
      } else {
        tier.setItem1(null);
      }
      
      Integer item2PrizeId = (Integer) rs.getObject("item2.prize_id");
      if (item1PrizeId != null) {
        PrizeItem prizeItem = new PrizeItem();
        GameItem gameItem = new GameItem();

        gameItem.setId(rs.getLong("item2.id"));
        gameItem.setDescription(rs.getString("item2.description"));
        gameItem.setImageUrl(rs.getString("item2.image_url"));
        gameItem.setMinLevel(rs.getInt("item2.min_level"));
        gameItem.setName(rs.getString("item2.name"));
        gameItem.setRarity(ItemRarity.values()[rs.getInt("item2.rarity")]);

        prizeItem.setId(item1PrizeId.longValue());
        prizeItem.setCount(rs.getInt("item2.count"));
        prizeItem.setGameItem(gameItem);

        tier.setItem2(prizeItem);
      } else {
        tier.setItem2(null);
      }
      
      Integer item3PrizeId = (Integer) rs.getObject("item3.prize_id");
      if (item3PrizeId != null) {
        PrizeItem prizeItem = new PrizeItem();
        GameItem gameItem = new GameItem();

        gameItem.setId(rs.getLong("item3.id"));
        gameItem.setDescription(rs.getString("item3.description"));
        gameItem.setImageUrl(rs.getString("item3.image_url"));
        gameItem.setMinLevel(rs.getInt("item3.min_level"));
        gameItem.setName(rs.getString("item3.name"));
        gameItem.setRarity(ItemRarity.values()[rs.getInt("item3.rarity")]);

        prizeItem.setId(item3PrizeId.longValue());
        prizeItem.setCount(rs.getInt("item3.count"));
        prizeItem.setGameItem(gameItem);

        tier.setItem3(prizeItem);
      } else {
        tier.setItem3(null);
      }
      
      Integer item4PrizeId = (Integer) rs.getObject("item4.prize_id");
      if (item4PrizeId != null) {
        PrizeItem prizeItem = new PrizeItem();
        GameItem gameItem = new GameItem();

        gameItem.setId(rs.getLong("item4.id"));
        gameItem.setDescription(rs.getString("item4.description"));
        gameItem.setImageUrl(rs.getString("item4.image_url"));
        gameItem.setMinLevel(rs.getInt("item4.min_level"));
        gameItem.setName(rs.getString("item4.name"));
        gameItem.setRarity(ItemRarity.values()[rs.getInt("item4.rarity")]);

        prizeItem.setId(item4PrizeId.longValue());
        prizeItem.setCount(rs.getInt("item4.count"));
        prizeItem.setGameItem(gameItem);

        tier.setItem4(prizeItem);
      } else {
        tier.setItem4(null);
      }
      
      Integer item5PrizeId = (Integer) rs.getObject("item5.prize_id");
      if (item5PrizeId != null) {
        PrizeItem prizeItem = new PrizeItem();
        GameItem gameItem = new GameItem();

        gameItem.setId(rs.getLong("item5.id"));
        gameItem.setDescription(rs.getString("item5.description"));
        gameItem.setImageUrl(rs.getString("item5.image_url"));
        gameItem.setMinLevel(rs.getInt("item5.min_level"));
        gameItem.setName(rs.getString("item5.name"));
        gameItem.setRarity(ItemRarity.values()[rs.getInt("item5.rarity")]);

        prizeItem.setId(item5PrizeId.longValue());
        prizeItem.setCount(rs.getInt("item5.count"));
        prizeItem.setGameItem(gameItem);

        tier.setItem5(prizeItem);
      } else {
        tier.setItem5(null);
      }
      
      Integer item6PrizeId = (Integer) rs.getObject("item6.prize_id");
      if (item6PrizeId != null) {
        PrizeItem prizeItem = new PrizeItem();
        GameItem gameItem = new GameItem();

        gameItem.setId(rs.getLong("item6.id"));
        gameItem.setDescription(rs.getString("item6.description"));
        gameItem.setImageUrl(rs.getString("item6.image_url"));
        gameItem.setMinLevel(rs.getInt("item6.min_level"));
        gameItem.setName(rs.getString("item6.name"));
        gameItem.setRarity(ItemRarity.values()[rs.getInt("item6.rarity")]);

        prizeItem.setId(item6PrizeId.longValue());
        prizeItem.setCount(rs.getInt("item6.count"));
        prizeItem.setGameItem(gameItem);

        tier.setItem6(prizeItem);
      } else {
        tier.setItem6(null);
      }
      
      Integer item7PrizeId = (Integer) rs.getObject("item7.prize_id");
      if (item7PrizeId != null) {
        PrizeItem prizeItem = new PrizeItem();
        GameItem gameItem = new GameItem();

        gameItem.setId(rs.getLong("item7.id"));
        gameItem.setDescription(rs.getString("item7.description"));
        gameItem.setImageUrl(rs.getString("item7.image_url"));
        gameItem.setMinLevel(rs.getInt("item7.min_level"));
        gameItem.setName(rs.getString("item7.name"));
        gameItem.setRarity(ItemRarity.values()[rs.getInt("item7.rarity")]);

        prizeItem.setId(item7PrizeId.longValue());
        prizeItem.setCount(rs.getInt("item7.count"));
        prizeItem.setGameItem(gameItem);

        tier.setItem7(prizeItem);
      } else {
        tier.setItem7(null);
      }
      
      Integer item8PrizeId = (Integer) rs.getObject("item8.prize_id");
      if (item8PrizeId != null) {
        PrizeItem prizeItem = new PrizeItem();
        GameItem gameItem = new GameItem();

        gameItem.setId(rs.getLong("item8.id"));
        gameItem.setDescription(rs.getString("item8.description"));
        gameItem.setImageUrl(rs.getString("item8.image_url"));
        gameItem.setMinLevel(rs.getInt("item8.min_level"));
        gameItem.setName(rs.getString("item8.name"));
        gameItem.setRarity(ItemRarity.values()[rs.getInt("item8.rarity")]);

        prizeItem.setId(item8PrizeId.longValue());
        prizeItem.setCount(rs.getInt("item8.count"));
        prizeItem.setGameItem(gameItem);

        tier.setItem8(prizeItem);
      } else {
        tier.setItem8(null);
      }
      
      Integer item9PrizeId = (Integer) rs.getObject("item9.prize_id");
      if (item9PrizeId != null) {
        PrizeItem prizeItem = new PrizeItem();
        GameItem gameItem = new GameItem();

        gameItem.setId(rs.getLong("item9.id"));
        gameItem.setDescription(rs.getString("item9.description"));
        gameItem.setImageUrl(rs.getString("item9.image_url"));
        gameItem.setMinLevel(rs.getInt("item9.min_level"));
        gameItem.setName(rs.getString("item9.name"));
        gameItem.setRarity(ItemRarity.values()[rs.getInt("item9.rarity")]);

        prizeItem.setId(item9PrizeId.longValue());
        prizeItem.setCount(rs.getInt("item9.count"));
        prizeItem.setGameItem(gameItem);

        tier.setItem9(prizeItem);
      } else {
        tier.setItem9(null);
      }
      
      Integer item10PrizeId = (Integer) rs.getObject("item10.prize_id");
      if (item10PrizeId != null) {
        PrizeItem prizeItem = new PrizeItem();
        GameItem gameItem = new GameItem();

        gameItem.setId(rs.getLong("item10.id"));
        gameItem.setDescription(rs.getString("item10.description"));
        gameItem.setImageUrl(rs.getString("item10.image_url"));
        gameItem.setMinLevel(rs.getInt("item10.min_level"));
        gameItem.setName(rs.getString("item10.name"));
        gameItem.setRarity(ItemRarity.values()[rs.getInt("item10.rarity")]);

        prizeItem.setId(item10PrizeId.longValue());
        prizeItem.setCount(rs.getInt("item10.count"));
        prizeItem.setGameItem(gameItem);

        tier.setItem10(prizeItem);
      } else {
        tier.setItem10(null);
      }

      return tier;
    }
  }
}