package com.jrfom.icelotto.dao;

import java.util.List;

import com.jrfom.icelotto.model.Entry;

public interface EntriesDao {
  Entry create(final Entry entry);
  void delete(Long entryId) throws Exception; // TODO: add exception when we give a damn
  List<Entry> findAll();
  List<Entry> findAllForDrawing(Long drawingId);
  List<Entry> findAllForDrawingAndUser(Long drawingId, Long userId);
  List<Entry> findAllForPool(Long poolId);
  List<Entry> findAllForTier(Long tierId);
  Entry findById(Long id);
  Entry save(Entry entry);
}
