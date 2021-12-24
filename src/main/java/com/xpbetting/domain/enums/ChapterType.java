package com.xpbetting.domain.enums;

public enum ChapterType {
  WIN("Win"),
  BET("Bet");

  private String value;

  public String getValue() {
    return value;
  }

  ChapterType(String value) {
    this.value = value;
  }
}
