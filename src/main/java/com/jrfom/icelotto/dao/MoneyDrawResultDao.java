package com.jrfom.icelotto.dao;

import java.util.List;

import com.jrfom.icelotto.model.MoneyDrawResult;

public interface MoneyDrawResultDao {
  MoneyDrawResult create(final MoneyDrawResult drawResult);
  void delete(Long drawResultId) throws Exception;
  List<MoneyDrawResult> findAll();
  List<MoneyDrawResult> findAllForDrawing(Long drawingId);
  List<MoneyDrawResult> findAllForUser(Long userid);
  MoneyDrawResult findById(Long id);
  MoneyDrawResult findByPoolId(Long id);
  MoneyDrawResult save(MoneyDrawResult drawResult);
}
