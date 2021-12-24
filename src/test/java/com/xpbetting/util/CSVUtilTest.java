package com.xpbetting.util;

import com.xpbetting.domain.model.GameEvent;
import com.xpbetting.exception.CSVUtilException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CSVUtilTest {

  @Test
  void convert_CSV_file_to_GameEvent_should_not_be_empty() throws CSVUtilException {
    List<GameEvent> gameEvents =
        CSVUtil.csvToGameEventDTOList(
            this.getClass().getClassLoader().getResourceAsStream("game_event_data_light.csv"));
    Assertions.assertTrue(gameEvents.size() > 0);
  }

  @Test
  void convert_CSV_file_to_GameEvent_should_return_CSVUtilException() {
    Assertions.assertThrows(
        CSVUtilException.class,
        () ->
            CSVUtil.csvToGameEventDTOList(
                this.getClass().getClassLoader().getResourceAsStream("wrong_game_event_data.csv")));
  }
}
