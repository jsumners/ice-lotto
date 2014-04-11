package com.jrfom.icelotto.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.google.common.base.Optional;
import com.jrfom.icelotto.dao.sqlite.RoleRepository;
import com.jrfom.icelotto.exception.RoleNotFoundException;
import com.jrfom.icelotto.model.Role;
import com.jrfom.icelotto.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleRepositoryService implements RoleService {
  private static final Logger log = LoggerFactory.getLogger(RoleRepositoryService.class);

  @Resource
  private RoleRepository roleRepository;

  @Override
  @Transactional
  public Optional<Role> create(String name, String description) {
    log.debug("Creating new role: [name: `{}`, desc: `{}`]", name, description);
    Optional<Role> result = Optional.absent();
    Role record = new Role(name, description);

    try {
      record = this.roleRepository.save(record);
      result = Optional.of(record);
    } catch (DataAccessException e) {
      log.error("Could not create new role: `{}`", e.getMessage());
      log.debug(e.toString());
    }

    return result;
  }

  @Override
  @Transactional(rollbackFor = RoleNotFoundException.class)
  public void delete(Long roleId) throws RoleNotFoundException {
    log.debug("Deleting role with id: `{}`", roleId);
    Role deleted = this.roleRepository.findById(roleId);

    if (deleted == null) {
      log.debug("Could not find role with id: `{}`", roleId);
      throw new RoleNotFoundException();
    } else {
      this.roleRepository.delete(roleId);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<Role> findAll() {
    log.debug("Finding all roles");
    return this.roleRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Role> findById(Long id) {
    log.debug("Finding role with id: `{}`", id);
    Optional<Role> result = Optional.absent();
    Role role = this.roleRepository.findById(id);

    if (role != null) {
      result = Optional.of(role);
    }

    return result;
  }
}