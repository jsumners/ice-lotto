package com.jrfom.icelotto.model;

import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jrfom.icelotto.util.Stringer;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

@Entity
@Table(
  name = "users"/*,
  uniqueConstraints = {
    @UniqueConstraint(columnNames = {
      "gw2display_name"
    })
  }*/
)
@Embeddable
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String email;

  @Column
  private String displayName;

  @Column(
//    name = "gw2display_name",
    nullable = false
  )
  private String gw2DisplayName;

  @Column
  @JsonIgnore
  private String password;

  /**
   * This should the be value of {@link org.threeten.bp.ZoneId#OLD_IDS_POST_2005}.
   */
  @Column
  private String timeZone;

  /**
   * If set, this will be used to fomat all dates and times that the user
   * will see. It <em>must</em> be a valid format according to
   * {@link org.threeten.bp.format.DateTimeFormatter#ofPattern(String)}. The
   * default pattern that will be used if it isn't set is
   * "yyyy-MM-dd HH:mm Z".
   */
  @Column
  private String datetimeFormat;

  @Column
  private Boolean enabled;

  @Column
  private String claimKey;

  @Column
  private Boolean claimed;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(
    name = "user_characters",
    joinColumns = {
      @JoinColumn(name = "user_id")
    },
    inverseJoinColumns = {
      @JoinColumn(name = "character_id")
    }
  )
  private Set<Character> characters;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "user_roles",
    joinColumns = {
      @JoinColumn(name = "user_id")
    },
    inverseJoinColumns = {
      @JoinColumn(name = "role_id")
    }
  )
  private Set<Role> roles;

  protected User() {}

  public User(String gw2DisplayName) {
    this.gw2DisplayName = gw2DisplayName;
    this.enabled = false;
    this.claimKey = KeyGenerators.string().generateKey();
  }

  public Long getId() {
    return this.id;
  }

  /**
   * For unit testing.
   *
   * @param id A number as an identifier.
   */
  protected void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getDisplayName() {
    return (this.displayName != null) ? this.displayName : this.gw2DisplayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getGw2DisplayName() {
    return this.gw2DisplayName;
  }

  public void setGw2DisplayName(String gw2DisplayName) {
    this.gw2DisplayName = gw2DisplayName;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getTimeZone() {
    return this.timeZone;
  }

  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }

  public String getDatetimeFormat() {
    return this.datetimeFormat;
  }

  public void setDatetimeFormat(String datetimeFormat) {
    this.datetimeFormat = datetimeFormat;
  }

  public Boolean isEnabled() {
    return (this.enabled == null) ? false : this.enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public String getClaimKey() {
    return this.claimKey;
  }

  public void setClaimKey(String claimKey) {
    this.claimKey = claimKey;
  }

  public Boolean isClaimed() {
    return (this.claimed == null) ? false : this.claimed;
  }

  public void setClaimed(Boolean claimed) {
    this.claimed = claimed;
  }

  public Set<Character> getCharacters() {
    return this.characters;
  }

  public Set<Role> getRoles() {
    return this.roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  @Transient
  public void addCharacter(Character character) {
    this.characters.add(character);
  }

  @Transient
  public Character characterWithName(String name) {
    Character result = null;

    for (Character character : this.characters) {
      if (character.getName().equals(name)) {
        result = character;
        break;
      }
    }

    return result;
  }

  /**
   * Determine if the user has any entries in a specified drawing.
   *
   * @param drawing The {@link com.jrfom.icelotto.model.Drawing} in question.
   *
   * @return {@code true} if the user has entries in the drawing, {@code false}
   * otherwise.
   */
  @Transient
  public boolean hasEntriesInDrawing(Drawing drawing) {
    boolean result = false;
    Set<Entry> entries = drawing.getEntries();

    for (Entry entry : entries) {
      if (entry.getUser().getId() == this.id) {
        result = true;
        break;
      }
    }

    return result;
  }

  /**
   * Determine if the user's entries for a specified
   * {@link com.jrfom.icelotto.model.Drawing} put him in the small money pool.
   * If not, and {@link #hasEntriesInDrawing(Drawing)} is {@code true} then the
   * user is in the large money pool.
   *
   * @param drawing An instance of {@link com.jrfom.icelotto.model.Drawing}.
   *
   * @return {@code true} if the user is in the small money pool, {@code false}
   * otherwise. {@code false} does not guarantee the user has any entries.
   */
  @Transient
  public boolean isInSmallPoolForDrawing(Drawing drawing) {
    // TODO: consider using a custom SQL query instead to offload processing
    // to the native db driver

    Integer total = 0;
    Set<Entry> entries = drawing.getEntries();

    for (Entry entry : entries) {
      if (entry.getUser().getId() == this.id) {
        total += entry.getAmount();
      }
    }

    return total < 21;
  }

  /**
   * Determine if the user's entries for a specified
   * {@link com.jrfom.icelotto.model.Drawing} put him in the large money pool.
   *
   * @param drawing An instance of {@link com.jrfom.icelotto.model.Drawing}.
   *
   * @return {@code true} if the user is in the large money pool, {@code false}
   * otherwise. {@code false} does not guarantee the user has any entries.
   */
  @Transient
  public boolean isInLargePoolForDrawing(Drawing drawing) {
    return (this.hasEntriesInDrawing(drawing) && !this.isInSmallPoolForDrawing(drawing));
  }

  /**
   * Formats an {@link org.threeten.bp.Instant} to the user's desired date
   * time time format. If the format does not include both date <em>and</em>
   * time parameters, then this method will fail.
   *
   * @param dateTime The {@link org.threeten.bp.Instant} to format.
   *
   * @return A formatted date and time string.
   */
  @Transient
  public String localizeDatetime(Instant dateTime) {
    ZoneId zoneId = (this.timeZone != null) ? ZoneId.of(this.timeZone) : ZoneId.of("UTC");
    String format = (this.datetimeFormat != null && this.datetimeFormat.length() > 0) ?
      this.datetimeFormat : "yyyy-MM-dd HH:mm Z";

    try {
      DateTimeFormatter.ofPattern(format);
    } catch (IllegalArgumentException e) {
      format = "yyyy-MM-dd HH:mm Z";
    }

    return OffsetDateTime.ofInstant(dateTime, zoneId).toString(DateTimeFormatter.ofPattern(format));
  }

  @Override
  public String toString() {
    return Stringer.jsonString(this);
  }
}