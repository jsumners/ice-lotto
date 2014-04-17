package com.jrfom.icelotto.model;

import javax.persistence.*;

import com.jrfom.icelotto.util.Stringer;

@Entity
@Table(name = "characters")
public class Character {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String name;

  public Character() {}

  public Character(String name) {
    this.name = name;
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

  @Override
  public String toString() {
    return Stringer.jsonString(this);
  }
}