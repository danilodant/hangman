package com.github.danilodant.hangman.rest;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.danilodant.hangman.exceptions.GameAlreadyRunningException;
import com.github.danilodant.hangman.exceptions.GameNotStartedException;
import com.github.danilodant.hangman.exceptions.OnlyLetterAreAcceptedException;
import com.github.danilodant.hangman.exceptions.RepeatedLetterNotAllowedException;
import com.github.danilodant.hangman.services.GameService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class GameControllerTest {

  @InjectMocks
  GameController controller;

  @Mock
  GameService mockGame;

  @Autowired
  GameControllerAdvices advices;

  private MockMvc mockMvc;
  private final String BASE_PATH = "/game/";

  @BeforeAll
  void setup() {
    MockitoAnnotations.openMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(advices).build();
  }

  @Test
  public void shouldStartNewGame() throws Exception {
    this.mockMvc.perform(get(BASE_PATH + "new")).andExpect(status().isFound());
  }

  @Test
  public void shouldHandleGameAlreadyRunningException() throws Exception {
    doThrow(new GameAlreadyRunningException())
        .when(mockGame)
        .startNewGame();

    this.mockMvc.perform(get(BASE_PATH + "new")).andExpect(status().isForbidden())
        .andExpect(content().string(containsString("You need to finish the already running game!")));
  }

  @Test
  void shouldAcceptLettersWithTheGameRunning() throws Exception {
    this.mockMvc.perform(post(BASE_PATH + "guess?letter=a")).andExpect(status().isFound());
  }

  @Test
  void shouldNotAcceptNumbers() throws Exception {
    doThrow(new OnlyLetterAreAcceptedException())
        .when(mockGame)
        .makeGuess('0');
    this.mockMvc.perform(post(BASE_PATH + "guess?letter=0")).andExpect(status().isForbidden())
        .andExpect(
            content().string(containsString("Only letters are accepted in this game. You DO NOT lose your guess!")));
  }

  @Test
  void shouldHandleGameNotStartedException() throws Exception {
    doThrow(new GameNotStartedException())
        .when(mockGame)
        .makeGuess('a');
    this.mockMvc.perform(post(BASE_PATH + "guess?letter=a")).andExpect(status().isForbidden())
        .andExpect(content().string(containsString("You need to start the game before guessing.")));
  }

  @Test
  void shouldHandleRepeatedLetterExpection() throws Exception {
    doThrow(new RepeatedLetterNotAllowedException())
        .when(mockGame)
        .makeGuess('e');
    this.mockMvc.perform(post(BASE_PATH + "guess?letter=e")).andExpect(status().isForbidden())
        .andExpect(content().string(
            containsString("You have already maked this guess, try another letter. You DO NOT lose your guess!")));
  }
}
