package com.jrfom.icelotto.model;

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
@Table(name = "prize_tiers")
public class PrizeTier {
  private static final Logger log = LoggerFactory.getLogger(PrizeTier.class);

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private PrizeItem item1;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private PrizeItem item2;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private PrizeItem item3;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private PrizeItem item4;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private PrizeItem item5;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private PrizeItem item6;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private PrizeItem item7;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private PrizeItem item8;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private PrizeItem item9;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private PrizeItem item10;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "prize_tier")
  private Set<Entry> entries;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "prize_tier")
  private Set<ShuffledTierEntry> shuffledTierEntries;

  @Column
  private Boolean drawn;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "draw_result")
  private PrizeDrawResult prizeDrawResult;

  public PrizeTier() {
    this.drawn = false;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return this.id;
  }

  public PrizeItem getItem1() {
    return this.item1;
  }

  public void setItem1(PrizeItem item1) {
    this.item1 = item1;
  }

  public PrizeItem getItem2() {
    return this.item2;
  }

  public void setItem2(PrizeItem item2) {
    this.item2 = item2;
  }

  public PrizeItem getItem3() {
    return this.item3;
  }

  public void setItem3(PrizeItem item3) {
    this.item3 = item3;
  }

  public PrizeItem getItem4() {
    return this.item4;
  }

  public void setItem4(PrizeItem item4) {
    this.item4 = item4;
  }

  public PrizeItem getItem5() {
    return this.item5;
  }

  public void setItem5(PrizeItem item5) {
    this.item5 = item5;
  }

  public PrizeItem getItem6() {
    return this.item6;
  }

  public void setItem6(PrizeItem item6) {
    this.item6 = item6;
  }

  public PrizeItem getItem7() {
    return this.item7;
  }

  public void setItem7(PrizeItem item7) {
    this.item7 = item7;
  }

  public PrizeItem getItem8() {
    return this.item8;
  }

  public void setItem8(PrizeItem item8) {
    this.item8 = item8;
  }

  public PrizeItem getItem9() {
    return this.item9;
  }

  public void setItem9(PrizeItem item9) {
    this.item9 = item9;
  }

  public PrizeItem getItem10() {
    return this.item10;
  }

  public void setItem10(PrizeItem item10) {
    this.item10 = item10;
  }

  public Set<Entry> getEntries() {
    return this.entries;
  }

  public void setEntries(Set<Entry> entries) {
    this.entries = entries;
  }

  public Set<ShuffledTierEntry> getShuffledTierEntries() {
    return this.shuffledTierEntries;
  }

  public void setShuffledTierEntries(Set<ShuffledTierEntry> shuffledTierEntries) {
    this.shuffledTierEntries = shuffledTierEntries;
  }

  public Boolean isDrawn() {
    return (this.drawn != null) ? this.drawn : false;
  }

  public void setDrawn(Boolean drawn) {
    this.drawn = drawn;
  }

  public PrizeDrawResult getPrizeDrawResult() {
    return this.prizeDrawResult;
  }

  public void setPrizeDrawResult(PrizeDrawResult prizeDrawResult) {
    this.prizeDrawResult = prizeDrawResult;
  }

  /**
   * <p>Updates the record by setting the item identifiers to those in the
   * {@code prizeItems} list. This is done by iterating the list, so list element
   * 0 corresponds to {@code item1} and list element 9 to {@code item10}.</p>
   *
   * <p>If you supply a list of 5 elements, only the first 5 items will be
   * updated.</p>
   *
   * <p>If an item is to be removed, then supply {@code null} for the value.</p>
   *
   * @param prizeItems A {@link java.util.List} of
   *                  {@link com.jrfom.icelotto.model.PrizeItem}s. An
   *                  {@link java.util.ArrayList} should be used since it supports
   *                  {@code null} values.
   */
  public void update(List<PrizeItem> prizeItems) {
    for (int i = 0, j = prizeItems.size(); i < j; i += 1) {
      switch (i) {
        case 0:
          this.setItem1(prizeItems.get(i));
          break;
        case 1:
          this.setItem2(prizeItems.get(i));
          break;
        case 2:
          this.setItem3(prizeItems.get(i));
          break;
        case 3:
          this.setItem4(prizeItems.get(i));
          break;
        case 4:
          this.setItem5(prizeItems.get(i));
          break;
        case 5:
          this.setItem6(prizeItems.get(i));
          break;
        case 6:
          this.setItem7(prizeItems.get(i));
          break;
        case 7:
          this.setItem8(prizeItems.get(i));
          break;
        case 8:
          this.setItem9(prizeItems.get(i));
          break;
        case 9:
          this.setItem10(prizeItems.get(i));
          break;
        default:
      }
    }
  }

  @Transient
  public PrizeItem getItemAtPosition(Integer position) {
    PrizeItem result;

    switch (position) {
      case 1:
        result = this.getItem1();
        break;
      case 2:
        result = this.getItem2();
        break;
      case 3:
        result = this.getItem3();
        break;
      case 4:
        result = this.getItem4();
        break;
      case 5:
        result = this.getItem5();
        break;
      case 6:
        result = this.getItem6();
        break;
      case 7:
        result = this.getItem7();
        break;
      case 8:
        result = this.getItem8();
        break;
      case 9:
        result = this.getItem9();
        break;
      case 10:
        result = this.getItem10();
        break;
      default:
        result = null;
    }

    return result;
  }

  @Transient
  public void setItemAtPosition(final Integer position, final PrizeItem item) {
    switch (position) {
      case 1:
        this.setItem1(item);
        break;
      case 2:
        this.setItem2(item);
        break;
      case 3:
        this.setItem3(item);
        break;
      case 4:
        this.setItem4(item);
        break;
      case 5:
        this.setItem5(item);
        break;
      case 6:
        this.setItem6(item);
        break;
      case 7:
        this.setItem7(item);
        break;
      case 8:
        this.setItem8(item);
        break;
      case 9:
        this.setItem9(item);
        break;
      case 10:
        this.setItem10(item);
        break;
      default:
    }
  }

  @Transient
  public void shuffleEntries() {
    log.debug("Shuffling entries for tier {}", this.id);
    if (this.shuffledTierEntries.size() > 0) {
      log.debug("Entries already shuffled");
      return;
    }

    // https://github.com/coolaj86/knuth-shuffle
    Object[] localEntries = this.entries.toArray();
    int currentIndex = localEntries.length;
    double randomIndex;
    Entry temp;

    while (0 != currentIndex) {
      randomIndex = Math.floor(Math.random() * currentIndex);
      currentIndex = currentIndex - 1;

      temp = (Entry) localEntries[currentIndex];
      localEntries[currentIndex] = localEntries[(int) randomIndex];
      localEntries[(int) randomIndex] = temp;
    }

    ShuffledTierEntry[] localShuffled = new ShuffledTierEntry[localEntries.length];
    for (int i = 0, j = localEntries.length; i < j; i += 1) {
      ShuffledTierEntry entry = new ShuffledTierEntry(this, (Entry) localEntries[i]);
      entry.setPosition(i);
      localShuffled[i] = entry;
    }

    this.shuffledTierEntries.clear();
    this.shuffledTierEntries.addAll(ImmutableSet.copyOf(localShuffled));
    log.debug("Shuffled entries: `{}`", Stringer.jsonString(localShuffled));
  }

  @Transient
  public ShuffledTierEntry getShuffledEntryAtPosition(int position) {
    ShuffledTierEntry entry = null;

    for (ShuffledTierEntry tierEntry : this.shuffledTierEntries) {
      if (tierEntry.getPosition().equals(position)) {
        entry = tierEntry;
        break;
      }
    }

    return entry;
  }

  @Transient
  public PrizeDrawResult draw() {
    PrizeDrawResult result = new PrizeDrawResult();
    this.shuffleEntries();

    if (!this.hasPrizes()) {
      // If we continue under this case, then an infinite look will happen
      return result;
    }

    // Pick the winner
    int randomDrawIndex = Crypto.randomInt(this.shuffledTierEntries.size()) - 1;
    ShuffledTierEntry shuffledEntry = this.getShuffledEntryAtPosition(randomDrawIndex);
    result.setUserDrawNumber(randomDrawIndex);
    result.setUser(shuffledEntry.getEntry().getUser());

    // Pick the prize
    int itemDrawNumber;
    do {
      itemDrawNumber = Crypto.randomInt(10);
    } while (this.getItemAtPosition(itemDrawNumber) == null);
    result.setItemDrawNumber(itemDrawNumber);

    // Update remaining properties
    result.setAwarded(Instant.now());
    result.setPrizeTier(this);
    result.setPrizeItem(this.getItemAtPosition(itemDrawNumber));
    this.drawn = true;
    this.prizeDrawResult = result;

    return result;
  }

  @Transient
  public boolean hasPrizes() {
    boolean result = false;

    for (int i = 1; i < 11; i += 1) {
      if (this.getItemAtPosition(i) != null) {
        result = true;
        break;
      }
    }

    return result;
  }

  @Override
  public String toString() {
    return Stringer.jsonString(this);
  }
}