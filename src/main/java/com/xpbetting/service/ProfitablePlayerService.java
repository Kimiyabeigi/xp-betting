package com.xpbetting.service;

import com.xpbetting.domain.dao.GameEventRepo;
import com.xpbetting.domain.dto.SumAggregationPerPlayerDTO;
import com.xpbetting.domain.dto.response.ProfitablePlayerResDTO;
import com.xpbetting.domain.enums.ChapterType;
import com.xpbetting.domain.enums.ProfitablePlayerType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
public class ProfitablePlayerService {

  final GameEventRepo repository;

  @Autowired
  public ProfitablePlayerService(GameEventRepo repository) {
    this.repository = repository;
  }

  /**
   * calculate sum of amount per player to recognize profitable player and return result as a csv
   * file
   *
   * @param response the HttpServletResponse includes a csv file
   */
  public void calculateSumAmountPerPlayer(HttpServletResponse response) {
    Map<Integer, ProfitablePlayerResDTO> result = new HashMap<>();
    calculateSumAggregationForWins(result);
    calculateSumAggregationForBets(result);
    generateCSV(response, result);
  }

  /**
   * calculate sum of amount per player for WIN chapter
   *
   * @param data the HasMap parameter to save result set
   */
  private void calculateSumAggregationForWins(Map<Integer, ProfitablePlayerResDTO> data) {
    List<SumAggregationPerPlayerDTO> gameEventSumAggregationWins =
        repository.calculateSumOfAmountByChapterPerPlayer(ChapterType.WIN.getValue());
    gameEventSumAggregationWins.forEach(
        win -> data.put(win.hashCode(), generateProfitablePlayerResDTO(ChapterType.WIN, win)));
  }

  /**
   * calculate sum of amount per player for BET chapter
   *
   * @param data the HasMap parameter to save result set
   */
  private void calculateSumAggregationForBets(Map<Integer, ProfitablePlayerResDTO> data) {
    List<SumAggregationPerPlayerDTO> gameEventSumAggregationBets =
        repository.calculateSumOfAmountByChapterPerPlayer(ChapterType.BET.getValue());

    gameEventSumAggregationBets.forEach(
        bet -> {
          ProfitablePlayerResDTO profitablePlayerResDTO = data.get(bet.hashCode());
          if (profitablePlayerResDTO == null)
            data.put(bet.hashCode(), generateProfitablePlayerResDTO(ChapterType.BET, bet));
          else {
            profitablePlayerResDTO.setAmountBet(bet.getAmount());
            profitablePlayerResDTO.setSubtractAmount(
                bet.getAmount() - profitablePlayerResDTO.getSubtractAmount());
            data.put(bet.hashCode(), profitablePlayerResDTO);
          }
        });
  }

  /**
   * generate pojo of ProfitablePlayerResDTO
   *
   * @param chapter the type of chapter includes (WIN, BET)
   * @param data the data of result set
   * @return ProfitablePlayerResDTO
   */
  private ProfitablePlayerResDTO generateProfitablePlayerResDTO(
      ChapterType chapter, SumAggregationPerPlayerDTO data) {
    ProfitablePlayerResDTO profitablePlayerResDTO = new ProfitablePlayerResDTO();
    profitablePlayerResDTO.setPlayer(data.getPlayer());
    profitablePlayerResDTO.setSubtractAmount(data.getAmount());
    if (chapter.equals(ChapterType.WIN)) profitablePlayerResDTO.setAmountWin(data.getAmount());
    else profitablePlayerResDTO.setAmountBet(data.getAmount());

    return profitablePlayerResDTO;
  }

  /**
   * generate a csv file
   *
   * @param response the response of request
   * @param data the data that is converted to a csv file
   */
  private void generateCSV(
      HttpServletResponse response, Map<Integer, ProfitablePlayerResDTO> data) {
    response.setContentType("text/csv");
    List<String> header = new ArrayList<>();
    header.add(ProfitablePlayerType.PLAYER_ID.name());
    try (CSVPrinter csvPrinter =
        new CSVPrinter(
            response.getWriter(), CSVFormat.DEFAULT.withHeader(header.toArray(new String[0])))) {
      response.setHeader("Content-Disposition", "Inline; filename=profitable_players.csv");
      List<ProfitablePlayerResDTO> profitablePlayerResDTOList = new ArrayList<>(data.values());
      profitablePlayerResDTOList.sort(
          Comparator.comparing(ProfitablePlayerResDTO::getSubtractAmount).reversed());

      profitablePlayerResDTOList.stream()
          .limit(5)
          .forEach(
              player -> {
                try {
                  csvPrinter.printRecord(player.getPlayer());
                } catch (IOException e) {
                  e.printStackTrace();
                }
              });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
