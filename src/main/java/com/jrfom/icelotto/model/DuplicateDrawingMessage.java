package com.jrfom.icelotto.model;

public class DuplicateDrawingMessage {
  private Long drawingId;
  private String startDateTime;

  public DuplicateDrawingMessage() {}

  public Long getDrawingId() {
    return this.drawingId;
  }

  public void setDrawingId(Long drawingId) {
    this.drawingId = drawingId;
  }

  public String getStartDateTime() {
    return this.startDateTime;
  }

  public void setStartDateTime(String startDateTime) {
    this.startDateTime = startDateTime;
  }
}