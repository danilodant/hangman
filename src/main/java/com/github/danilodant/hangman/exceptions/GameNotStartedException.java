package com.github.danilodant.hangman.exceptions;

public class GameNotStartedException extends IllegalStateException {

  public GameNotStartedException(){
    super("You need to start the game before guessing.");
  }
  
}
