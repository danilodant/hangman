package com.github.danilodant.hangman.exceptions;

public class GameAlreadyRunningException extends IllegalStateException {

  public GameAlreadyRunningException() {
    super("You need to finish the already running game!");
  }
}
