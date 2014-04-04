package com.jrfom.icelotto.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.google.common.base.Optional;
import com.jrfom.icelotto.exception.PrizeItemNotFoundException;
import com.jrfom.icelotto.model.GameItem;
import com.jrfom.icelotto.model.PrizeItem;
import com.jrfom.icelotto.repository.PrizeItemRepository;
import com.jrfom.icelotto.service.PrizeItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrizeItemRepositoryService implements PrizeItemService {
  private static final Logger log = LoggerFactory.getLogger(PrizeItemRepositoryService.class);

  @Resource
  private PrizeItemRepository prizeItemRepository;

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public Optional<PrizeItem> create(GameItem gameItem) {
    log.debug("Creating new prize item with game item: `{}`", gameItem.toString());
    Optional<PrizeItem> result = Optional.absent();
    PrizeItem record = new PrizeItem(gameItem);

    try {
      record = this.prizeItemRepository.save(record);
      result = Optional.of(record);
    } catch (DataAccessException e) {
      log.error("Could not create prize item record: `{}`", e.getMessage());
      log.debug(e.toString());
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(rollbackFor = PrizeItemNotFoundException.class)
  public void delete(Long prizeItemId) throws PrizeItemNotFoundException {
    log.debug("Deleting prize item with id: `{}`", prizeItemId);
    PrizeItem deleted = this.prizeItemRepository.findOne(prizeItemId);

    if (deleted == null) {
      log.debug("Could not find prize item with id: `{}`", prizeItemId);
      throw new PrizeItemNotFoundException();
    } else {
      this.prizeItemRepository.delete(prizeItemId);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public List<PrizeItem> findAll() {
    log.debug("Finding all prize items");
    return this.prizeItemRepository.findAll();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public Optional<PrizeItem> findById(Long id) {
    log.debug("Finding prize item with id: `{}`", id);
    Optional<PrizeItem> result = Optional.absent();
    PrizeItem item = this.prizeItemRepository.findOne(id);

    if (item != null) {
      result = Optional.of(item);
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void save(PrizeItem prizeItem) {
    this.entityManager.merge(prizeItem);
  }
}