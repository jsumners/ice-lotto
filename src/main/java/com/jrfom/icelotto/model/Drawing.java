package com.jrfom.icelotto.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.jrfom.icelotto.jpa.converters.InstantConverter;
import org.threeten.bp.Instant;

@Entity
@Table(name = "drawings")
public class Drawing {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(columnDefinition = "INTEGER")
  @Convert(converter = InstantConverter.class)
  private Instant scheduled;

  @Column(columnDefinition = "INTEGER")
  @Convert(converter = InstantConverter.class)
  private Instant started;

  @Column(columnDefinition = "INTEGER")
  @Convert(converter = InstantConverter.class)
  private Instant ended;

  @Column
  private Boolean inProgress;

  @Column
  private Boolean duplicated;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(referencedColumnName = "id")
  private PrizePool smallPool;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(referencedColumnName = "id")
  private PrizePool largePool;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "drawing")
  private Set<Entry> entries;

  public Drawing() {
    this.entries = new HashSet<>(0);
    this.inProgress = false;
    this.duplicated = false;
  }

  public Drawing(Instant scheduled, PrizePool smallPool, PrizePool largePool) {
    this.scheduled = scheduled;
    this.inProgress = false;
    this.duplicated = false;
    this.smallPool = smallPool;
    this.largePool = largePool;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Instant getScheduled() {
    return this.scheduled;
  }

  public void setScheduled(Instant scheduled) {
    this.scheduled = scheduled;
  }

  public Instant getStarted() {
    return this.started;
  }

  public void setStarted(Instant started) {
    this.started = started;
  }

  public Instant getEnded() {
    return this.ended;
  }

  public void setEnded(Instant ended) {
    this.ended = ended;
  }

  public Boolean isInProgress() {
    return (this.inProgress == null) ? false : this.inProgress;
  }

  public void setInProgress(Boolean inProgress) {
    this.inProgress = inProgress;
  }

  public Boolean isDuplicated() {
    return (this.duplicated == null) ? false : this.duplicated;
  }

  public void setDuplicated(Boolean duplicated) {
    this.duplicated = duplicated;
  }

  public PrizePool getSmallPool() {
    return this.smallPool;
  }

  public void setSmallPool(PrizePool prizePool) {
    this.smallPool = prizePool;
  }

  public PrizePool getLargePool() {
    return this.largePool;
  }

  public void setLargePool(PrizePool largePool) {
    this.largePool = largePool;
  }

  public Set<Entry> getEntries() {
    return this.entries;
  }

  @Transient
  public Integer getSmallPoolTotal() {
    Integer result = 0;
    List<Entry> countableEntries = new ArrayList<>(0);

    for (Entry entry : this.entries) {
      if (entry.getUser().hasEntriesInDrawing(this) &&
        entry.getUser().isInSmallPoolForDrawing(this))
      {
        countableEntries.add(entry);
      }
    }

    for (Entry entry : countableEntries) {
      result += entry.getAmount();
    }

    return result;
  }

  @Transient
  public Integer getLargePoolTotal() {
    Integer result = 0;
    List<Entry> countableEntries = new ArrayList<>(0);

    for (Entry entry : this.entries) {
      if (entry.getUser().hasEntriesInDrawing(this) &&
        entry.getUser().isInLargePoolForDrawing(this))
      {
        countableEntries.add(entry);
      }
    }

    for (Entry entry : countableEntries) {
      result += entry.getAmount();
    }

    return result;
  }

  @Transient
  public void addEntry(Entry entry) {
    this.entries.add(entry);
  }
}