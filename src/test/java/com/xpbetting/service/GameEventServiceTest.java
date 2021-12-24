package com.xpbetting.service;

import com.xpbetting.XpBettingApplication;
import com.xpbetting.domain.dao.GameEventRepo;
import com.xpbetting.exception.CSVUtilException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = XpBettingApplication.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GameEventServiceTest {

  @Autowired private GameEventService gameEventService;
  @Autowired private GameEventRepo repository;

  @Test
  void initiate_database_should_return_true() throws CSVUtilException {
    repository.deleteAll();
    gameEventService.initiateDatabase(
        this.getClass().getClassLoader().getResourceAsStream("game_event_data_light.csv"));

    Assertions.assertAll(
        () -> assertTrue(repository.count() > 0), () -> assertEquals(23, repository.count()));
  }
}
