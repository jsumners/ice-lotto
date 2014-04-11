package com.jrfom.icelotto.repository;

import com.jrfom.icelotto.model.Drawing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DrawingRepository extends JpaRepository<Drawing, Long> {
  @Query(
    "select d " +
    "from Drawing d " +
    "where d.ended is null " +
    "and d.scheduled = (select min(z.scheduled) from Drawing z where z.ended is null) " +
    "order by d.scheduled asc"
  )
  Drawing nextDrawing();

  @Query(
    "select d " +
    "from Drawing d " +
    "where d.ended = (" +
      "select max(z.ended) " +
      "from Drawing z" +
    ")"
  )
  Drawing previousDrawing();
}
