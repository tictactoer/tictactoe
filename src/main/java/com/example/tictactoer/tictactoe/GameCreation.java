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
public class GameCreation {
	String name;
	String character;

	boolean isValid() {
		if (getCharacter().length() != 1)
			return false;

		char playerCharacter = Character.toLowerCase(getCharacter().charAt(0));
		if (playerCharacter != 'x' && playerCharacter != 'o')
			return false;

		return true;
	}

	char getCharacterAsChar() {
		return Character.toLowerCase(getCharacter().charAt(0));
	}

}
