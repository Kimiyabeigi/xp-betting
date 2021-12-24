package com.xpbetting.service;

import com.xpbetting.domain.dao.GameEventRepo;
import com.xpbetting.domain.dto.SumAggregationPerSessionDTO;
import com.xpbetting.domain.dto.response.SessionWageredWonResDTO;
import com.xpbetting.domain.enums.ChapterType;
import com.xpbetting.domain.enums.SessionWageredWonType;
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
public class SessionWageredWonService {

  final GameEventRepo repository;

  @Autowired
  public SessionWageredWonService(GameEventRepo repository) {
    this.repository = repository;
  }

  /**
   * calculate sum of amount per session for each player and return result as a csv file
   *
   * @param response the HttpServletResponse includes a csv file
   */
  public void calculateSumAmountPerSession(HttpServletResponse response) {
    Map<Integer, SessionWageredWonResDTO> result = new HashMap<>();
    calculateSumAggregationForWins(result);
    calculateSumAggregationForBets(result);
    generateCSV(response, result);
  }

  /**
   * calculate sum of amount per session for each player for WIN chapter
   *
   * @param data the HasMap parameter to save result set
   */
  private void calculateSumAggregationForWins(Map<Integer, SessionWageredWonResDTO> data) {
    List<SumAggregationPerSessionDTO> gameEventSumAggregationWins =
        repository.calculateSumOfAmountByChapterPerSession(ChapterType.WIN.getValue());
    gameEventSumAggregationWins.forEach(
        win -> data.put(win.hashCode(), generateSessionWageredWonDTO(ChapterType.WIN, win)));
  }

  /**
   * calculate sum of amount per session for each player for BET chapter
   *
   * @param data the HasMap parameter to save result set
   */
  private void calculateSumAggregationForBets(Map<Integer, SessionWageredWonResDTO> data) {
    List<SumAggregationPerSessionDTO> gameEventSumAggregationBets =
        repository.calculateSumOfAmountByChapterPerSession(ChapterType.BET.getValue());

    gameEventSumAggregationBets.forEach(
        bet -> {
          SessionWageredWonResDTO sessionWageredWonResDTO = data.get(bet.hashCode());
          if (sessionWageredWonResDTO == null)
            data.put(bet.hashCode(), generateSessionWageredWonDTO(ChapterType.BET, bet));
          else {
            sessionWageredWonResDTO.setAmountBet(bet.getAmount());
            data.put(bet.hashCode(), sessionWageredWonResDTO);
          }
        });
  }

  /**
   * generate pojo of SessionWageredWonResDTO
   *
   * @param chapter the type of chapter includes (WIN, BET)
   * @param data the data of result set
   * @return SessionWageredWonResDTO
   */
  private SessionWageredWonResDTO generateSessionWageredWonDTO(
      ChapterType chapter, SumAggregationPerSessionDTO data) {
    SessionWageredWonResDTO sessionWageredWonResDTO = new SessionWageredWonResDTO();
    sessionWageredWonResDTO.setSession(data.getSession());
    sessionWageredWonResDTO.setChapter(data.getChapter());
    sessionWageredWonResDTO.setPlayer(data.getPlayer());
    if (chapter.equals(ChapterType.WIN)) sessionWageredWonResDTO.setAmountWin(data.getAmount());
    else sessionWageredWonResDTO.setAmountBet(data.getAmount());

    return sessionWageredWonResDTO;
  }

  /**
   * generate a csv file
   *
   * @param response the response of request
   * @param data the data that is converted to a csv file
   */
  private void generateCSV(
      HttpServletResponse response, Map<Integer, SessionWageredWonResDTO> data) {
    response.setContentType("text/csv");
    List<String> header = new ArrayList<>();
    header.add(SessionWageredWonType.PLAYER_ID.name());
    header.add(SessionWageredWonType.SESSION_ID.name());
    header.add(SessionWageredWonType.AMOUNT_WAGERED.name());
    header.add(SessionWageredWonType.AMOUNT_WON.name());
    try (CSVPrinter csvPrinter =
        new CSVPrinter(
            response.getWriter(), CSVFormat.DEFAULT.withHeader(header.toArray(new String[0])))) {
      response.setHeader("Content-Disposition", "Inline; filename=session_wagered_won.csv");
      data.forEach(
          (k, v) -> {
            try {
              csvPrinter.printRecord(
                  v.getPlayer(),
                  v.getSession(),
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
