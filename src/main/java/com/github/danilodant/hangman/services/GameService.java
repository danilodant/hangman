package com.github.danilodant.hangman.services;

import java.util.ArrayList;
import java.util.List;

import com.github.danilodant.hangman.exceptions.RepeatedLetterNotAllowedException;
import com.github.danilodant.hangman.exceptions.GameAlreadyRunningException;
import com.github.danilodant.hangman.exceptions.GameNotStartedException;
import com.github.danilodant.hangman.exceptions.OnlyLetterAreAcceptedException;
import com.github.danilodant.hangman.repositories.WordsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GameService {

  @Autowired
  WordsRepository wordsRepository;

  private String hiddenWord = null;
  private char[] publicWord = null;
  private List<Character> correctLettersGuessed = null;
  private List<Character> missedLettersGuessed = null;
  private String lastGameStatus = "Never played";

  public boolean isRunning() {
    return hiddenWord != null;
  }

  public void startNewGame() {
    if (isRunning()) {
      throw new GameAlreadyRunningException();
    }
    initializeGame();
  }

  private void initializeGame() {
    this.hiddenWord = wordsRepository.getRandomWord();
    this.publicWord = this.hiddenWord.replaceAll(".", "_").toCharArray();
    this.correctLettersGuessed = new ArrayList<>();
    this.missedLettersGuessed = new ArrayList<>();
  }

  public String getPublicWord() {
    return String.valueOf(publicWord);
  }

  public String getHiddenWord() {
    return hiddenWord;
  }

  public void makeGuess(char letter) {
    if (!isRunning()) {
      throw new GameNotStartedException();
    }
    if (!Character.isLetter(letter)) {
      throw new OnlyLetterAreAcceptedException();
    }
    char upperCasedCharacter = Character.toUpperCase(letter);

    if (this.letterAlreadyAddedAsGuessed(upperCasedCharacter)) {
      throw new RepeatedLetterNotAllowedException();
    }

    if (hiddenWord.contains(String.valueOf(upperCasedCharacter))) {
      this.correctLettersGuessed.add(upperCasedCharacter);
      revealLetter(upperCasedCharacter);
    } else {
      this.missedLettersGuessed.add(upperCasedCharacter);
    }
    verifyIfGameFinishedAndClear();
  }

  private void revealLetter(char upperCasedCharacter) {
    for (int i = 0; i < hiddenWord.length(); i++) {
      if (upperCasedCharacter == hiddenWord.charAt(i)) {
        publicWord[i] = upperCasedCharacter;
      }
    }
  }

  private boolean letterAlreadyAddedAsGuessed(char upperCasedCharacter) {
    return this.correctLettersGuessed.contains(upperCasedCharacter)
        || this.missedLettersGuessed.contains(upperCasedCharacter);
  }

  @Bean
  public List<Character> getCorrectLettersGuessed() {
    return correctLettersGuessed;
  }

  @Bean
  public List<Character> getMissedLettersGuessed() {
    return missedLettersGuessed;
  }

  public void verifyIfGameFinishedAndClear() {
    if (isGameFinished()) {
      setLastGameMessage();
      clearGame();
    }
  }

  private void setLastGameMessage() {
    if (gameFinishedWithWin()) {
      this.lastGameStatus = "Congrats! You win! Guessed word was: " + getPublicWord();
    } else {
      this.lastGameStatus = "Sorry to be the bearer of bad news, but you lose. You were only able to find: "
          + getPublicWord();
    }
  }

  public boolean isGameFinished(){
    return gameFinishedWithWin() || gameFinishedWithLose();
  }

  private void clearGame() {
    this.hiddenWord = null;
    this.publicWord = null;
    this.correctLettersGuessed = null;
    this.missedLettersGuessed = null;
  }

  boolean gameFinishedWithWin() {
    return hiddenWord.contentEquals(String.valueOf(publicWord));
  }

  boolean gameFinishedWithLose() {
    return missedLettersGuessed.size() > 5;
  }

  public String getLastGameResult() {
    return lastGameStatus;
  }

  public void resetGame() {
    this.hiddenWord = null;
    this.publicWord = null;
    this.correctLettersGuessed = null;
    this.missedLettersGuessed = null;
  }

}
