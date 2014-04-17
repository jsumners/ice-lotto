package com.jrfom.icelotto.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;

import com.google.common.base.Optional;
import com.jrfom.icelotto.dao.DrawingDao;
import com.jrfom.icelotto.exception.DrawingNotFoundException;
import com.jrfom.icelotto.model.Drawing;
import com.jrfom.icelotto.model.PrizePool;
import com.jrfom.icelotto.service.DrawingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threeten.bp.Instant;

@Service
public class DrawingRepositoryService implements DrawingService {
  private static final Logger log = LoggerFactory.getLogger(DrawingRepositoryService.class);

  @Resource
  private DrawingDao drawingDao;

  @Override
  @Transactional
  public Optional<Drawing> create(Instant scheduled) {
    return this.create(scheduled, null, null);
  }

  @Override
  @Transactional(rollbackFor = PersistenceException.class)
  public Optional<Drawing> create(Instant scheduled, PrizePool smallPool, PrizePool largePool) {
    log.debug("Creating new drawing");
    Optional<Drawing> result = Optional.absent();
    Drawing record = new Drawing(scheduled, smallPool, largePool);

    try {
      record = this.drawingDao.save(record);
      result = Optional.of(record);
    } catch (DataAccessException e) {
      log.error("Could not create new drawing: `{}`", e.getMessage());
      log.debug(e.toString());
    }

    return result;
  }

  @Override
  @Transactional(rollbackFor = DrawingNotFoundException.class)
  public void delete(Long drawingId) throws DrawingNotFoundException {
    log.debug("Deleting drawing with id: `{}`", drawingId);
    Drawing deleted = this.drawingDao.findById(drawingId);

    if (deleted == null) {
      log.debug("Could not find drawing with id: `{}`", drawingId);
      throw new DrawingNotFoundException();
    } else {
      this.drawingDao.delete(drawingId);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<Drawing> findAll() {
    log.debug("Finding all drawings");
    return this.drawingDao.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Drawing> findById(Long id) {
    log.debug("Finding drawing with id: `{}`", id);
    Optional<Drawing> result = Optional.absent();
    Drawing drawing = this.drawingDao.findById(id);

    if (drawing != null) {
      result = Optional.of(drawing);
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional(readOnly = true)
  public Optional<Drawing> nextDrawing() {
    Optional<Drawing> result = Optional.absent();
    Drawing drawing = this.drawingDao.nextDrawing();

    if (drawing != null) {
      result = Optional.of(drawing);
    }

    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Drawing> previousDrawing() {
    Optional<Drawing> result = Optional.absent();
    Drawing drawing = this.drawingDao.previousDrawing();

    if (drawing != null) {
      result = Optional.of(drawing);
    }

    return result;
  }

  @Override
  @Transactional
  public Drawing save(Drawing drawing) {
    return this.drawingDao.save(drawing);
  }
}