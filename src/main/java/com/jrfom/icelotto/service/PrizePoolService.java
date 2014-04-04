package com.jrfom.icelotto.service;

import java.util.List;

import com.google.common.base.Optional;
import com.jrfom.icelotto.exception.PrizePoolNotFoundException;
import com.jrfom.icelotto.model.PrizePool;

public interface PrizePoolService {
  Optional<PrizePool> create();
  void delete(Long prizePoolId) throws PrizePoolNotFoundException;
  List<PrizePool> findAll();
  Optional<PrizePool> findById(Long id);
  PrizePool save(PrizePool prizePool);
}
