package com.xpbetting.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SumAggregationPerGameDTO {
  private Integer player;
  private String gameName;
  private String chapter;
  private Double amount;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SumAggregationPerGameDTO that = (SumAggregationPerGameDTO) o;
    return Objects.equals(player, that.player)
        && Objects.equals(gameName, that.gameName)
        && Objects.equals(chapter, that.chapter)
        && Objects.equals(amount, that.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(player, gameName);
  }
}
