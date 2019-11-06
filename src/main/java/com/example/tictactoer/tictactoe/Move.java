package com.example.tictactoer.tictactoe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Class encoding the request body of a game creation request
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Move {
	char row;
	char column;

	boolean isValid() {
		char r = Character.toLowerCase(row);
		if ((r < 'a') && (r > 'c'))
			return false;

		char c = Character.toLowerCase(column);
		if ((c < 'a') && (c > 'c'))
			return false;

		return true;
	}

	int getRowAsInt() {
		return Character.toLowerCase(row) - 'a';
	}

	int getColumnAsInt() {
		return Character.toLowerCase(column) - 'a';
	}

}
