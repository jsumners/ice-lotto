package com.jrfom.icelotto.dao.sqlite;

import java.util.HashMap;
import java.util.List;

import com.jrfom.icelotto.dao.RoleDao;
import com.jrfom.icelotto.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RoleRepository implements RoleDao {
  private static final Logger log = LoggerFactory.getLogger(RoleRepository.class);

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  @Transactional("jdbcTransactionManager")
  public Role create(final Role role) {
    log.debug("Creating new role: [`{}`, `{}`]", role.getName(), role.getDescription());
    SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate);
    insert.usingGeneratedKeyColumns("id")
      .withTableName("roles")
      .usingColumns(new String[]{
        "description",
        "name"
      });

    Number key = insert.executeAndReturnKey(new HashMap<String, Object>(){{
      put("description", role.getDescription());
      put("name", role.getName());
    }});

    return this.findById(key.longValue());
  }

  @Override
  @Transactional("jdbcTransactionManager")
  public void delete(Long roleId) {
    // TODO: make sure we can delete the role without affecting users
    log.debug("Deleting role with id: `{}`", roleId);
    this.jdbcTemplate.update("delete from roles where id = ?", roleId);
  }

  @Override
  public List<Role> findAll() {
    log.debug("Finding all roles");
    return this.jdbcTemplate.queryForList(
      "select * from roles order by id",
      Role.class
    );
  }

  @Override
  public Role findById(Long id) {
    log.debug("Finding role with id: `{}`", id);
    return this.jdbcTemplate.queryForObject(
      "select * from roles a where a.id = ?",
      Role.class,
      new Object[]{id}
    );
  }

  @Override
  @Transactional("jdbcTransactionManager")
  public Role save(Role role) {
    log.debug("Saving instance of role with id: `{}`", role.getId());
    this.jdbcTemplate.update(
      "update roles set " +
      "description = ?," +
      "name = ? " +
      "where id = ?",
      role.getDescription(),
      role.getName(),
      role.getId()
    );

    return this.findById(role.getId());
  }
}