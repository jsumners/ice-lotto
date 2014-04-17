package com.jrfom.icelotto.dao;

import java.util.List;

import com.jrfom.icelotto.exception.DrawingNotFoundException;
import com.jrfom.icelotto.model.Drawing;

public interface DrawingDao {
  Drawing create(final Drawing drawing);
  void delete(Long drawingId) throws DrawingNotFoundException;
  List<Drawing> findAll();
  Drawing findById(Long id);
  Drawing nextDrawing();
  Drawing previousDrawing();
  Drawing save(Drawing drawing);
}
