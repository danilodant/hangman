package com.github.danilodant.hangman.rest;

import com.github.danilodant.hangman.services.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/game")
public class GameController {

  @Autowired
  GameService game;

  @GetMapping
  public String gamePage(Model model) {
    model.addAttribute("gameIsRunning", game.isRunning());
    model.addAttribute("lastGameResult", game.getLastGameResult());
    if (game.isRunning()) {
      model.addAttribute("guessed", game.getCorrectLettersGuessed());
      model.addAttribute("missed", game.getMissedLettersGuessed());
      model.addAttribute("missedSize", game.getMissedLettersGuessed().size());
      model.addAttribute("hiddenWord", game.getHiddenWord());
      model.addAttribute("publicWord", game.getPublicWord());
    }
    return "home";
  }

  @GetMapping("/reset")
  public String resetGame(Model model) {
    model.addAttribute("gameIsRunning", game.isRunning());
    game.resetGame();
    return "redirect:/game";
  }

  @GetMapping("/new")
  public String startNewGame() {
    game.startNewGame();
    return "redirect:/game";
  }

  @PostMapping("/guess")
  public String getLetter(@RequestParam Character letter) {
    game.makeGuess(letter);
    return "redirect:/game";
  }

}
