package com.jrfom.icelotto.dao.sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.jrfom.icelotto.dao.GameItemDao;
import com.jrfom.icelotto.exception.GameItemNotFoundException;
import com.jrfom.icelotto.model.GameItem;
import com.jrfom.icelotto.model.ItemRarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
    insert.withTableName("game_items")
      .usingColumns(new String[]{
        "id",
        "description",
        "image_url",
        "min_level",
        "name",
        "rarity"
      });

    int rows = insert.execute(new HashMap<String, Object>() {{
      put("id", gameItem.getId());
      put("description", gameItem.getDescription());
      put("image_url", gameItem.getImageUrl());
      put("min_level", gameItem.getMinLevel());
      put("name", gameItem.getName());
      put("rarity", gameItem.getRarity());
    }});

    return this.findById(gameItem.getId());
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
    return this.jdbcTemplate.query(
      "select * from game_items order by id",
      new GameItemRowMapper()
    );
  }

  @Override
  public GameItem findById(Long id) {
    log.debug("Finding game item with id: `{}`", id);
    GameItem result = null;

    try {
      result = this.jdbcTemplate.queryForObject(
        "select * from game_items a where a.id = ?",
        new GameItemRowMapper(),
        id
      );
    } catch (DataAccessException e) {
      log.error("Could not find game item with id: `{}`", id);
      log.debug(e.toString());
    }

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

  private class GameItemRowMapper implements RowMapper<GameItem> {
    @Override
    public GameItem mapRow(ResultSet rs, int rowNum) throws SQLException {
      GameItem gameItem = null;

      Long id = rs.getLong("id");
      if (!id.equals(0)) {
        gameItem = new GameItem();

        gameItem.setId(id);
        gameItem.setDescription(rs.getString("description"));
        gameItem.setImageUrl(rs.getString("image_url"));
        gameItem.setMinLevel(rs.getInt("min_level"));
        gameItem.setName(rs.getString("name"));
        gameItem.setRarity(ItemRarity.values()[rs.getInt("rarity")]);
      }

      return gameItem;
    }
  }
}