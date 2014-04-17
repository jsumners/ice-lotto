package com.jrfom.icelotto.dao.sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.jrfom.icelotto.dao.CharacterDao;
import com.jrfom.icelotto.exception.CharacterNotFoundException;
import com.jrfom.icelotto.model.Character;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CharacterRepository implements CharacterDao {
  private static final Logger log = LoggerFactory.getLogger(CharacterRepository.class);

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  @Transactional("jdbcTransactionManager")
  public Character create(final Character character) {
    log.debug("Creating new character: `{}`", character.getName());
    SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate);
    insert.usingGeneratedKeyColumns("id")
      .withTableName("characters")
      .usingColumns("name");

    Number key = insert.executeAndReturnKey(new HashMap<String, Object>(){{
      put("name", character.getName());
    }});

    return this.findById(key.longValue());
  }

  @Override
  @Transactional("jdbcTransactionManager")
  public void delete(Long characterId) throws CharacterNotFoundException {
    Character character = this.findById(characterId);
    if (character == null) {
      throw new CharacterNotFoundException();
    }

    this.jdbcTemplate.update("delete from user_characters where character_id = ?", characterId);
    this.jdbcTemplate.update("delete from characters where id = ?", characterId);
  }

  @Override
  public List<Character> findAll() {
    log.debug("Finding all characters");
    return this.jdbcTemplate.query(
      "select * from characters order by id",
      new CharacterRowMapper()
    );
  }

  @Override
  public List<Character> findAllForUser(Long userId) {
    log.debug("Finding all characters for user with id: `{}`", userId);
    return this.jdbcTemplate.query(
      "select * " +
        "from characters a " +
        "join user_characters b " +
        "on b.character_id = a.id " +
        "and b.user_id = ?",
      new CharacterRowMapper(),
      userId
    );
  }

  @Override
  public Character findById(Long id) {
    log.debug("Finding character with id: `{}`", id);
    return this.jdbcTemplate.queryForObject(
      "select * from characters where id = ?",
      new CharacterRowMapper(),
      id
    );
  }

  @Override
  @Transactional("jdbcTransactionManager")
  public Character save(Character character) {
    return null;
  }

  private class CharacterRowMapper implements RowMapper<Character> {
    @Override
    public Character mapRow(ResultSet rs, int rowNum) throws SQLException {
      Character character = new Character();
      character.setId(rs.getLong("id"));
      character.setName(rs.getString("name"));

      return character;
    }
  }
}