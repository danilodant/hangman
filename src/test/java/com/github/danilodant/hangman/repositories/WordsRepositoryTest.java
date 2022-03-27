package com.github.danilodant.hangman.repositories;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WordsRepositoryTest {

  @Autowired
  private WordsRepository repo;

  @Test
  void shouldLoadDataFromXmlFile() {
    assertNotEquals(repo.getWords().size(), 0);
  }

  @Test
  void shouldGetRandomWord() {
    String randomWord = repo.getRandomWord();
    String randomWord2 = repo.getRandomWord();
    String randomWord3 = repo.getRandomWord();
    String randomWord4 = repo.getRandomWord();
    assertFalse(
        randomWord.equals(randomWord2) &&
        randomWord.equals(randomWord3) &&
        randomWord.equals(randomWord4) &&
        randomWord2.equals(randomWord3) &&
        randomWord2.equals(randomWord4) &&
        randomWord3.equals(randomWord4));
  }

}
