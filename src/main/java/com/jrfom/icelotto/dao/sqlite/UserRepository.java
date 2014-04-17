package com.jrfom.icelotto.dao.sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.jrfom.icelotto.dao.UserDao;
import com.jrfom.icelotto.exception.UserNotFoundException;
import com.jrfom.icelotto.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserRepository implements UserDao {
  private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  @Transactional("jdbcTransactionManager")
  public User create(final User user) {
    log.debug("Creating new user with username: `{}`", user.getGw2DisplayName());
    SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate);
    insert.usingGeneratedKeyColumns("id")
      .withTableName("users")
      .usingColumns(new String[]{
        "claim_key",
        "claimed",
        "datetime_format",
        "display_name",
        "email",
        "enabled",
        "gw2display_name",
        "password",
        "time_zone"
      });

    Number key = insert.executeAndReturnKey(new HashMap<String, Object>(){{
      put("claim_key", user.getClaimKey());
      put("claimed", user.isClaimed());
      put("datetime_format", user.getDatetimeFormat());
      put("display_name", user.getDisplayName());
      put("email", user.getEmail());
      put("enabled", user.isEnabled());
      put("gw2display_name", user.getGw2DisplayName());
      put("password", user.getPassword());
      put("time_zone", user.getTimeZone());
    }});

    return this.findById(key.longValue());
  }

  @Override
  @Transactional("jdbcTransactionManager")
  public void delete(Long userId) throws UserNotFoundException {
    // TODO: make sure we can delete a user without affecting any audit data
    log.debug("Deleting user with id: `{}`", userId);
    User user = this.findById(userId);
    if (user == null) {
      throw new UserNotFoundException();
    }

    // TODO: do this with the other repositories' methods
    this.jdbcTemplate.update("delete from user_roles where user_id = ?", userId);
    this.jdbcTemplate.update(
      "delete from characters where id in (select character_id from user_characters where user_id = ?)",
      userId
    );
    this.jdbcTemplate.update(
      "delete from user_characters where user_id = ?",
      userId
    );
    this.jdbcTemplate.update("delete from users where id = ?", userId);
  }

  @Override
  public List<User> findAll() {
    log.debug("Finding all users");
    return this.jdbcTemplate.queryForList(
      "select * from users",
      User.class
    );
  }

  @Override
  public List<User> findAllLike(final String term) {
    log.debug("Finding all users like term: `{}`", term);
    final NamedParameterJdbcTemplate template =
      new NamedParameterJdbcTemplate(this.jdbcTemplate);
    return template.query(
      "select a.* " +
        "from users a " +
        "join user_characters b " +
        "on b.user_id = a.id " +
        "where lower(a.gw2display_name) like lower(:term) " +
        "or lower(a.display_name) like lower(:term) " +
        "or lower(a.email) like lower(:term) " +
        "or (" +
        "select count(z.name) from characters z " +
        "where lower(z.name) like lower(:term) " +
        "and z.id = b.character_id" +
        ") > 0 " +
        "group by a.id",
      new HashMap<String, Object>(){{
        put("term", term);
      }},
      new UserRowMapper()
    );
  }

  @Override
  public List<User> findAllOrderByGw2DisplayName() {
    log.debug("Finding all users and ordering by gw2 display name");
    return this.jdbcTemplate.query(
      "select * from users order by gw2display_name asc",
      new UserRowMapper()
    );
  }

  @Override
  public User findByGw2DisplayName(String gw2DisplayName) throws UserNotFoundException {
    log.debug("Finding user with gw2 display name: `{}`", gw2DisplayName);
    User user = this.jdbcTemplate.queryForObject(
      "select * from users where lower(gw2display_name) = lower(?)",
      new UserRowMapper(),
      gw2DisplayName
    );

    if (user == null) {
      throw new UserNotFoundException();
    }

    return user;
  }

  @Override
  public User findByGw2DisplayNameAndClaimKey(String gw2DisplayName, String claimKey) throws UserNotFoundException {
    log.debug("Finding user by gw2 display name and claim key: [`{}`, `{}`]", gw2DisplayName, claimKey);
    User user = this.jdbcTemplate.queryForObject(
      "select * from users a " +
      "where lower(a.gw2displayName) = lower(?) " +
      "and a.claimKey = ? " +
      "and a.claimed = 0",
      new UserRowMapper(),
      gw2DisplayName,
      claimKey
    );

    if (user == null) {
      throw new UserNotFoundException();
    }

    return user;
  }

  @Override
  public User findById(Long id) {
    log.debug("Finding user with id: `{}`", id);
    return this.jdbcTemplate.queryForObject(
      "select * from users where id = ?",
      new UserRowMapper(),
      id
    );
  }

  @Override
  @Transactional("jdbcTransactionManager")
  public User save(User user) {
    // TODO: handle saving characters and roles at the same time
    log.debug("Saving user with id: `{}`", user.getId());
    this.jdbcTemplate.update(
      "update users " +
        "set claim_key = ?, " +
        "claimed = ?, " +
        "datetime_format = ?, " +
        "display_name = ?, " +
        "email = ?, " +
        "enabled = ?, " +
        "gw2display_name = ?, " +
        "password = ?, " +
        "time_zone = ? " +
        "where id = ?",
      user.getClaimKey(),
      user.isClaimed(),
      user.getDatetimeFormat(),
      user.getDisplayName(),
      user.getEmail(),
      user.isEnabled(),
      user.getGw2DisplayName(),
      user.getPassword(),
      user.getTimeZone(),
      user.getId()
    );

    return this.findById(user.getId());
  }

  private class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
      User user = new User();
      user.setId(rs.getLong("id"));
      user.setGw2DisplayName(rs.getString("gw2display_name"));
      user.setDisplayName(rs.getString("display_name"));
      user.setEmail(rs.getString("email"));
      user.setPassword(rs.getString("password"));
      user.setTimeZone(rs.getString("time_zone"));
      user.setDatetimeFormat(rs.getString("datetime_format"));
      user.setClaimKey(rs.getString("claim_key"));
      user.setClaimed(rs.getInt("claimed") > 0);
      user.setEnabled(rs.getInt("enabled") > 0);

      return user;
    }
  }
}