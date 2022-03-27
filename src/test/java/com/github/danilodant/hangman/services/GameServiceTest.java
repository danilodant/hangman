package com.github.danilodant.hangman.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import com.github.danilodant.hangman.exceptions.GameAlreadyRunningException;
import com.github.danilodant.hangman.exceptions.GameNotStartedException;
import com.github.danilodant.hangman.exceptions.OnlyLetterAreAcceptedException;
import com.github.danilodant.hangman.exceptions.RepeatedLetterNotAllowedException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class GameServiceTest {
  
  @Autowired
  GameService game;
  
  @BeforeEach
  void beforeEach(){
    final String TEST_WORD = "TEST";
    ReflectionTestUtils.setField(game, "lastGameStatus", "Never played");
    ReflectionTestUtils.setField(game, "hiddenWord", TEST_WORD);
    ReflectionTestUtils.setField(game, "publicWord", TEST_WORD.replaceAll(".", "_").toCharArray());
    ReflectionTestUtils.setField(game, "correctLettersGuessed", new ArrayList<>());
    ReflectionTestUtils.setField(game, "missedLettersGuessed", new ArrayList<>());
  }
  
  @Test
  void shouldThrowIfGuessBeforGameInitialized() {
    ReflectionTestUtils.setField(game, "hiddenWord", null);
    ReflectionTestUtils.setField(game, "publicWord", null);
    ReflectionTestUtils.setField(game, "correctLettersGuessed", null);
    ReflectionTestUtils.setField(game, "missedLettersGuessed", null);

    Exception exception = assertThrows(GameNotStartedException.class, () -> {
      game.makeGuess('c');
    });
    assertEquals("You need to start the game before guessing.", exception.getMessage());
  }

  @Test
  void shouldNotAllowCreateNewGameIfAlreadyRunning() {
    Exception exception = assertThrows(GameAlreadyRunningException.class, () -> {
      game.startNewGame();
    });
    assertEquals("You need to finish the already running game!", exception.getMessage());
    assertEquals(0, game.getCorrectLettersGuessed().size());
  }

  @Test
  void shouldStartNewGameWithRandomWord(){
    String publicWord = game.getPublicWord();
    assertNotNull(publicWord);
    assertEquals(4, publicWord.length());
  };
  
  @Test
  void shouldMakeAGuessAndAddToGuessedLetters(){
    String publicWord = game.getPublicWord();
    assertEquals('_', publicWord.charAt(1));
    game.makeGuess('e');
    publicWord = game.getPublicWord();
    assertEquals('E', publicWord.charAt(1));
  }
  
  @Test
  void shouldNotAllowGuessAndThrow(){
    Exception exception = assertThrows(OnlyLetterAreAcceptedException.class, () -> {
      game.makeGuess('0');
    });
    assertEquals("Only letters are accepted in this game. You DO NOT lose your guess!", exception.getMessage());
    assertEquals(0, game.getCorrectLettersGuessed().size());
  }
  
  @Test
  void shouldFinishTheGameWhenAllLettersAreGuessed(){
    assertFalse(game.gameFinishedWithWin());
    game.makeGuess('t');
    game.makeGuess('e');
    game.makeGuess('s');
    assertEquals("Congrats! You win! Guessed word was: TEST", game.getLastGameResult());
    assertFalse(game.isRunning());
  }

  @Test
  void shouldGetLastGameStatus() {
    String lastGameStatus = game.getLastGameResult();
    assertEquals("Never played", lastGameStatus);
  }
  
  @Test
  void shouldFinishTheGameWithSixWrongLetters(){
    assertFalse(game.gameFinishedWithLose());
    game.makeGuess('a');
    game.makeGuess('b');
    game.makeGuess('c');
    game.makeGuess('d');
    game.makeGuess('e');
    game.makeGuess('f');
    game.makeGuess('g');
    assertEquals("Sorry to be the bearer of bad news, but you lose. You were only able to find: _E__", game.getLastGameResult());
    assertFalse(game.isRunning());
  }

  @Test
  void shouldNotAllowRepeatedLetterAndThrow() {
    game.makeGuess('a');
    Exception exception = assertThrows(RepeatedLetterNotAllowedException.class, () -> {
      game.makeGuess('a');
    });
    assertEquals("You have already maked this guess, try another letter. You DO NOT lose your guess!", exception.getMessage());
    assertEquals(1, game.getMissedLettersGuessed().size());
  }

}
