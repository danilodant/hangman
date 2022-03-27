package com.github.danilodant.hangman.rest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SmokeTest {

	@Autowired
	private GameController controller;

	@Test
	public void contextLoads() throws Exception {
		assertNotNull(controller);
	}
  
}
