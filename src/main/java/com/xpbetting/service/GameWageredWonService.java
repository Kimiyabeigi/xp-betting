package com.xpbetting.service;

import com.xpbetting.domain.dao.GameEventRepo;
import com.xpbetting.domain.dto.SumAggregationPerGameDTO;
import com.xpbetting.domain.dto.response.GameWageredWonResDTO;
import com.xpbetting.domain.enums.ChapterType;
import com.xpbetting.domain.enums.GameWageredWonType;
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
public class GameWageredWonService {

  final GameEventRepo repository;

  @Autowired
  public GameWageredWonService(GameEventRepo repository) {
    this.repository = repository;
  }

  /**
   * calculate sum of amount per game for each player and return result as a csv file
   *
   * @param response the HttpServletResponse includes a csv file
   */
  public void calculateSumAmountPerGame(HttpServletResponse response) {
    Map<Integer, GameWageredWonResDTO> result = new HashMap<>();
    calculateSumAggregationForWins(result);
    calculateSumAggregationForBets(result);
    generateCSV(response, result);
  }

  /**
   * calculate sum of amount per game for each player for WIN chapter
   *
   * @param data the HasMap parameter to save result set
   */
  private void calculateSumAggregationForWins(Map<Integer, GameWageredWonResDTO> data) {
    List<SumAggregationPerGameDTO> gameEventSumAggregationWins =
        repository.calculateSumOfAmountByChapterPerGame(ChapterType.WIN.getValue());
    gameEventSumAggregationWins.forEach(
        win -> data.put(win.hashCode(), generateGameWageredWonDTO(ChapterType.WIN, win)));
  }

  /**
   * calculate sum of amount per game for each player for BET chapter
   *
   * @param data the HasMap parameter to save result set
   */
  private void calculateSumAggregationForBets(Map<Integer, GameWageredWonResDTO> data) {
    List<SumAggregationPerGameDTO> gameEventSumAggregationBets =
        repository.calculateSumOfAmountByChapterPerGame(ChapterType.BET.getValue());

    gameEventSumAggregationBets.forEach(
        bet -> {
          GameWageredWonResDTO gameWageredWonResDTO = data.get(bet.hashCode());
          if (gameWageredWonResDTO == null)
            data.put(bet.hashCode(), generateGameWageredWonDTO(ChapterType.BET, bet));
          else {
            gameWageredWonResDTO.setAmountBet(bet.getAmount());
            data.put(bet.hashCode(), gameWageredWonResDTO);
          }
        });
  }

  /**
   * generate pojo of generateGameWageredWonDTO
   *
   * @param chapter the type of chapter includes (WIN, BET)
   * @param data the data of result set
   * @return GameWageredWonResDTO
   */
  private GameWageredWonResDTO generateGameWageredWonDTO(
      ChapterType chapter, SumAggregationPerGameDTO data) {
    GameWageredWonResDTO gameWageredWonResDTO = new GameWageredWonResDTO();
    gameWageredWonResDTO.setGameName(data.getGameName());
    gameWageredWonResDTO.setChapter(data.getChapter());
    gameWageredWonResDTO.setPlayer(data.getPlayer());
    if (chapter.equals(ChapterType.WIN)) gameWageredWonResDTO.setAmountWin(data.getAmount());
    else gameWageredWonResDTO.setAmountBet(data.getAmount());

    return gameWageredWonResDTO;
  }

  /**
   * generate a csv file
   *
   * @param response the response of request
   * @param data the data that is converted to a csv file
   */
  private void generateCSV(HttpServletResponse response, Map<Integer, GameWageredWonResDTO> data) {
    response.setContentType("text/csv");
    List<String> header = new ArrayList<>();
    header.add(GameWageredWonType.PLAYER_ID.name());
    header.add(GameWageredWonType.GAME_NAME.name());
    header.add(GameWageredWonType.AMOUNT_WAGERED.name());
    header.add(GameWageredWonType.AMOUNT_WON.name());
    try (CSVPrinter csvPrinter =
        new CSVPrinter(
            response.getWriter(), CSVFormat.DEFAULT.withHeader(header.toArray(new String[0])))) {
      response.setHeader("Content-Disposition", "Inline; filename=game_wagered_won.csv");
      data.forEach(
          (k, v) -> {
            try {
              csvPrinter.printRecord(
                  v.getPlayer(),
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
