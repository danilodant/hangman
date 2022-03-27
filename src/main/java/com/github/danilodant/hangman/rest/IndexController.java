package com.github.danilodant.hangman.rest;

import com.github.danilodant.hangman.services.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {

  @Autowired
  GameService game;

  @GetMapping
  String startPage(Model model){
    model.addAttribute("gameIsRunning", game.isRunning());
    return game.isRunning() ? "redirect:/game" : "home";
  }
  
}
