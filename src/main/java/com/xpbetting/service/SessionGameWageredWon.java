package com.xpbetting.service;

import com.xpbetting.domain.dao.GameEventRepo;
import com.xpbetting.domain.dto.SumAggregationDTO;
import com.xpbetting.domain.dto.response.SessionGameWageredWonResDTO;
import com.xpbetting.domain.enums.ChapterType;
import com.xpbetting.domain.enums.SessionGameWageredWonType;
import com.xpbetting.util.CSVUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SessionGameWageredWon {

  final GameEventRepo repository;

  @Autowired
  public SessionGameWageredWon(GameEventRepo repository) {
    this.repository = repository;
  }

  /**
   * calculate sum of amount per game and session for each player and return result as a csv file
   *
   * @param response the HttpServletResponse includes a csv file
   */
  public void calculateSumAmount(HttpServletResponse response) {
    Map<Integer, SessionGameWageredWonResDTO> result = new HashMap<>();
    calculateSumAggregationForWins(result);
    calculateSumAggregationForBets(result);
    generateCSV(response, result);
  }

  /**
   * calculate sum of amount per game and session for each player for WIN chapter
   *
   * @param data the HasMap parameter to save result set
   */
  private void calculateSumAggregationForWins(Map<Integer, SessionGameWageredWonResDTO> data) {
    List<SumAggregationDTO> gameEventSumAggregationWins =
        repository.calculateSumOfAmountByChapter(ChapterType.WIN.getValue());
    gameEventSumAggregationWins.forEach(
        win -> data.put(win.hashCode(), generateSessionGameWageredWonDTO(ChapterType.WIN, win)));
  }

  /**
   * calculate sum of amount per game and session for each player for BET chapter
   *
   * @param data the HasMap parameter to save result set
   */
  private void calculateSumAggregationForBets(Map<Integer, SessionGameWageredWonResDTO> data) {
    List<SumAggregationDTO> gameEventSumAggregationBets =
        repository.calculateSumOfAmountByChapter(ChapterType.BET.getValue());

    gameEventSumAggregationBets.forEach(
        bet -> {
          SessionGameWageredWonResDTO sessionGameWageredWonResDTO = data.get(bet.hashCode());
          if (sessionGameWageredWonResDTO == null)
            data.put(bet.hashCode(), generateSessionGameWageredWonDTO(ChapterType.BET, bet));
          else {
            sessionGameWageredWonResDTO.setAmountBet(bet.getAmount());
            data.put(bet.hashCode(), sessionGameWageredWonResDTO);
          }
        });
  }

  /**
   * generate pojo of SessionGameWageredWonResDTO
   *
   * @param chapter the type of chapter includes (WIN, BET)
   * @param data the data of result set
   * @return SessionGameWageredWonResDTO
   */
  private SessionGameWageredWonResDTO generateSessionGameWageredWonDTO(
      ChapterType chapter, SumAggregationDTO data) {
    SessionGameWageredWonResDTO sessionGameWageredWonResDTO = new SessionGameWageredWonResDTO();
    sessionGameWageredWonResDTO.setSession(data.getSession());
    sessionGameWageredWonResDTO.setGameName(data.getGameName());
    sessionGameWageredWonResDTO.setChapter(data.getChapter());
    sessionGameWageredWonResDTO.setPlayer(data.getPlayer());
    if (chapter.equals(ChapterType.WIN)) sessionGameWageredWonResDTO.setAmountWin(data.getAmount());
    else sessionGameWageredWonResDTO.setAmountBet(data.getAmount());

    return sessionGameWageredWonResDTO;
  }

  /**
   * generate a csv file
   *
   * @param response the response of request
   * @param data the data that is converted to a csv file
   */
  private void generateCSV(
      HttpServletResponse response, Map<Integer, SessionGameWageredWonResDTO> data) {
    response.setContentType("text/csv");
    List<String> header = new ArrayList<>();
    header.add(SessionGameWageredWonType.PLAYER_ID.name());
    header.add(SessionGameWageredWonType.SESSION_ID.name());
    header.add(SessionGameWageredWonType.GAME_NAME.name());
    header.add(SessionGameWageredWonType.AMOUNT_WAGERED.name());
    header.add(SessionGameWageredWonType.AMOUNT_WON.name());
    try (CSVPrinter csvPrinter =
        new CSVPrinter(
            response.getWriter(), CSVFormat.DEFAULT.withHeader(header.toArray(new String[0])))) {
      response.setHeader("Content-Disposition", "Inline; filename=session_game_wagered_won.csv ");
      data.forEach(
          (k, v) -> {
            try {
              csvPrinter.printRecord(
                  v.getPlayer(),
                  v.getSession(),
                  v.getGameName(),
                  CSVUtil.decimalFormat.format(v.getAmountBet()),
                  CSVUtil.decimalFormat.format(v.getAmountWin()));
            } catch (IOException e) {
              e.printStackTrace();
            }
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
