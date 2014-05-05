package com.jrfom.icelotto.service;

import java.util.List;

import com.google.common.base.Optional;
import com.jrfom.icelotto.exception.UserNotFoundException;
import com.jrfom.icelotto.model.Character;
import com.jrfom.icelotto.model.Drawing;
import com.jrfom.icelotto.model.Role;
import com.jrfom.icelotto.model.User;

public interface UserService {
  List<Character> charactersForUser(Long userId);
  Optional<User> create(String gw2DisplayName);
  void delete(Long userId) throws UserNotFoundException;
  List<User> findAll();
  Optional<User> findById(Long id);
  Optional<User> findByGw2DisplayName(String gw2DisplayName);
  Optional<User> findByGw2DisplayNameAndClaimKey(String gw2DisplayName, String claimKey);
  List<User> findAllLike(String term);
  List<User> findAllOrderByGw2DisplayName();
  Boolean hasEntriesInDrawing(Long userId, Drawing drawing);
  Boolean isInSmallMoneyPoolForDrawing(Long userId, Drawing drawing);
  Boolean isInLargeMoneyPoolForDrawing(Long userId, Drawing drawing);
  List<Role> rolesForUser(Long userId);
  User save(User user);

  Character addCharacter(final User user, final String characterName);
}
