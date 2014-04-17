package com.jrfom.icelotto.controllers;

import java.util.ArrayList;
import java.util.List;

import com.jrfom.icelotto.model.Character;
import com.jrfom.icelotto.model.User;
import com.jrfom.icelotto.model.websocket.UserSearchMessage;
import com.jrfom.icelotto.model.websocket.UserSearchResult;
import com.jrfom.icelotto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class UserSearchController {
  @Autowired
  private UserService userService;

  @MessageMapping("/admin/user/search")
  @SendTo("/topic/admin/user/search/result")
  public List<UserSearchResult> search(UserSearchMessage searchMessage) {
    List<UserSearchResult> results = new ArrayList<>(0);

    results.addAll(this.findUsers(searchMessage.getName()));

    return results;
  }

  private List<UserSearchResult> findUsers(String term) {
    List<UserSearchResult> results = new ArrayList<>(0);
    List<User> users = this.userService.findAllLike(term);

    for (User user : users) {
      UserSearchResult result = new UserSearchResult();
      result.setUserId(user.getId());
      result.setGw2DisplayName(user.getGw2DisplayName());
      result.setName(user.getDisplayName());

      List<String> characters = new ArrayList<>(0);
      List<Character> userCharacters = this.userService.charactersForUser(user.getId());
      for (Character character : userCharacters) {
        characters.add(character.getName());
      }
      result.setTokens(characters);

      results.add(result);
    }

    return results;
  }
}