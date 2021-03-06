package com.jrfom.icelotto.service;

import java.util.List;

import com.google.common.base.Optional;
import com.jrfom.icelotto.exception.PrizeTierNotFoundException;
import com.jrfom.icelotto.model.GameItem;
import com.jrfom.icelotto.model.PrizeTier;

public interface PrizeTierService {
  Optional<PrizeTier> create();
  void delete(Long prizeTierId) throws PrizeTierNotFoundException;
  List<PrizeTier> findAll();
  Optional<PrizeTier> findById(Long id);
  void save(PrizeTier prizeTier);

  void addItem(GameItem gameItem, Long tierId, Integer position, Integer count);
}
