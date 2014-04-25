package com.jrfom.icelotto.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.google.common.collect.ImmutableSet;
import com.jrfom.icelotto.util.Crypto;
import com.jrfom.icelotto.util.Stringer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.Instant;

@Entity
@Table(name = "prize_pools")
public class PrizePool {
  private static final Logger log = LoggerFactory.getLogger(PrizePool.class);

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private PrizeTier tier1;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private PrizeTier tier2;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private PrizeTier tier3;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private PrizeTier tier4;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private PrizeTier tier5;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private PrizeTier tier6;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private PrizeTier tier7;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private PrizeTier tier8;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private PrizeTier tier9;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private PrizeTier tier10;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "prize_pool")
  private Set<ShuffledPoolEntry> shuffledPoolEntries;

  @Column
  private Boolean drawn;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "draw_result")
  private MoneyDrawResult moneyDrawResult;

  public PrizePool() {
    this.tier1 = new PrizeTier();
    this.tier2 = new PrizeTier();
    this.tier3 = new PrizeTier();
    this.tier4 = new PrizeTier();
    this.tier5 = new PrizeTier();
    this.tier6 = new PrizeTier();
    this.tier7 = new PrizeTier();
    this.tier8 = new PrizeTier();
    this.tier9 = new PrizeTier();
    this.tier10 = new PrizeTier();
    this.drawn = false;
  }

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

  public Set<ShuffledPoolEntry> getShuffledPoolEntries() {
    return this.shuffledPoolEntries;
  }

  public void setShuffledPoolEntries(Set<ShuffledPoolEntry> shuffledPoolEntries) {
    this.shuffledPoolEntries = shuffledPoolEntries;
  }

  public Boolean isDrawn() {
    return (this.drawn != null) ? this.drawn : false;
  }

  public void setDrawn(Boolean drawn) {
    this.drawn = drawn;
  }

  public MoneyDrawResult getMoneyDrawResult() {
    return this.moneyDrawResult;
  }

  public void setMoneyDrawResult(MoneyDrawResult moneyDrawResult) {
    this.moneyDrawResult = moneyDrawResult;
  }

  @Transient
  public List<PrizeTier> getPrizeTiers() {
    List<PrizeTier> list = new ArrayList<>(10);
    list.add(this.tier1);
    list.add(this.tier2);
    list.add(this.tier3);
    list.add(this.tier4);
    list.add(this.tier5);
    list.add(this.tier6);
    list.add(this.tier7);
    list.add(this.tier8);
    list.add(this.tier9);
    list.add(this.tier10);

    return list;
  }

  @Transient
  public void setTierAtPosition(final Integer position, final PrizeTier tier) {
    switch (position) {
      case 1:
        this.setTier1(tier);
        break;
      case 2:
        this.setTier2(tier);
        break;
      case 3:
        this.setTier3(tier);
        break;
      case 4:
        this.setTier4(tier);
        break;
      case 5:
        this.setTier5(tier);
        break;
      case 6:
        this.setTier6(tier);
        break;
      case 7:
        this.setTier7(tier);
        break;
      case 8:
        this.setTier8(tier);
        break;
      case 9:
        this.setTier9(tier);
        break;
      case 10:
        this.setTier10(tier);
        break;
      default:
    }
  }

  @Transient
  public List<PrizeDrawResult> getDrawingResults() {
    List<PrizeTier> prizeTiers = this.getPrizeTiers();
    List<PrizeDrawResult> results = new ArrayList<>();

    for (PrizeTier tier : prizeTiers) {
      results.add(tier.getPrizeDrawResult());
    }

    return results;
  }

  @Transient
  public ShuffledPoolEntry getShuffledEntryAtPosition(int position) {
    ShuffledPoolEntry entry = null;

    for (ShuffledPoolEntry poolEntry : this.shuffledPoolEntries) {
      if (poolEntry.getPosition().equals(position)) {
        entry = poolEntry;
        break;
      }
    }

    return entry;
  }

  @Transient
  public MoneyDrawResult draw() {
    MoneyDrawResult result = new MoneyDrawResult();
    this.shuffleEntries();

    // Pick the winner
    int randomDrawIndex = Crypto.randomInt(this.shuffledPoolEntries.size()) - 1;
    ShuffledPoolEntry entry = this.getShuffledEntryAtPosition(randomDrawIndex);
    result.setDrawNumber(randomDrawIndex);
    result.setUser(entry.getEntry().getUser());

    result.setPrizePool(this);
    result.setAwarded(Instant.now());
    this.drawn = true;

    return result;
  }

  @Transient
  protected void shuffleEntries() {
    log.debug("Shuffling entries for pool {}", this.id);
    if (this.shuffledPoolEntries.size() > 0) {
      log.debug("Entries already shuffled");
      return;
    }

    // https://github.com/coolaj86/knuth-shuffle
    List<Entry> entries = this.getEntries();
    // Because we count _each gold_ as an "entry" in the money draws,
    // we need to calculate a total number of entries based on the amount
    // associated with each actual entry. There are more efficient ways to do
    // this, http://programmers.stackexchange.com/questions/150616/return-random-list-item-by-its-weight
    // , but I don't want to redesign a large chunk of this mess.
    int maxEntries = 0;
    for (Entry entry : entries) {
      maxEntries += entry.getAmount();
    }

    int k = 0;
    Entry[] expandedEntries = new Entry[maxEntries];
    for (Entry entry : entries) {
      for (int i = 0, j = entry.getAmount(); i < j; i += 1) {
        expandedEntries[k] = entry;
        k += 1;
      }
    }

    int currentIndex = maxEntries;
    double randomIndex;
    Entry temp;

    while (0 != currentIndex) {
      randomIndex = Math.floor(Math.random() * currentIndex);
      currentIndex = currentIndex - 1;

      temp = expandedEntries[currentIndex];
      expandedEntries[currentIndex] = expandedEntries[(int) randomIndex];
      expandedEntries[(int) randomIndex] = temp;
    }

    ShuffledPoolEntry[] shuffled = new ShuffledPoolEntry[maxEntries];
    for (int i = 0, j = maxEntries; i < j; i += 1) {
      ShuffledPoolEntry entry = new ShuffledPoolEntry(this, expandedEntries[i]);
      entry.setPosition(i);
      shuffled[i] = entry;
    }

    this.shuffledPoolEntries.clear();
    this.shuffledPoolEntries.addAll(ImmutableSet.copyOf(shuffled));
    log.debug("Shuffled entries: `{}`", Stringer.jsonString(shuffled));
  }

  @Transient
  protected List<Entry> getEntries() {
    List<PrizeTier> prizeTiers = this.getPrizeTiers();
    List<Entry> entries = new ArrayList<>(0);

    for (PrizeTier tier : prizeTiers) {
      for (Entry entry : tier.getEntries()) {
        if (entry.getPrizeTierId().equals(tier.getId())) {
          entries.add(entry);
        }
      }
    }

    return entries;
  }

  @Override
  public String toString() {
    return Stringer.jsonString(this);
  }
}