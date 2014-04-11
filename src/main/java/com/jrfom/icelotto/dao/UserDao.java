package com.jrfom.icelotto.dao;

import java.util.List;

import com.jrfom.icelotto.exception.UserNotFoundException;
import com.jrfom.icelotto.model.User;

public interface UserDao {
  User create(final User user);
  void delete(Long userId) throws UserNotFoundException;
  List<User> findAll();
  List<User> findAllLike(String term);
  List<User> findAllOrderByGw2DisplayName();
  User findByGw2DisplayName(String gw2DisplayName) throws UserNotFoundException;
  User findByGw2DisplayNameAndClaimKey(String gw2DisplayName, String claimKey) throws UserNotFoundException;
  User findById(Long id);
  User save(User user);
}
