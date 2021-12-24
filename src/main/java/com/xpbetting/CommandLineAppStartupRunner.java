package com.xpbetting;

import com.xpbetting.service.GameEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

  final GameEventService gameEventService;

  @Autowired
  public CommandLineAppStartupRunner(GameEventService gameEventService) {
    this.gameEventService = gameEventService;
  }

  @Override
  public void run(String... args) throws Exception {
    gameEventService.initiateDatabase(this.getClass().getResourceAsStream("/game_event_data.csv"));
  }
}
