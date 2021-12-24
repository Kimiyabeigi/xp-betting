package com.xpbetting.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameEventDTO {
  private Integer code;
  private Integer player;
  private Float amount;
  private LocalDateTime eventDate;
  private String chapter;
  private Integer partner;
  private String gameName;
  private Integer session;
}
