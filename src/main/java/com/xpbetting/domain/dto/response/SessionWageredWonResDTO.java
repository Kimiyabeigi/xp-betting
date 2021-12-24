package com.xpbetting.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionWageredWonResDTO {
  private Integer player;
  private Integer session;
  private String chapter;
  private Double amountWin = 0d;
  private Double amountBet = 0d;
}
