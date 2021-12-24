package com.xpbetting.domain.dao;

import com.xpbetting.domain.dto.SumAggregationDTO;
import com.xpbetting.domain.dto.SumAggregationPerGameDTO;
import com.xpbetting.domain.dto.SumAggregationPerPlayerDTO;
import com.xpbetting.domain.dto.SumAggregationPerSessionDTO;
import com.xpbetting.domain.model.GameEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameEventRepo extends JpaRepository<GameEvent, Long> {
  @Query(
      value =
          "select new com.xpbetting.domain.dto.SumAggregationPerSessionDTO(player, session, chapter, sum(amount)) "
              + " from game_event "
              + " group by session, player, chapter "
              + " having chapter = ?1")
  List<SumAggregationPerSessionDTO> calculateSumOfAmountByChapterPerSession(String chapter);

  @Query(
      value =
          "select new com.xpbetting.domain.dto.SumAggregationPerGameDTO(player, gameName, chapter, sum(amount)) "
              + " from game_event "
              + " group by gameName, player, chapter "
              + " having chapter = ?1")
  List<SumAggregationPerGameDTO> calculateSumOfAmountByChapterPerGame(String chapter);

  @Query(
      value =
          "select new com.xpbetting.domain.dto.SumAggregationDTO(player, session, gameName, chapter, sum(amount)) "
              + " from game_event "
              + " group by session, gameName, player, chapter "
              + " having chapter = ?1")
  List<SumAggregationDTO> calculateSumOfAmountByChapter(String chapter);

  @Query(
      value =
          "select new com.xpbetting.domain.dto.SumAggregationPerPlayerDTO(player, chapter, sum(amount)) "
              + " from game_event "
              + " group by player, chapter "
              + " having chapter = ?1")
  List<SumAggregationPerPlayerDTO> calculateSumOfAmountByChapterPerPlayer(String chapter);
}
