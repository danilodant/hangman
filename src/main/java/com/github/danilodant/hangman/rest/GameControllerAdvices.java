package com.github.danilodant.hangman.rest;

import com.github.danilodant.hangman.exceptions.GameAlreadyRunningException;
import com.github.danilodant.hangman.exceptions.GameNotStartedException;
import com.github.danilodant.hangman.exceptions.OnlyLetterAreAcceptedException;
import com.github.danilodant.hangman.exceptions.RepeatedLetterNotAllowedException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GameControllerAdvices {

  @ResponseBody
  @ExceptionHandler(GameAlreadyRunningException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  String gameAlreadyRunningHandler(GameAlreadyRunningException exception){
    return exception.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(GameNotStartedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  String gameNotStartHandler(GameNotStartedException exception){
    return exception.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(OnlyLetterAreAcceptedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  String gameNotStartHandler(OnlyLetterAreAcceptedException exception){
    return exception.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(RepeatedLetterNotAllowedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  String repeatedLetterHandler(RepeatedLetterNotAllowedException exception){
    return exception.getMessage();
  }
  
}
