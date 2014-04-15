package com.jrfom.icelotto.dao;

import java.util.List;

import com.jrfom.icelotto.exception.PrizeTierNotFoundException;
import com.jrfom.icelotto.model.PrizeTier;

public interface PrizeTierDao {
  PrizeTier create(final PrizeTier prizeTier);
  void delete(Long prizeTierId) throws PrizeTierNotFoundException;
  List<PrizeTier> findAll();
  PrizeTier findById(Long id);
  PrizeTier save(PrizeTier prizeTier);
}
