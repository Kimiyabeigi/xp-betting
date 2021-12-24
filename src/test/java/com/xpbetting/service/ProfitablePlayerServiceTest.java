package com.xpbetting.service;

import com.xpbetting.XpBettingApplication;
import com.xpbetting.domain.dao.GameEventRepo;
import com.xpbetting.exception.CSVUtilException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = XpBettingApplication.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfitablePlayerServiceTest {

  @Autowired ProfitablePlayerService profitablePlayerService;
  @Autowired private GameEventService gameEventService;
  @Autowired private GameEventRepo repository;

  @BeforeAll
  void beforeAll() throws CSVUtilException {
    repository.deleteAll();
    gameEventService.initiateDatabase(
        this.getClass().getClassLoader().getResourceAsStream("game_event_data_light.csv"));
  }

  @Test
  void calculate_profitable_player_should_return_2_rows() throws UnsupportedEncodingException {
    MockHttpServletResponse response = new MockHttpServletResponse();
    profitablePlayerService.calculateSumAmountPerPlayer(response);
    String responseResult = response.getContentAsString();
    Assertions.assertAll(
        () -> assertTrue(responseResult.contains("14345196")),
        () -> assertTrue(responseResult.contains("14345194")),
        () -> assertEquals(3, responseResult.split("\r\n").length));
  }
}
