package com.jrfom.icelotto.dao;

import java.util.List;

import com.jrfom.icelotto.exception.PrizePoolNotFoundException;
import com.jrfom.icelotto.model.PrizePool;

public interface PrizePoolDao {
  PrizePool create(final PrizePool prizePool);
  void delete(Long prizePoolId) throws PrizePoolNotFoundException;
  List<PrizePool> findAll();
  PrizePool findById(Long id);
  Integer poolTotal(Long poolId);
  Integer poolTotalForUser(Long poolId, Long userId);
  PrizePool save(PrizePool prizePool);
}