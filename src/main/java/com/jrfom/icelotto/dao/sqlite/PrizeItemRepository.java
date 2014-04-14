package com.jrfom.icelotto.dao.sqlite;

import java.util.HashMap;
import java.util.List;

import com.jrfom.icelotto.dao.PrizeItemDao;
import com.jrfom.icelotto.exception.PrizeItemNotFoundException;
import com.jrfom.icelotto.model.GameItem;
import com.jrfom.icelotto.model.PrizeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PrizeItemRepository implements PrizeItemDao {
  private static final Logger log = LoggerFactory.getLogger(PrizeItemRepository.class);

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private GameItemRepository gameItemRepository;

  private final String baseQuery = "select a.id, a.count, " +
    "b.id as 'game_item.id', b.description as 'game_item.description', " +
    "b.image_url as 'game_item.image_url', b.min_level as 'game_item.min_level', " +
    "b.name as 'game_item.name', b.rarity as 'game_item.rarity' " +
    "from prize_items a " +
    "join game_items b " +
    "on b.id = a.game_item ";

  @Override
  public PrizeItem create(final PrizeItem prizeItem) {
    log.debug("Creating new prize item record for: `{}`", prizeItem.toString());
    SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate);
    insert.usingGeneratedKeyColumns("id")
      .withTableName("prize_items")
      .usingColumns(new String[] {
        "count",
        "game_item"
      });

    Number key = insert.executeAndReturnKey(new HashMap<String, Object>(){{
      put("count", prizeItem.getCount());
      put("game_item", prizeItem.getGameItem().getId());
    }});

    return this.findById(key.longValue());
  }

  @Override
  public void delete(Long prizeItemId) throws PrizeItemNotFoundException {
    log.debug("Deleting prize item record for item: `{}`", prizeItemId);
    PrizeItem prizeItem = this.findById(prizeItemId);
    if (prizeItem == null) {
      throw new PrizeItemNotFoundException();
    }

    this.jdbcTemplate.update("delete from prize_items where id = ?", prizeItemId);
  }

  @Override
  public List<PrizeItem> findAll() {
    log.debug("Finding all prize items");
    return this.jdbcTemplate.query(
      this.baseQuery,
      new BeanPropertyRowMapper<PrizeItem>(PrizeItem.class)
    );
  }

  @Override
  public PrizeItem findById(Long id) {
    log.debug("Finding prize item with id: `{}`", id);
    return this.jdbcTemplate.queryForObject(
       this.baseQuery + " where a.id = ?",
      new BeanPropertyRowMapper<PrizeItem>(PrizeItem.class),
      id
    );
  }

  @Override
  public PrizeItem save(PrizeItem prizeItem) {
    log.debug("Updating prize item record identified by: `{}`", prizeItem.getId());

    GameItem gameItem = this.gameItemRepository.save(prizeItem.getGameItem());
    this.jdbcTemplate.update(
      "update prize_items set count = ?, game_item = ?",
      prizeItem.getCount(),
      gameItem.getId()
    );

    return this.findById(prizeItem.getId());
  }
}