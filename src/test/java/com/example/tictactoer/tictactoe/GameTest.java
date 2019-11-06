package com.example.tictactoer.tictactoe;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import com.example.tictactoer.tictactoe.Game;
import com.example.tictactoer.tictactoe.Game.Status;

public class GameTest {

	@Test
	public void testMove1() {
		Game game = new Game(Game.O);

		game.move(0, 1, Game.O);

		assertArrayEquals(" o       ".toCharArray(), game.getBoard());

	}

	@Test
	public void testMoveOutOfBounds() {
		Game game = new Game(Game.O);

		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> game.move(4, 0, Game.O));

	}

	@Test
	public void testHumanWinsDiagonal() {
		Game game = new Game(Game.O);

		assertEquals(Status.ONGOING, game.getStatus());

		game.move(0, 0, Game.O);
		assertEquals(Status.ONGOING, game.getStatus());

		game.move(1, 1, Game.O);
		assertEquals(Status.ONGOING, game.getStatus());

		game.move(2, 2, Game.O);
		assertEquals(Status.HUMAN_WIN, game.getStatus());

		game.move(0, 1, Game.O);
		assertEquals(Status.HUMAN_WIN, game.getStatus());

		game.move(0, 2, Game.O);
		assertEquals(Status.HUMAN_WIN, game.getStatus());

		game.move(1, 0, Game.O);
		assertEquals(Status.HUMAN_WIN, game.getStatus());

		game.move(1, 2, Game.O);
		assertEquals(Status.HUMAN_WIN, game.getStatus());

		game.move(2, 0, Game.O);
		assertEquals(Status.HUMAN_WIN, game.getStatus());

		game.move(2, 1, Game.O);
		assertEquals(Status.HUMAN_WIN, game.getStatus());

	}

	@Test
	public void testComputerWinsBColumn() {
		Game game = new Game(Game.X);

		assertEquals(Status.ONGOING, game.getStatus());

		game.move(0, 1, Game.O);
		assertEquals(Status.ONGOING, game.getStatus());

		game.move(1, 1, Game.O);
		assertEquals(Status.ONGOING, game.getStatus());

		game.move(2, 1, Game.O);
		assertEquals(Status.COMPUTER_WIN, game.getStatus());

		game.move(0, 0, Game.O);
		assertEquals(Status.COMPUTER_WIN, game.getStatus());

		game.move(0, 2, Game.O);
		assertEquals(Status.COMPUTER_WIN, game.getStatus());

		game.move(1, 0, Game.O);
		assertEquals(Status.COMPUTER_WIN, game.getStatus());

		game.move(1, 2, Game.O);
		assertEquals(Status.COMPUTER_WIN, game.getStatus());

		game.move(2, 0, Game.O);
		assertEquals(Status.COMPUTER_WIN, game.getStatus());

		game.move(2, 2, Game.O);
		assertEquals(Status.COMPUTER_WIN, game.getStatus());

	}

	@Test
	public void testDraw() {
		Game game = new Game(Game.X);
		game.move(0, 0, Game.O);
		assertEquals(Status.ONGOING, game.getStatus());

		game.move(0, 1, Game.X);
		assertEquals(Status.ONGOING, game.getStatus());

		game.move(0, 2, Game.X);
		assertEquals(Status.ONGOING, game.getStatus());

		game.move(1, 0, Game.X);
		assertEquals(Status.ONGOING, game.getStatus());

		game.move(1, 1, Game.O);
		assertEquals(Status.ONGOING, game.getStatus());

		game.move(1, 2, Game.O);
		assertEquals(Status.ONGOING, game.getStatus());

		game.move(2, 0, Game.X);
		assertEquals(Status.ONGOING, game.getStatus());

		game.move(2, 1, Game.O);
		assertEquals(Status.ONGOING, game.getStatus());

		game.move(2, 2, Game.X);
		assertEquals(Status.DRAW, game.getStatus());

	}

}
