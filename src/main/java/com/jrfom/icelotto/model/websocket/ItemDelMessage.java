package com.jrfom.icelotto.model.websocket;

public class ItemDelMessage {
  private Long drawingId;
  private Long poolId;
  private Long tierId;
  private Long itemId;
  private Integer tierPosition;

  public ItemDelMessage() {}

  public Long getDrawingId() {
    return this.drawingId;
  }

  public void setDrawingId(Long drawingId) {
    this.drawingId = drawingId;
  }

  public Long getPoolId() {
    return this.poolId;
  }

  public void setPoolId(Long poolId) {
    this.poolId = poolId;
  }

  public Long getTierId() {
    return this.tierId;
  }

  public void setTierId(Long tierId) {
    this.tierId = tierId;
  }

  public Long getItemId() {
    return this.itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public Integer getTierPosition() {
    return this.tierPosition;
  }

  public void setTierPosition(Integer tierPosition) {
    this.tierPosition = tierPosition;
  }
}