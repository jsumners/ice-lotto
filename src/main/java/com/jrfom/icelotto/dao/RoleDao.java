package com.jrfom.icelotto.dao;

import java.util.List;

import com.jrfom.icelotto.model.Role;

public interface RoleDao {
  Role create(Role role);
  void delete(Long roleId);
  List<Role> findAll();
  List<Role> findAllForUser(Long userId);
  Role findById(Long id);
  Role save(Role role);
}