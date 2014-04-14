package com.jrfom.icelotto.dao;

import java.util.List;

import com.jrfom.icelotto.exception.PrizeItemNotFoundException;
import com.jrfom.icelotto.model.PrizeItem;

public interface PrizeItemDao {
  PrizeItem create(final PrizeItem prizeItem);
  void delete(Long prizeItemId) throws PrizeItemNotFoundException;
  List<PrizeItem> findAll();
  PrizeItem findById(Long id);
  PrizeItem save(PrizeItem prizeItem);
}
