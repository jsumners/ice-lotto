package com.jrfom.icelotto.model;

import javax.persistence.*;

@Entity
@Table(name = "prize_items")
public class PrizeItem {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private GameItem gameItem;

  @Column
  private Integer count;

  public PrizeItem() {}

  public PrizeItem(GameItem gameItem) {
    this(gameItem, 1);
  }

  public PrizeItem(GameItem gameItem, Integer count) {
    this.gameItem = gameItem;
    this.count = count;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public GameItem getGameItem() {
    return this.gameItem;
  }

  public void setGameItem(GameItem gameItem) {
    this.gameItem = gameItem;
  }

  public Integer getCount() {
    return this.count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }
}