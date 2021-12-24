package com.xpbetting.service;

import com.xpbetting.domain.dao.GameEventRepo;
import com.xpbetting.domain.model.GameEvent;
import com.xpbetting.exception.CSVUtilException;
import com.xpbetting.util.CSVUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class GameEventService {
  final GameEventRepo repository;

  @Autowired
  public GameEventService(GameEventRepo repository) {
    this.repository = repository;
  }

  /**
   * insert data from file to database
   *
   * @param file the csv file of clients
   */
  public void initiateDatabase(InputStream file) throws CSVUtilException {
    List<GameEvent> gameEvents = CSVUtil.csvToGameEventDTOList(file);
    repository.saveAll(gameEvents);
  }
}
