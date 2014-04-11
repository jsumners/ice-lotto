package com.jrfom.icelotto.dao.sqlite;

import java.util.HashMap;
import java.util.List;

import com.jrfom.icelotto.dao.GameItemDao;
import com.jrfom.icelotto.exception.GameItemNotFoundException;
import com.jrfom.icelotto.model.GameItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class GameItemRepository implements GameItemDao {
  private static final Logger log = LoggerFactory.getLogger(GameItemRepository.class);

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public GameItem create(final GameItem gameItem) {
    log.debug("Creating new game item in sqlite database");
    SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate);
    insert.usingGeneratedKeyColumns("id")
      .withTableName("game_items")
      .usingColumns(new String[]{
        "description",
        "image_url",
        "min_level",
        "name",
        "rarity"
      });

    Number key = insert.executeAndReturnKey(new HashMap<String, Object>() {{
      put("description", gameItem.getDescription());
      put("image_url", gameItem.getImageUrl());
      put("min_level", gameItem.getMinLevel());
      put("name", gameItem.getName());
      put("rarity", gameItem.getRarity());
    }});

    return this.findById(key.longValue());
  }

  @Override
  public void delete(Long gameItemId) throws GameItemNotFoundException {
    log.debug("Deleting game item with id: `{}`", gameItemId);
    this.jdbcTemplate.update(
      "delete from game_items where id = ?",
      gameItemId
    );
  }

  @Override
  public List<GameItem> findAll() {
    log.debug("Finding all game items");
    return this.jdbcTemplate.queryForList(
      "select * from game_items order by id",
      GameItem.class
    );
  }

  @Override
  public GameItem findById(Long id) {
    log.debug("Finding game item with id: `{}`", id);
    GameItem result = this.jdbcTemplate.queryForObject(
      "select * from game_items a where a.id = ?",
      GameItem.class,
      new Object[] {id}
    );

    return result;
  }

  @Override
  public GameItem save(GameItem gameItem) {
    log.debug("Saving game item with id: `{}`", gameItem.getId());
    this.jdbcTemplate.update(
      "update game_items set " +
      "description = ?, " +
      "image_url = ?, " +
      "min_level = ?, " +
      "name = ?, " +
      "rarity = ? " +
      "where id = ?",
      gameItem.getDescription(),
      gameItem.getImageUrl(),
      gameItem.getMinLevel(),
      gameItem.getName(),
      gameItem.getRarity(),
      gameItem.getId()
    );

    return this.findById(gameItem.getId());
  }
}