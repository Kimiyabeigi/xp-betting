package com.xpbetting.service;

import com.xpbetting.XpBettingApplication;
import com.xpbetting.domain.dao.GameEventRepo;
import com.xpbetting.exception.CSVUtilException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@SpringBootTest(classes = XpBettingApplication.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SessionWageredWonServiceTest {

  @Autowired private SessionWageredWonService sessionWageredWonService;
  @Autowired private GameEventService gameEventService;
  @Autowired private GameEventRepo repository;

  @BeforeAll
  void beforeAll() throws CSVUtilException {
    repository.deleteAll();
    gameEventService.initiateDatabase(
        this.getClass().getClassLoader().getResourceAsStream("game_event_data_light.csv"));
  }

  @Test
  void calculate_sum_amount_per_session_should_should_return_true() throws IOException {
    InputStream inputStream =
        this.getClass().getClassLoader().getResourceAsStream("result_sum_amount_session.csv");
    String validResult = readFromInputStream(inputStream).replace("\n", "");

    MockHttpServletResponse response = new MockHttpServletResponse();
    sessionWageredWonService.calculateSumAmountPerSession(response);
    String responseResult = response.getContentAsString().replace("\r\n", "");

    Assertions.assertEquals(validResult, responseResult);
  }

  private String readFromInputStream(InputStream inputStream) throws IOException {
    StringBuilder resultStringBuilder = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      while ((line = br.readLine()) != null) {
        resultStringBuilder.append(line).append("\n");
      }
    }
    return resultStringBuilder.toString();
  }
}
