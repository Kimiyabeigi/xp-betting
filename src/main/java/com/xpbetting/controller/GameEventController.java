package com.xpbetting.controller;

import com.xpbetting.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api")
public class GameEventController {

  final GameEventService gameEventService;
  final SessionWageredWonService sessionWageredWonService;
  final GameWageredWonService gameWageredWonService;
  final SessionGameWageredWon sessionGameWageredWon;
  final ProfitablePlayerService profitablePlayerService;

  @Autowired
  public GameEventController(
      GameEventService gameEventService,
      SessionWageredWonService sessionWageredWonService,
      GameWageredWonService gameWageredWonService,
      SessionGameWageredWon sessionGameWageredWon,
      ProfitablePlayerService profitablePlayerService) {
    this.gameEventService = gameEventService;
    this.sessionWageredWonService = sessionWageredWonService;
    this.gameWageredWonService = gameWageredWonService;
    this.sessionGameWageredWon = sessionGameWageredWon;
    this.profitablePlayerService = profitablePlayerService;
  }

  @GetMapping("session/win-bet")
  public void calculateAmountPerSession(HttpServletResponse response) {
    sessionWageredWonService.calculateSumAmountPerSession(response);
  }

  @GetMapping("game/win-bet")
  public void calculateAmountPerGame(HttpServletResponse response) {
    gameWageredWonService.calculateSumAmountPerGame(response);
  }

  @GetMapping("session-game/win-bet")
  public void calculateAmount(HttpServletResponse response) {
    sessionGameWageredWon.calculateSumAmount(response);
  }

  @GetMapping("profit-player")
  public void calculateProfitablePlayer(HttpServletResponse response) {
    profitablePlayerService.calculateSumAmountPerPlayer(response);
  }
}
