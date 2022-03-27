package com.github.danilodant.hangman.exceptions;

public class RepeatedLetterNotAllowedException extends IllegalArgumentException {

  public RepeatedLetterNotAllowedException() {
    super("You have already maked this guess, try another letter. You DO NOT lose your guess!");
  }

}
