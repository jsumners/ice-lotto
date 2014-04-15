package com.jrfom.icelotto.model;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jrfom.icelotto.jpa.converters.InstantConverter;
import com.jrfom.icelotto.serializers.MoneyDrawResultSerializer;
import org.threeten.bp.Instant;

/**
 * <p>Note, the JSON serialization of this class is represented like so:</p>
 *
 * {@code
 * {
 *   "drawingId": 0,
 *   "poolId": 0,
 *   "awarded": "date string",
 *   "drawNumber": 0,
 *   "amountWon": 0,
 *   "userId": 0,
 *   "userDisplayName": "string",
 * }
 * }
 */
@Entity
@Table(
  name = "money_draw_results"
)
@JsonSerialize(using = MoneyDrawResultSerializer.class)
public class MoneyDrawResult {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  @Convert(converter = InstantConverter.class)
  private Instant awarded;

  @OneToOne
  @JoinColumn(referencedColumnName = "id")
  private Drawing drawing;

  @OneToOne
  @JoinColumn(referencedColumnName = "id")
  private PrizePool prizePool;

  @Column
  private Integer drawNumber;

  @Column
  private Integer amountWon;

  @OneToOne
  private User user;

  public MoneyDrawResult() {
    this.awarded = Instant.now();
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return this.id;
  }

  public Instant getAwarded() {
    return this.awarded;
  }

  public void setAwarded(Instant awarded) {
    this.awarded = awarded;
  }

  public Drawing getDrawing() {
    return this.drawing;
  }

  public void setDrawing(Drawing drawing) {
    this.drawing = drawing;
  }

  public PrizePool getPrizePool() {
    return this.prizePool;
  }

  public void setPrizePool(PrizePool prizePool) {
    this.prizePool = prizePool;
  }

  public Integer getDrawNumber() {
    return this.drawNumber;
  }

  public void setDrawNumber(Integer drawNumber) {
    this.drawNumber = drawNumber;
  }

  public Integer getAmountWon() {
    return this.amountWon;
  }

  public void setAmountWon(Integer amountWon) {
    this.amountWon = amountWon;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}