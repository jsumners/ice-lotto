package com.jrfom.icelotto.model.websocket;

import org.threeten.bp.Instant;

public class EndDrawingResponse {
  private Long drawingId;
  private boolean ended;
  private Instant endTime;

  public EndDrawingResponse() {
    this.drawingId = -1l;
    this.ended = false;
  }

  public Long getDrawingId() {
    return this.drawingId;
  }

  public void setDrawingId(Long drawingId) {
    this.drawingId = drawingId;
  }

  public boolean isEnded() {
    return this.ended;
  }

  public void setEnded(boolean ended) {
    this.ended = ended;
  }

  public Instant getEndTime() {
    return this.endTime;
  }

  public void setEndTime(Instant endTime) {
    this.endTime = endTime;
  }
}