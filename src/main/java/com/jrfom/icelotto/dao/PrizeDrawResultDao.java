package com.jrfom.icelotto.dao;

import java.util.List;

import com.jrfom.icelotto.model.PrizeDrawResult;

public interface PrizeDrawResultDao {
  PrizeDrawResult create(final PrizeDrawResult drawResult);
  void delete(Long resultId) throws Exception;
  List<PrizeDrawResult> findAll();
  List<PrizeDrawResult> findAllForUser(Long userId);
  PrizeDrawResult findById(Long id);
  PrizeDrawResult findByPrizeItemId(Long prizeItemId);
  PrizeDrawResult findByPrizeTierId(Long tierId);
  PrizeDrawResult save(PrizeDrawResult drawResult);
}
