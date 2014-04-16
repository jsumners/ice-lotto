package com.jrfom.icelotto.model;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jrfom.icelotto.jpa.converters.InstantConverter;
import com.jrfom.icelotto.serializers.PrizeDrawResultSerializer;
import org.threeten.bp.Instant;

/**
 * <p>Note, the JSON serialization of this class is represented like so:</p>
 *
 * {@code
 * {
 *   "tierId": 0,
 *   "prizeId": 0,
 *   "userId": 0,
 *   "userDisplayName": "string",
 *   "userDrawNumber": 0,
 *   "itemDrawNumber": 0,
 *   "awarded": "date string"
 * }
 * }
 */
@Entity
@Table(
  name = "prize_draw_results"
)
@JsonSerialize(using = PrizeDrawResultSerializer.class)
public class PrizeDrawResult {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(referencedColumnName = "id")
  private PrizeTier prizeTier;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(referencedColumnName = "id")
  private PrizeItem prizeItem;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(referencedColumnName = "id")
  private User user;

  @Column
  private Integer userDrawNumber;

  @Column
  private Integer itemDrawNumber;

  @Column(columnDefinition = "NUMBER")
  @Convert(converter = InstantConverter.class)
  private Instant awarded;

  public PrizeDrawResult() {}

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return this.id;
  }

  public PrizeTier getPrizeTier() {
    return this.prizeTier;
  }

  public void setPrizeTier(PrizeTier prizeTier) {
    this.prizeTier = prizeTier;
  }

  public PrizeItem getPrizeItem() {
    return this.prizeItem;
  }

  public void setPrizeItem(PrizeItem prizeItem) {
    this.prizeItem = prizeItem;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Integer getUserDrawNumber() {
    return this.userDrawNumber;
  }

  public void setUserDrawNumber(Integer userDrawNumber) {
    this.userDrawNumber = userDrawNumber;
  }

  public Integer getItemDrawNumber() {
    return this.itemDrawNumber;
  }

  public void setItemDrawNumber(Integer drawNumber) {
    this.itemDrawNumber = drawNumber;
  }

  public Instant getAwarded() {
    return this.awarded;
  }

  public void setAwarded(Instant awarded) {
    this.awarded = awarded;
  }
}