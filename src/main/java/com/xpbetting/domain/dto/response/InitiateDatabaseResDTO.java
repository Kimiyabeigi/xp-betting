package com.xpbetting.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InitiateDatabaseResDTO {
  private String filename;
  private Long totalImportedRows;
}
