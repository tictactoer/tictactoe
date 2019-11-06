package com.example.tictactoer.tictactoe;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/*
 * The playing logic for the computer player. Chooses a random empty position.
 */
@Getter
@Setter
@Component
public class Player {
	Random random = new Random();

	void move(Game game) {
		char[] board = game.getBoard();

		List<Integer> frees = IntStream.range(0, board.length).filter(i -> board[i] == Game.EMPTY).boxed()
				.collect(Collectors.toList());

		int chosen = frees.get(random.nextInt(frees.size()));
		game.move(chosen, game.getComputerChar());
	}
}
