package com.jrfom.icelotto.controllers;

import com.google.common.base.Optional;
import com.jrfom.icelotto.model.User;
import com.jrfom.icelotto.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.threeten.bp.Instant;

@Controller
public class DateFormatController {
  private static final Logger log = LoggerFactory.getLogger(DateFormatController.class);

  @Autowired
  private UserService userService;

  @RequestMapping(
    value = "/format/epoch/second/{epochSecond}",
    method = RequestMethod.GET,
    consumes = MediaType.TEXT_PLAIN_VALUE,
    produces = MediaType.TEXT_PLAIN_VALUE
  )
  @ResponseBody
  public String restFormatEpochSecond(@PathVariable Integer epochSecond) {
    log.debug("Received REST request to format epoch second: `{}`", epochSecond);
    return this.formatEpochSecond(epochSecond);
  }

  // TODO: This method is not working for some reason. Maybe timing?
  @MessageMapping("/app/format/epoch/second")
  @SendToUser("/topic/epoch/second/formatted")
  public String wsFormatEpochSecond(Integer epochSecond) {
    log.debug("Received websocket request to format epoch second: `{}`", epochSecond);
    return this.formatEpochSecond(epochSecond);
  }

  private String formatEpochSecond(Integer epochSecond) {
    User user = this.getUser();
    return user.localizeDatetime(Instant.ofEpochSecond(epochSecond));
  }

  /**
   * Looks up the currently logged in user via the Spring Security context
   * and returns an instance of {@link com.jrfom.icelotto.model.User}.
   *
   * @return {@code null} if a user is not found otherwise and instance of
   * {@link com.jrfom.icelotto.model.User}.
   */
  private User getUser() {
    User user = null;
    UserDetails userDetails =
      (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Optional<User> userOptional =
      this.userService.findByGw2DisplayName(userDetails.getUsername());

    if (userOptional.isPresent()) {
      user = userOptional.get();
    }

    return user;
  }
}