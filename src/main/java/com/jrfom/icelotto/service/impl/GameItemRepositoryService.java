package com.jrfom.icelotto.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.google.common.base.Optional;
import com.jrfom.icelotto.dao.GameItemDao;
import com.jrfom.icelotto.exception.GameItemNotFoundException;
import com.jrfom.icelotto.model.GameItem;
import com.jrfom.icelotto.service.GameItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameItemRepositoryService implements GameItemService {
  private static final Logger log = LoggerFactory.getLogger(GameItemRepositoryService.class);

  @Resource
  private GameItemDao gameItemDao;

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public Optional<GameItem> create(Long id, String name) {
    return this.create(id, name, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public Optional<GameItem> create(Long id, String name, String description) {
    log.debug(
      "Creating new game item: [id: `{}`, name: `{}`, desc: `{}`]",
      id,
      name,
      description
    );
    Optional<GameItem> result = Optional.absent();
    GameItem record = new GameItem(id, name, description);

    try {
      record = this.gameItemDao.save(record);
      result = Optional.of(record);
    } catch (NullPointerException e) {
      log.error("Attempted to create a game item with a null id: `{}`", e.getMessage());
      log.debug(e.toString());
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(rollbackFor = GameItemNotFoundException.class)
  public void delete(Long GameItemId) throws GameItemNotFoundException {
    log.debug("Deleting game item with id: `{}`", GameItemId);
    GameItem deleted = this.gameItemDao.findById(GameItemId);

    if (deleted == null) {
      log.debug("Could not find game item with id: `{}`", GameItemId);
      throw new GameItemNotFoundException();
    } else {
      this.gameItemDao.delete(GameItemId);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public List<GameItem> findAll() {
    log.debug("Finding all game items");
    return this.gameItemDao.findAll();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public Optional<GameItem> findById(Long id) {
    log.debug("Finding game item with id: `{}`", id);
    Optional<GameItem> result = Optional.absent();
    GameItem item = this.gameItemDao.findById(id);

    if (item != null) {
      result = Optional.of(item);
    }

    return result;
  }

  @Override
  @Transactional
  public void save(GameItem gameItem) {
    this.entityManager.merge(gameItem);
  }
}