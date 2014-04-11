package com.jrfom.icelotto.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import com.google.common.base.Optional;
import com.jrfom.icelotto.dao.sqlite.GameItemRepository;
import com.jrfom.icelotto.exception.GameItemNotFoundException;
import com.jrfom.icelotto.model.GameItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
  ContextConfig.class
})
public class GameItemRepositoryServiceTest {
  private static final Logger log = LoggerFactory.getLogger(GameItemRepositoryServiceTest.class);

  @Autowired
  private GameItemRepository prizeItemRepository;

  @Autowired
  private GameItemRepositoryService prizeItemRepositoryService;

  private GameItem prizeItem;

  private final Long ITEM_ID = Long.valueOf(2495);
  private final String ITEM_NAME = "Foo";
  private final String ITEM_DESCRIPTION = "Bar";

  @Before
  public void setup() {
    reset(this.prizeItemRepository); // Since we use Spring to autowire it
    this.prizeItem = new GameItem(this.ITEM_ID, this.ITEM_NAME, this.ITEM_DESCRIPTION);
  }

  @Test
  public void create() {
    log.info("Running GameItemRepositoryService.create() test");

    // Setup the "database"
    GameItem persisted = new GameItem(this.ITEM_ID, this.ITEM_NAME, this.ITEM_DESCRIPTION);
    when(this.prizeItemRepository.save(any(GameItem.class))).thenReturn(persisted);

    Optional<GameItem> result = this.prizeItemRepositoryService.create(
      this.ITEM_ID,
      this.ITEM_NAME,
      this.ITEM_DESCRIPTION
    );
    assertTrue(result.isPresent());

    GameItem returned = result.get();

    // Now we get a copy of the argument that was passed to the JPA save method
    ArgumentCaptor<GameItem> prizeItemArgumentCaptor = ArgumentCaptor.forClass(GameItem.class);
    verify(this.prizeItemRepository, times(1)).save(prizeItemArgumentCaptor.capture());
    verifyNoMoreInteractions(this.prizeItemRepository);

    // And then verify that it matches what we gave the create method
    assertEquals(this.prizeItem.getId(), prizeItemArgumentCaptor.getValue().getId());
    assertEquals(this.prizeItem.getName(), prizeItemArgumentCaptor.getValue().getName());
    assertEquals(this.prizeItem.getDescription(), prizeItemArgumentCaptor.getValue().getDescription());

    assertEquals(persisted, returned);
  }

  @Test(expected = NullPointerException.class)
  public void createWithNullId() {
    log.info("Running GameItemRepositoryService.create() with null id test");
    GameItem persisted = new GameItem(this.ITEM_ID, this.ITEM_NAME, this.ITEM_DESCRIPTION);
    when(this.prizeItemRepository.save(any(GameItem.class))).thenReturn(persisted);

    Optional<GameItem> result = this.prizeItemRepositoryService.create(
      null,
      this.ITEM_NAME,
      this.ITEM_DESCRIPTION
    );
    assertFalse(result.isPresent());
  }

  @Test
  public void delete() throws GameItemNotFoundException {
    log.info("Running GameItemRepositoryService.delete() success test");
    GameItem deleted = new GameItem(this.ITEM_ID, this.ITEM_NAME, this.ITEM_DESCRIPTION);
    when(this.prizeItemRepository.findById(this.ITEM_ID)).thenReturn(deleted);

    this.prizeItemRepositoryService.delete(this.ITEM_ID);
    verify(this.prizeItemRepository).findById(this.ITEM_ID);
    verify(this.prizeItemRepository).delete(this.ITEM_ID);
    verifyNoMoreInteractions(this.prizeItemRepository);
  }

  @Test(expected = GameItemNotFoundException.class)
  public void deleteGameItemNotFound() throws GameItemNotFoundException {
    when(this.prizeItemRepository.findById(this.ITEM_ID)).thenReturn(null);

    this.prizeItemRepositoryService.delete(this.ITEM_ID);

    verify(this.prizeItemRepository).findById(this.ITEM_ID);
    verifyNoMoreInteractions(this.prizeItemRepository);
  }

  @Test
  public void findAll() {
    List<GameItem> prizeItems = new ArrayList<>();
    when(this.prizeItemRepository.findAll()).thenReturn(prizeItems);

    List<GameItem> returned = this.prizeItemRepositoryService.findAll();

    verify(this.prizeItemRepository).findAll();
    verifyNoMoreInteractions(this.prizeItemRepository);

    assertEquals(prizeItems, returned);
  }

  @Test
  public void findById() {
    GameItem prizeItem = new GameItem(this.ITEM_ID, this.ITEM_NAME, this.ITEM_DESCRIPTION);
    when(this.prizeItemRepository.findById(this.ITEM_ID)).thenReturn(prizeItem);

    Optional<GameItem> result = this.prizeItemRepositoryService.findById(this.ITEM_ID);

    verify(this.prizeItemRepository).findById(this.ITEM_ID);
    verifyNoMoreInteractions(this.prizeItemRepository);

    assertTrue(result.isPresent());
    GameItem returned = result.get();

    assertEquals(prizeItem, returned);
  }
}

@Configuration
class ContextConfig {
  @Bean
  public JdbcTemplate jdbcTemplate() {
    return mock(JdbcTemplate.class);
  }

  @Bean
  public EntityManagerFactory entityManagerFactory() {
    return mock(EntityManagerFactory.class);
  }

  @Bean
  public GameItemRepository prizeItemRepository() {
    GameItemRepository repository = mock(GameItemRepository.class);
    return repository;
  }

  @Bean
  public GameItemRepositoryService prizeItemRepositoryService() {
    return new GameItemRepositoryService();
  }
}