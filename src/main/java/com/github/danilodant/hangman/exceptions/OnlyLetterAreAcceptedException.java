package com.github.danilodant.hangman.exceptions;

public class OnlyLetterAreAcceptedException extends IllegalArgumentException {

  public OnlyLetterAreAcceptedException() {
    super("Only letters are accepted in this game. You DO NOT lose your guess!");
  }

}
