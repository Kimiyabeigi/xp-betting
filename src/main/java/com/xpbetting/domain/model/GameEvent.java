package com.xpbetting.domain.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "game_event")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GameEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer code;
  private Integer player;
  private Float amount;
  private LocalDateTime eventDate;
  private String chapter;
  private Integer partner;
  private String gameName;
  private Integer session;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    GameEvent gameEvent = (GameEvent) o;
    return id != null && Objects.equals(id, gameEvent.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
