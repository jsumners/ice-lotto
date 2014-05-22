package com.jrfom.icelotto.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.google.common.base.Optional;
import com.jrfom.icelotto.dao.PrizeItemDao;
import com.jrfom.icelotto.dao.PrizeTierDao;
import com.jrfom.icelotto.exception.PrizeTierNotFoundException;
import com.jrfom.icelotto.model.GameItem;
import com.jrfom.icelotto.model.PrizeItem;
import com.jrfom.icelotto.model.PrizeTier;
import com.jrfom.icelotto.service.PrizeTierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrizeTierRepositoryService implements PrizeTierService {
  private static final Logger log = LoggerFactory.getLogger(PrizeTierRepositoryService.class);

  @Resource
  private PrizeTierDao prizeTierDao;

  @Resource
  private PrizeItemDao prizeItemDao;

  @Override
  @Transactional
  public Optional<PrizeTier> create() {
    log.debug("Creating new prize tier");
    Optional<PrizeTier> result = Optional.absent();
    PrizeTier record = new PrizeTier();

    try {
      record = this.prizeTierDao.save(record);
      result = Optional.of(record);
    } catch (DataAccessException e) {
      log.error("Something went wrong creating prize tier: `{}`", e.getMessage());
      log.debug(e.toString());
    }

    return result;
  }

  @Override
  @Transactional(rollbackFor = PrizeTierNotFoundException.class)
  public void delete(Long prizeTierId) throws PrizeTierNotFoundException {
    log.debug("Deleting prize tier with id: `{}`", prizeTierId);
    PrizeTier deleted = this.prizeTierDao.findById(prizeTierId);

    if (deleted == null) {
      log.debug("Could not find prize tier with id: `{}`", prizeTierId);
      throw new PrizeTierNotFoundException();
    } else {
      this.prizeTierDao.delete(prizeTierId);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<PrizeTier> findAll() {
    log.debug("Finding all prize tiers");
    return this.prizeTierDao.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<PrizeTier> findById(Long id) {
    log.debug("Finding prize tier with id: `{}`", id);
    Optional<PrizeTier> result = Optional.absent();
    PrizeTier tier = this.prizeTierDao.findById(id);

    if (tier != null) {
      result = Optional.of(tier);
    }

    return result;
  }

  @Override
  @Transactional
  public void save(PrizeTier prizeTier) {
    this.prizeTierDao.save(prizeTier);
  }

  @Override
  @Transactional
  public void addItem(GameItem gameItem, Long tierId, Integer position, Integer count) {
    log.debug(
      "Adding item: [itemId: `{}`, tierId: `{}`, position: `{}`, count: `{}`]",
      gameItem.getId(),
      tierId,
      position,
      count
    );
    Optional<PrizeTier> tierOptional = this.findById(tierId);
    if (!tierOptional.isPresent()) {
      throw new PrizeTierNotFoundException();
    }

    PrizeItem prizeItem = new PrizeItem(gameItem);
    prizeItem.setCount(count);
    prizeItem = this.prizeItemDao.create(prizeItem);

    PrizeTier tier = tierOptional.get();
    switch (position) {
      case 1:
        tier.setItem1(prizeItem);
        break;
      case 2:
        tier.setItem2(prizeItem);
        break;
      case 3:
        tier.setItem3(prizeItem);
        break;
      case 4:
        tier.setItem4(prizeItem);
        break;
      case 5:
        tier.setItem5(prizeItem);
        break;
      case 6:
        tier.setItem6(prizeItem);
        break;
      case 7:
        tier.setItem7(prizeItem);
        break;
      case 8:
        tier.setItem8(prizeItem);
        break;
      case 9:
        tier.setItem9(prizeItem);
        break;
      case 10:
        tier.setItem10(prizeItem);
        break;
      default:
    }

    this.save(tier);
  }
}