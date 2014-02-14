package com.jrfom.icelotto.model;

import javax.persistence.*;

@Entity
@Table(name = "prize_pools")
public class PrizePool {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

//  @Column(name = "tier_1")
  @OneToOne
  @JoinColumn(name = "id")
  private PrizeTier tier1;

//  @Column(name = "tier_2")
  @OneToOne
  @JoinColumn(name = "id")
  private PrizeTier tier2;

//  @Column(name = "tier_3")
  @OneToOne
  @JoinColumn(name = "id")
  private PrizeTier tier3;

//  @Column(name = "tier_4")
  @OneToOne
  @JoinColumn(name = "id")
  private PrizeTier tier4;

//  @Column(name = "tier_5")
  @OneToOne
  @JoinColumn(name = "id")
  private PrizeTier tier5;

//  @Column(name = "tier_6")
  @OneToOne
  @JoinColumn(name = "id")
  private PrizeTier tier6;

//  @Column(name = "tier_7")
  @OneToOne
  @JoinColumn(name = "id")
  private PrizeTier tier7;

//  @Column(name = "tier_8")
  @OneToOne
  @JoinColumn(name = "id")
  private PrizeTier tier8;

//  @Column(name = "tier_9")
  @OneToOne
  @JoinColumn(name = "id")
  private PrizeTier tier9;

//  @Column(name = "tier_10")
  @OneToOne
  @JoinColumn(name = "id")
  private PrizeTier tier10;

  public PrizePool() {}

  public Long getId() {
    return this.id;
  }

  public PrizeTier getTier1() {
    return this.tier1;
  }

  public void setTier1(PrizeTier tier1) {
    this.tier1 = tier1;
  }

  public PrizeTier getTier2() {
    return this.tier2;
  }

  public void setTier2(PrizeTier tier2) {
    this.tier2 = tier2;
  }

  public PrizeTier getTier3() {
    return this.tier3;
  }

  public void setTier3(PrizeTier tier3) {
    this.tier3 = tier3;
  }

  public PrizeTier getTier4() {
    return this.tier4;
  }

  public void setTier4(PrizeTier tier4) {
    this.tier4 = tier4;
  }

  public PrizeTier getTier5() {
    return this.tier5;
  }

  public void setTier5(PrizeTier tier5) {
    this.tier5 = tier5;
  }

  public PrizeTier getTier6() {
    return this.tier6;
  }

  public void setTier6(PrizeTier tier6) {
    this.tier6 = tier6;
  }

  public PrizeTier getTier7() {
    return this.tier7;
  }

  public void setTier7(PrizeTier tier7) {
    this.tier7 = tier7;
  }

  public PrizeTier getTier8() {
    return this.tier8;
  }

  public void setTier8(PrizeTier tier8) {
    this.tier8 = tier8;
  }

  public PrizeTier getTier9() {
    return this.tier9;
  }

  public void setTier9(PrizeTier tier9) {
    this.tier9 = tier9;
  }

  public PrizeTier getTier10() {
    return this.tier10;
  }

  public void setTier10(PrizeTier tier10) {
    this.tier10 = tier10;
  }
}