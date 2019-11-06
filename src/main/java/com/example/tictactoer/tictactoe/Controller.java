package com.example.tictactoer.tictactoe;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * API Controller for the tic-tac-toe server
 */
@org.springframework.stereotype.Controller
@RequestMapping("/game")
public class Controller {

	@Autowired
	private GameRepo gameRepo;

	// Shared instance across all games, so a player needs to be stateless regarding game state
	@Autowired
	private Player player;

	@PostMapping(value = "", produces = "text/plain")
	public @ResponseBody String createGame(@RequestBody GameCreation creation) {
		if (!creation.isValid())
			throw new InvalidPlayerCharacterException();

		Game game = new Game(creation.getCharacterAsChar());
		gameRepo.save(game);

		return Long.toString(game.getId());
	}

	@GetMapping(value = "/{game-id}", produces = "text/plain")
	public @ResponseBody String getGame(@PathVariable("game-id") long gameId) {
		Game game = gameRepo.findById(gameId);
		if (game == null)
			throw new GameNotFoundException();

		return renderGame(game);
	}

	@PostMapping(value = "/{game-id}/move", produces = "text/plain")
	public @ResponseBody void movePair(@PathVariable("game-id") long gameId, @RequestBody Move move) {
		if (!move.isValid())
			throw new InvalidMoveException();

		Game game = gameRepo.findById(gameId);
		if (game == null)
			throw new GameNotFoundException();

		if (game.getStatus() != Game.Status.ONGOING)
			throw new IllegalMoveException();

		if (game.getPosition(move.getRowAsInt(), move.getColumnAsInt()) != Game.EMPTY)
			throw new IllegalMoveException();

		game.move(move.getRowAsInt(), move.getColumnAsInt(), game.humanChar);

		if (game.getStatus() == Game.Status.ONGOING)
			player.move(game);

		// Note that persisting only after the pair of moves has been completed
		gameRepo.save(game);
	}

	static final Map<Game.Status, List<String>> statusLines = Map.of(Game.Status.COMPUTER_WIN,
			Arrays.asList("", "", "\tComputer won!"), Game.Status.HUMAN_WIN, Arrays.asList("", "", "\tHuman won!"),
			Game.Status.DRAW, Arrays.asList("", "", "\tDraw!"), Game.Status.ONGOING,
			Arrays.asList("", "\tgame ongoing", "\thuman's turn"));

	/*
	 * Renders the board and status according to the examples. Any more complicated
	 * views would need something dedicated or at least a templating engine
	 */
	static String renderGame(Game game) {
		Game.Status status = game.getStatus();
		StringBuilder sb = new StringBuilder("   A B C\n");

		for (int row = 0; row < Game.ROWS; row++) {
			char[] rowState = game.getRow(row);
			sb.append((char) ('A' + row)).append(" |");

			for (int col = 0; col < Game.COLUMNS; col++)
				sb.append(rowState[col]).append('|');
			sb.append(statusLines.get(status).get(row)).append("\n");
		}

		return sb.toString();
	}

	@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid move")
	public class InvalidMoveException extends RuntimeException {
		private static final long serialVersionUID = 8496265263200146209L;
	}

	@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Move not legal in current state of the game")
	public class IllegalMoveException extends RuntimeException {
		private static final long serialVersionUID = 5639396090021771786L;
	}

	@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Game not found")
	public class GameNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 2557587728017052920L;
	}

	@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid player character")
	public class InvalidPlayerCharacterException extends RuntimeException {
		private static final long serialVersionUID = 5113026805575043139L;
	}

}
