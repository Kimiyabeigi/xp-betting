package com.xpbetting.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SumAggregationPerSessionDTO {
  private Integer player;
  private Integer session;
  private String chapter;
  private Double amount;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SumAggregationPerSessionDTO that = (SumAggregationPerSessionDTO) o;
    return Objects.equals(player, that.player)
        && Objects.equals(session, that.session)
        && Objects.equals(chapter, that.chapter)
        && Objects.equals(amount, that.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(player, session);
  }
}
