package com.jrfom.icelotto.dao;

import java.util.List;

import com.jrfom.icelotto.exception.GameItemNotFoundException;
import com.jrfom.icelotto.model.GameItem;

public interface GameItemDao {
  GameItem create(final GameItem gameItem);
  void delete(Long gameItemId) throws GameItemNotFoundException;
  List<GameItem> findAll();
  GameItem findById(Long id);
  GameItem save(GameItem gameItem);
}