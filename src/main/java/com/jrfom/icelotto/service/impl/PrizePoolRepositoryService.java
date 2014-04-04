package com.jrfom.icelotto.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.google.common.base.Optional;
import com.jrfom.icelotto.exception.PrizePoolNotFoundException;
import com.jrfom.icelotto.model.PrizePool;
import com.jrfom.icelotto.repository.PrizePoolRepository;
import com.jrfom.icelotto.service.PrizePoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrizePoolRepositoryService implements PrizePoolService {
  private static final Logger log = LoggerFactory.getLogger(PrizePoolRepositoryService.class);

  @Resource
  private PrizePoolRepository prizePoolRepository;

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Optional<PrizePool> create() {
    log.debug("Creating new prize pool");
    Optional<PrizePool> result = Optional.absent();
    PrizePool record = new PrizePool();

    try {
      record = this.prizePoolRepository.save(record);
      result = Optional.of(record);
    } catch (DataAccessException e) {
      log.error("Something went wrong creating prize pool: `{}`", e.getMessage());
      log.debug(e.toString());
    }

    return result;
  }

  @Override
  @Transactional(rollbackFor = PrizePoolNotFoundException.class)
  public void delete(Long prizePoolId) throws PrizePoolNotFoundException {
    log.debug("Deleting prize pool with id: `{}`", prizePoolId);
    PrizePool deleted = this.prizePoolRepository.findOne(prizePoolId);

    if (deleted == null) {
      log.debug("Could not find prize pool with id: `{}`", prizePoolId);
      throw new PrizePoolNotFoundException();
    } else {
      this.prizePoolRepository.delete(prizePoolId);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<PrizePool> findAll() {
    log.debug("Finding all prize pools");
    return this.prizePoolRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<PrizePool> findById(Long id) {
    log.debug("Finding prize pool with id: `{}`", id);
    Optional<PrizePool> result = Optional.absent();
    PrizePool pool = this.prizePoolRepository.findOne(id);

    if (pool != null) {
      result = Optional.of(pool);
    }

    return result;
  }

  @Override
  @Transactional
  public PrizePool save(PrizePool prizePool) {
    log.debug("Saving prize pool with id: `{}`", prizePool.getId());
    return this.entityManager.merge(prizePool);
  }
}