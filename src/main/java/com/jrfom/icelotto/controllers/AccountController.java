package com.jrfom.icelotto.controllers;

import com.google.common.base.Optional;
import com.jrfom.icelotto.model.Character;
import com.jrfom.icelotto.model.User;
import com.jrfom.icelotto.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.threeten.bp.ZoneId;

/**
 * This controller handles individual users viewing and modifying their
 * own accounts. Administration of all users is handled through
 * {@link com.jrfom.icelotto.controllers.UsersController}.
 */
@Controller
public class AccountController {
  private static final Logger log = LoggerFactory.getLogger(AccountController.class);

  @Autowired
  private UserService userService;

  @RequestMapping(
    value = "/account",
    method = RequestMethod.GET
  )
  public ModelAndView index(@AuthenticationPrincipal UserDetails userDetails) {
    log.debug("Received request to view account: `{}`", userDetails.getUsername());
    String user = userDetails.getUsername();
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/account/view");
    mav.addObject("userAuthorized", false);

    Optional<User> userOptional = this.userService.findByGw2DisplayName(user);

    if (userOptional.isPresent()) {
      mav.addObject("user", userOptional.get());
    } else {
      mav.addObject("userErrorMessage", "Could not find user: " + user);
    }

    return mav;
  }

  @RequestMapping(
    value = "/account/change/password",
    method = RequestMethod.GET
  )
  public String setPasswordForm(@AuthenticationPrincipal UserDetails userDetails) {
    log.debug("Received request for change password form from `{}`", userDetails.getUsername());
    return "/account/password";
  }

  @RequestMapping(
    value = "/account/change/password/set",
    method = RequestMethod.POST
  )
  public ModelAndView setPassword(
    @RequestParam String oldPassword,
    @RequestParam String newPassword,
    @RequestParam String newPasswordVerify,
    @AuthenticationPrincipal UserDetails userDetails,
    RedirectAttributes redirectAttributes)
  {
    log.debug("Received request to set new password for `{}`", userDetails.getUsername());
    ModelAndView mav = new ModelAndView();
    redirectAttributes.addFlashAttribute("error", "false");
    redirectAttributes.addFlashAttribute("message", "Password successfully changed.");

    mav.setViewName("redirect:/account");
    return mav;
  }

  @RequestMapping(
    value = "/account/edit",
    method = RequestMethod.GET,
    produces = MediaType.TEXT_HTML_VALUE
  )
  public ModelAndView editView(@AuthenticationPrincipal UserDetails userDetails) {
    log.debug("Received request to edit account: `{}`", userDetails.getUsername());
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/account/edit");
    mav.addObject("tzs", ZoneId.OLD_IDS_POST_2005);

    Optional<User> userOptional =
      this.userService.findByGw2DisplayName(userDetails.getUsername());
    if (userOptional.isPresent()) {
      mav.addObject("user", userOptional.get());
    } else {
      mav.addObject("userErrorMessage", "Could not find user: " + userDetails.getUsername());
    }

    return mav;
  }

  @RequestMapping(
    value = "/account/edit",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
  )
  public ModelAndView editSubmit(
    @AuthenticationPrincipal UserDetails userDetails,
    RedirectAttributes redirectAttributes,
    @RequestParam String email,
    @RequestParam String timeZone,
    @RequestParam String dateFormat)
  {
    log.debug("Received request to update account: `{}`", userDetails.getUsername());
    ModelAndView mav = new ModelAndView();

    Optional<User> userOptional =
      this.userService.findByGw2DisplayName(userDetails.getUsername());
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      user.setEmail((email != null) ? email : user.getEmail());
      user.setTimeZone((timeZone != null) ? timeZone : user.getTimeZone());
      user.setDatetimeFormat((dateFormat != null) ? dateFormat : user.getDatetimeFormat());

      this.userService.save(user);
    }

    // TODO: maybe handle some errors here and redirect back with flash attributes

    mav.setViewName("redirect:/account");
    return mav;
  }

  @RequestMapping(
    value = "/account/add/character",
    method = RequestMethod.PUT,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(HttpStatus.ACCEPTED)
  @ResponseBody
  public Character addCharacter(@RequestBody String characterName, @AuthenticationPrincipal UserDetails userDetails) {
    log.debug("Received request to add new character [`{}`, `{}`]", userDetails.getUsername(), characterName);
    Character character = new Character(characterName);
    Optional<User> userOptional =
      this.userService.findByGw2DisplayName(userDetails.getUsername());

    if (userOptional.isPresent()) {
      character = this.userService.addCharacter(userOptional.get(), characterName);
    }

    return character;
  }
}