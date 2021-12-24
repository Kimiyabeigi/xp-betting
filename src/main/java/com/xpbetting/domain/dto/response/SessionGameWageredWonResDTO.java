package com.xpbetting.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionGameWageredWonResDTO {
  private Integer player;
  private Integer session;
  private String gameName;
  private String chapter;
  private Double amountWin = 0d;
  private Double amountBet = 0d;
}
