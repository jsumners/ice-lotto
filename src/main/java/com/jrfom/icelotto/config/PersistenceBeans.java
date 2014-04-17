package com.jrfom.icelotto.config;

import com.jrfom.icelotto.dao.*;
import com.jrfom.icelotto.dao.sqlite.*;
import com.jrfom.icelotto.service.*;
import com.jrfom.icelotto.service.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceBeans {
  @Bean
  public DrawingService drawingService() {
    return new DrawingRepositoryService();
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
  public UserService userService() {
    return new UserRepositoryService();
  }

  @Bean
  public CharacterDao characterDao() {
    return new CharacterRepository();
  }

  @Bean
  public DrawingDao drawingDao() {
    return new DrawingRepository();
  }

  @Bean
  public EntriesDao entriesDao() {
    return new EntriesRepository();
  }

  @Bean
  public GameItemDao gameItemDao() {
    return new GameItemRepository();
  }

  @Bean
  public MoneyDrawResultDao moneyDrawResultDao() {
    return new MoneyDrawResultRepository();
  }

  @Bean
  private PrizeDrawResultDao prizeDrawResultDao() {
    return new PrizeDrawResultRepository();
  }

  @Bean
  public PrizeItemDao prizeItemDao() {
    return new PrizeItemRepository();
  }

  @Bean
  public PrizePoolDao prizePoolDao() {
    return new PrizePoolRepository();
  }

  @Bean
  public PrizeTierDao prizeTierDao() {
    return new PrizeTierRepository();
  }

  @Bean
  public RoleDao roleDao() {
    return new RoleRepository();
  }

  @Bean
  public UserDao userDao() {
    return new UserRepository();
  }
}