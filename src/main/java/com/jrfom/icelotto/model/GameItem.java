package com.jrfom.icelotto.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;
import com.jrfom.icelotto.util.Stringer;

/**
 * Represents an in-game item. It only stores the attributes we care about.
 * All others can be retrieved via the GW2 API.
 */
@Entity
@Table(name = "game_items")
public class GameItem {
  @Id
  @Column(nullable = false)
  @NotNull
  private Long id;

  @Column
  private String name;

  @Column
  private String description;

  @Column
  private Integer minLevel;

  @Enumerated(EnumType.ORDINAL)
  private ItemRarity rarity;

  @Column
  private String imageUrl;

  public GameItem() {}

  /**
   * Create an instance and set all properties. Note that {@code id} cannot be
   * {@code null}. If {@code id} is {@code null} then an
   * {@link java.lang.NullPointerException} will be thrown.
   *
   * @param id An identifier for the item.
   * @param name The name of the item.
   * @param description A decription of the item.
   *
   * @throws java.lang.NullPointerException
   */
  public GameItem(Long id, String name, String description) {
    Preconditions.checkNotNull(id, "Item id cannot be null");

    this.id = id;
    this.name = name;
    this.description = description;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getMinLevel() {
    return this.minLevel;
  }

  public void setMinLevel(Integer minLevel) {
    this.minLevel = minLevel;
  }

  public ItemRarity getRarity() {
    return this.rarity;
  }

  public void setRarity(ItemRarity rarity) {
    this.rarity = rarity;
  }

  public String getImageUrl() {
    return this.imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public void update(String name, String description) {
    this.name = name;
    this.description = description;
  }

  @Override
  public String toString() {
    return Stringer.jsonString(this);
  }
}