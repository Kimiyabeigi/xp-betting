package com.xpbetting.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfitablePlayerResDTO {
  private Integer player;
  private Double amountWin = 0d;
  private Double amountBet = 0d;
  private Double subtractAmount = 0d;
}
