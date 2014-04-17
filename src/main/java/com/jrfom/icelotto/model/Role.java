package com.jrfom.icelotto.model;

import java.util.Set;

import javax.persistence.*;

import com.jrfom.icelotto.util.Stringer;

@Entity
@Table(name = "roles")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String name;

  @Column
  private String description;

  @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
  private Set<User> users;

  public Role() {}

  public Role(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return this.id;
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

  public Set<User> getUsers() {
    return this.users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }

  @Override
  public String toString() {
    return Stringer.jsonString(this);
  }
}