package com.jrfom.icelotto.controllers;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jrfom.gw2.ApiClient;
import com.jrfom.gw2.api.model.items.Item;
import com.jrfom.icelotto.exception.GameItemNotFoundException;
import com.jrfom.icelotto.exception.PrizeTierNotFoundException;
import com.jrfom.icelotto.model.*;
import com.jrfom.icelotto.model.websocket.*;
import com.jrfom.icelotto.service.*;
import com.jrfom.icelotto.util.ImageDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.threeten.bp.Instant;

@Controller
public class DrawingController {
  private static final Logger log = LoggerFactory.getLogger(DrawingController.class);

  @Autowired
  private ApiClient apiClient;

  @Autowired
  private ImageDownloader imageDownloader;

  @Autowired
  private DrawingService drawingService;

  @Autowired
  private PrizeTierService prizeTierService;

  @Autowired
  private PrizePoolService prizePoolService;

  @Autowired
  private GameItemService gameItemService;

  @Autowired
  private UserService userService;

  @RequestMapping(
    value = "/drawings",
    method = RequestMethod.GET,
    produces = MediaType.TEXT_HTML_VALUE
  )
  public ModelAndView drawingsIndex() {
    ModelAndView modelAndView = new ModelAndView();
    List<Drawing> drawingList = this.drawingService.findAll();

    modelAndView.setViewName("drawings");
    modelAndView.addObject("drawingsList", drawingList);

    return modelAndView;
  }

  @RequestMapping(
    value = "/drawing/{id}",
    method = RequestMethod.GET,
    produces = MediaType.TEXT_HTML_VALUE
  )
  public ModelAndView editDrawing(@PathVariable Long id) {
    ModelAndView modelAndView = new ModelAndView();
    Optional<Drawing> drawingOptional = this.drawingService.findById(id);

    if (drawingOptional.isPresent()) {
      modelAndView.addObject("drawing", drawingOptional.get());
    }
    modelAndView.addObject("drawingId", id);

    User user = this.getUser();
    if (user != null) {
      modelAndView.addObject("user", user);
    }

    modelAndView.setViewName("drawing");
    return modelAndView;
  }

  @RequestMapping(
    value = "/drawing/previous",
    method = RequestMethod.GET,
    produces = MediaType.TEXT_HTML_VALUE
  )
  public ModelAndView previousDrawing() {
    ModelAndView mav = new ModelAndView();
    String viewName = "forward:/drawing";

    Optional<Drawing> drawingOptional = this.drawingService.previousDrawing();
    if (drawingOptional.isPresent()) {
      viewName += "/" + drawingOptional.get().getId();
    } else {
      viewName += "/-1";
    }

    mav.setViewName(viewName);
    return mav;
  }

  @MessageMapping("/admin/drawing/item/add")
  @SendTo("/topic/admin/drawing/item/add")
  public ItemAddResponse addItem(ItemAddMessage itemAddMessage) {
    ItemAddResponse response = new ItemAddResponse();

    try {
      GameItem gameItem = this.getGameItem(itemAddMessage.getItemId());

      if (gameItem == null) {
        throw new GameItemNotFoundException(
          "Item is in valid. Item id: " + itemAddMessage.getItemId()
        );
      }

      this.prizeTierService.addItem(
        gameItem,
        itemAddMessage.getTierId(),
        itemAddMessage.getTierPosition(),
        itemAddMessage.getCount()
      );
    } catch (PrizeTierNotFoundException | GameItemNotFoundException e) {
      response = new ItemAddResponse(false, e.getMessage());
    } catch (NullPointerException e) {
      response = new ItemAddResponse(false, "Item id was null. Try again?");
    }

    return response;
  }

  @MessageMapping("/admin/drawing/create")
  @SendTo("/topic/admin/drawing/created")
  public String createDrawing(DrawingCreateMessage drawingCreateMessage) {
    PrizePool smallPool = new PrizePool();
    PrizePool largePool = new PrizePool();
    this.drawingService.create(drawingCreateMessage.getDate(), smallPool, largePool);

    return "created";
  }

  @MessageMapping("/admin/drawing/deposit")
  @SendTo("/topic/admin/drawing/deposit/added")
  public DepositEntryResponse depositEntry(DepositEntryMessage depositEntryMessage) {
    DepositEntryResponse response = new DepositEntryResponse();
    Optional<Drawing> drawingOptional =
      this.drawingService.findById(depositEntryMessage.getDrawingId());

    // TODO: clean this mess up
    if (drawingOptional.isPresent()) {
      User user;
      Drawing drawing = drawingOptional.get();

      Optional<PrizeTier> prizeTierOptional =
        this.prizeTierService.findById(depositEntryMessage.getTierId());
      Optional<User> userOptional =
        this.userService.findByGw2DisplayName(depositEntryMessage.getGw2DisplayName());

      if (!userOptional.isPresent()) {
        user = new User(depositEntryMessage.getGw2DisplayName());
        user = this.userService.save(user);
      } else {
        user = userOptional.get();
      }

      if (prizeTierOptional.isPresent()) {
        PrizeTier prizeTier = prizeTierOptional.get();
        Entry entry = new Entry(user, prizeTier, depositEntryMessage.getAmount());
        drawing.addEntry(entry);
        drawing = this.drawingService.save(drawing);

        response.setSmallPoolTotal(drawing.getSmallPoolTotal());
        response.setLargePoolTotal(drawing.getLargePoolTotal());
      }
    }

    return response;
  }

