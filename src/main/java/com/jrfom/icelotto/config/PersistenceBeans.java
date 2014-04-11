package com.jrfom.icelotto.config;

import com.jrfom.icelotto.dao.sqlite.CharacterRepository;
import com.jrfom.icelotto.dao.sqlite.GameItemRepository;
import com.jrfom.icelotto.dao.sqlite.RoleRepository;
import com.jrfom.icelotto.service.*;
import com.jrfom.icelotto.service.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceBeans {
  @Bean
  public CharacterRepository characterRepository() {
    return new CharacterRepository();
  }

  @Bean
  public DrawingService drawingService() {
    return new DrawingRepositoryService();
  }

  @Bean
  public GameItemRepository gameItemRepository() {
    return new GameItemRepository();
  }

  @Bean
  public GameItemService gameItemService() {
    return new GameItemRepositoryService();
  }

  @Bean
  public PrizeItemService prizeItemService() {
    return new PrizeItemRepositoryService();
  }

  @Bean
  public PrizePoolService prizePoolService() {
    return new PrizePoolRepositoryService();
  }

  @Bean
  public PrizeTierService prizeTierService() {
    return new PrizeTierRepositoryService();
  }

  @Bean
  public RoleRepository roleRepository() {
    return new RoleRepository();
  }

  @Bean
  public UserService userService() {
    return new UserRepositoryService();
  }
}