package com.example.tictactoer.tictactoe;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
 * Game encapsulates the state of a tic-tac-toe board and enforces the rules to extent that they are about
 * board positions. Game does not enforce players to take turns to play or stop playing after one player 
 * has reached victory for example.
 */
@Getter
@Entity
public class Game {
	public static final char O = 'o';
	public static final char X = 'x';
	public static final char EMPTY = ' ';
	public static final int ROWS = 3;
	public static final int COLUMNS = 3;
	static final char[] EMPTY_BOARD;
	static {
		EMPTY_BOARD = new char[ROWS * COLUMNS];
		Arrays.fill(EMPTY_BOARD, EMPTY);
	}

	public static enum Status {
		HUMAN_WIN, COMPUTER_WIN, DRAW, ONGOING
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	char humanChar;

	char[] board = EMPTY_BOARD.clone();

	// Just for JPA,
	Game() {

	}

	Game(char humanChar) {
		if (humanChar != X && (humanChar != O))
			throw new IllegalArgumentException();
		this.humanChar = humanChar;
	}

	// WinCondition is a winning condition on the board
	// This is a straight line on the board a given by its starting coordinate and
	// the step in the array encoding the gradient of the line
	@AllArgsConstructor
	static class WinCondition {
		int start;
		int step;
		static final int LENGTH = ROWS;

		boolean isWin(char[] board, char player) {
			int cur = start;

			for (int i = 0; i < LENGTH; i++) {
				if (board[cur] != player)
					return false;
				cur += step;
			}

			return true;
		}
	}

	// Definition of all lines of victory
	static final List<WinCondition> winConditions = Arrays.asList(
			// Rows
			new WinCondition(0, 1), new WinCondition(3, 1), new WinCondition(6, 1),
			// Columns
			new WinCondition(0, 3), new WinCondition(1, 3), new WinCondition(2, 3),
			// Diagonals
			new WinCondition(0, 4), new WinCondition(2, 2));

	// Do a move for a position given as a single dimensional index
	void move(int position, char playerChar) {
		if (position < 0 || position >= board.length)
			throw new IndexOutOfBoundsException("Attempt to move on board out of bounds: " + position);
		if (board[position] != EMPTY) {
			throw new IllegalStateException("Attempt to move on a non-empty position on board: " + position);
		}
		board[position] = playerChar;
	}

	// Do a move for a position given as a two dimensional character coordinates
	// starting from A
	void move(int row, int column, char playerChar) {
		move(row * COLUMNS + column, playerChar);
	}

	char[] getRow(int row) {
		return Arrays.copyOfRange(board, row * COLUMNS, row * COLUMNS + COLUMNS);
	}

	char getPosition(int row, int column) {
		return board[row * COLUMNS + column];
	}

	boolean isWin(char player) {
		return winConditions.stream().anyMatch(wc -> wc.isWin(board, player));
	}

	Status getStatus() {
		if (isWin(humanChar))
			return Status.HUMAN_WIN;

		if (isWin(getComputerChar()))
			return Status.COMPUTER_WIN;

		if (isFull())
			return Status.DRAW;

		return Status.ONGOING;
	}

	boolean isFull() {
		return !CharBuffer.wrap(board).chars().anyMatch(pos -> pos == EMPTY);
	}

	char getComputerChar() {
		if (humanChar == X)
			return O;
		else
			return X;
	}
}