  @MessageMapping("/admin/drawing/start")
  @SendTo("/topic/drawing/started")
  public StartDrawingResponse startDrawing(StartDrawingMessage startDrawingMessage) {
    StartDrawingResponse result = new StartDrawingResponse();
    Optional<Drawing> drawingOptional =
      this.drawingService.findById(startDrawingMessage.getDrawingId());

    if (drawingOptional.isPresent()) {
      result.setDrawingId(drawingOptional.get().getId());
      result.setStarted(true);

      drawingOptional.get().setInProgress(true);
      drawingOptional.get().setStarted(Instant.now());
      this.drawingService.save(drawingOptional.get());
    }

    return result;
  }

  @MessageMapping("/admin/drawing/end")
  @SendTo("/topic/drawing/ended")
  public EndDrawingResponse endDrawing(EndDrawingMessage message) {
    EndDrawingResponse response = new EndDrawingResponse();
    Optional<Drawing> drawingOptional =
      this.drawingService.findById(message.getDrawingId());

    if (drawingOptional.isPresent()) {
      Drawing drawing = drawingOptional.get();
      drawing.setInProgress(false);
      drawing.setEnded(Instant.now());
      this.drawingService.save(drawing);

      response.setDrawingId(drawing.getId());
      response.setEnded(true);
      response.setEndTime(drawing.getEnded());
    }

    return response;
  }

  @MessageMapping("/admin/drawing/draw/tier")
  @SendTo("/topic/drawing/tier/winner")
  public DrawForTierResponse tierDraw(DrawForTierMessage message) {
    DrawForTierResponse response = new DrawForTierResponse();
    response.setPoolId(message.getPoolId());
    response.setTierId(message.getTierId());

    Optional<PrizeTier> prizeTierOptional =
      this.prizeTierService.findById(message.getTierId());
    if (prizeTierOptional.isPresent()) {
      PrizeTier tier = prizeTierOptional.get();
      PrizeDrawResult drawResult = tier.draw();

      response.setResult(drawResult);
      this.prizeTierService.save(tier);
    }

    return response;
  }

  @MessageMapping("/admin/drawing/draw/pool/money")
  @SendTo("/topic/drawing/pool/winner")
  public DrawMoneyResponse poolDraw(DrawMoneyMessage message) {
    DrawMoneyResponse response = new DrawMoneyResponse();

    Optional<Drawing> drawingOptional =
      this.drawingService.findById(message.getDrawingId());
    if (drawingOptional.isPresent()) {
      Drawing drawing = drawingOptional.get();
      PrizePool prizePool;
      double amountWon;

      if (message.isSmallPool()) {
        prizePool = drawing.getSmallPool();
        amountWon = Math.floor(drawing.getSmallPoolTotal() / 2);
      } else {
        prizePool = drawing.getLargePool();
        amountWon = Math.floor(drawing.getLargePoolTotal() / 2);
      }

      MoneyDrawResult drawResult = prizePool.draw();
      drawResult.setDrawing(drawing);
      drawResult.setAmountWon((int) amountWon);
      prizePool.setMoneyDrawResult(drawResult);
      this.prizePoolService.save(prizePool);

      response.setPoolId(prizePool.getId());
      response.setResult(drawResult);
    }

    return response;
  }

  private GameItem getGameItem(Long itemId) {
    // TODO: this should be in a service
    Preconditions.checkNotNull(itemId);
    GameItem gameItem = null;
    Optional<GameItem> gameItemOptional =
        this.gameItemService.findById(itemId);

    if (gameItemOptional.isPresent()) {
      gameItem = gameItemOptional.get();
    } else {
      Optional<Item> itemOptional = this.apiClient.getItemDetails(itemId.intValue());
      if (itemOptional.isPresent()) {
        Item item = itemOptional.get();
        gameItem = new GameItem(
          item.getItemId().longValue(),
          item.getName(),
          item.getDescription()
        );
        gameItem.setMinLevel(item.getLevel());
        gameItem.setRarity(ItemRarity.valueOf(item.getRarity()));

        String url = String.format(
          "https://render.guildwars2.com/file/%s/%s.png",
          item.getIconFileSignature(),
          item.getIconFileId()
        );
        this.imageDownloader.downloadImageAtUrlAs(url, "icons/" + item.getItemId());
        gameItem.setImageUrl(url);
      }

      this.gameItemService.save(gameItem);
    }

    return gameItem;
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