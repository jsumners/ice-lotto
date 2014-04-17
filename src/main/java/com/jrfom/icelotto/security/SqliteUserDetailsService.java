package com.jrfom.icelotto.security;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;
import com.jrfom.icelotto.model.Role;
import com.jrfom.icelotto.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class SqliteUserDetailsService implements UserDetailsService {
  private static final Logger log = LoggerFactory.getLogger(SqliteUserDetailsService.class);

  @Autowired
  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.debug("Looking up details for user: `{}`", username);
    User springUser = null;

    Optional<com.jrfom.icelotto.model.User> userOptional =
      this.userService.findByGw2DisplayName(username);
    if (!userOptional.isPresent()) {
      log.error("Could not find a user with username: `{}`", username);
      throw new UsernameNotFoundException("User not found: " + username);
    }

    com.jrfom.icelotto.model.User localUser = userOptional.get();
    localUser.setRoles(this.userService.rolesForUser(localUser.getId()));
    springUser = new User(
      localUser.getGw2DisplayName(),
      localUser.getPassword(),
      localUser.isEnabled(),
      true, // TODO: implement account expiration
      true, // TODO: implement credentials expiration
      true,  // TODO: implement account locking
      SqliteUserDetailsService.getGrantedAuthorities(localUser.getRoles())
    );

    return springUser;
  }

  public static List<SimpleGrantedAuthority> getGrantedAuthorities(List<Role> roles) {
    List<SimpleGrantedAuthority> authorities = new ArrayList<>(0);

    for (Role role : roles) {
      authorities.add(new SimpleGrantedAuthority(
        String.format("ROLE_%s", role.getName()).toUpperCase()
      ));
    }

    return authorities;
  }
}