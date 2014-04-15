package com.jrfom.icelotto.model;

import javax.persistence.*;

import com.jrfom.icelotto.jpa.converters.InstantConverter;
import org.threeten.bp.Instant;

@Entity
@Table(name = "entries")
public class Entry {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne
  @JoinColumn(referencedColumnName = "id")
  private User user;

  @Column
  private Integer amount;

  @Column(columnDefinition = "INTEGER")
  @Convert(converter = InstantConverter.class)
  private Instant enteredDate;

  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private Drawing drawing;

  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private PrizeTier prizeTier;

  public Entry() {}

  public Entry(User user, PrizeTier prizeTier, Integer amount) {
    this.user = user;
    this.amount = amount;
    this.enteredDate = Instant.now();
    this.prizeTier = prizeTier;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Integer getAmount() {
    return this.amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public Instant getEnteredDate() {
    return enteredDate;
  }

  public void setEnteredDate(Instant enteredDate) {
    this.enteredDate = enteredDate;
  }

  public Drawing getDrawing() {
    return this.drawing;
  }

  public void setDrawing(Drawing drawing) {
    this.drawing = drawing;
  }

  public PrizeTier getPrizeTier() {
    return this.prizeTier;
  }

  public void setPrizeTier(PrizeTier prizeTier) {
    this.prizeTier = prizeTier;
  }
}