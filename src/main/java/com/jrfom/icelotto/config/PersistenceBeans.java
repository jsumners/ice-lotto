package com.jrfom.icelotto.config;

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
}