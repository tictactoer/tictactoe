package com.example.tictactoer.tictactoe;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.example.tictactoer.tictactoe.Controller;
import com.example.tictactoer.tictactoe.Game;
import com.example.tictactoer.tictactoe.GameRepo;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private GameRepo gameRepo;

	@Mock
	private Game game;

	@Test
	public void testCreateGame() throws Exception {
		MockHttpServletRequestBuilder req = post("/game")
				.accept(MediaType.TEXT_PLAIN)
				.content("{\"name\":\"Mati\",\"character\":\"x\"} ")
				.contentType(MediaType.APPLICATION_JSON);

		mvc.perform(req).andExpect(status().isOk());
	}

	@Test
	public void testCreateEmptyPlayerCharacter() throws Exception {
		MockHttpServletRequestBuilder req = post("/game")
				.accept(MediaType.TEXT_PLAIN)
				.content("{\"name\":\"Mati\",\"character\":\"\"} ")
				.contentType(MediaType.APPLICATION_JSON);

		mvc.perform(req).andExpect(status().isBadRequest());
	}

	@Test
	public void testGetGameNotFound() throws Exception {
		when(gameRepo.findById(11)).thenReturn(null);

		MockHttpServletRequestBuilder req = get("/game/11").accept(MediaType.TEXT_PLAIN);

		mvc.perform(req).andExpect(status().isNotFound());
	}

	@Test
	public void testGetGameSuccess() throws Exception {
		when(gameRepo.findById(11)).thenReturn(new Game('x'));

		MockHttpServletRequestBuilder req = get("/game/11").accept(MediaType.TEXT_PLAIN);

		mvc.perform(req).andExpect(status().isOk()).andExpect(content()
				.string("   A B C\n" +
						"A | | | |\n" +
						"B | | | |\tgame ongoing\n" +
						"C | | | |\thuman's turn\n"));
	}

	@Test
	public void testMove() throws Exception {
		Game game = new Game('x');
		when(gameRepo.findById(12)).thenReturn(game);
		MockHttpServletRequestBuilder req = post("/game/12/move")
				.accept(MediaType.TEXT_PLAIN)
				.content("{\"row\": \"C\", \"column\":\"A\"} ")
				.contentType(MediaType.APPLICATION_JSON);

		mvc.perform(req).andExpect(status().isOk());
	}

	@Test
	public void testRenderOngoing() throws Exception {
		when(game.getStatus()).thenReturn(Game.Status.ONGOING);
		when(game.getRow(0)).thenReturn("   ".toCharArray());
		when(game.getRow(1)).thenReturn("oxo".toCharArray());
		when(game.getRow(2)).thenReturn("   ".toCharArray());

		String res = Controller.renderGame(game);
		assertEquals("   A B C\n" +
		             "A | | | |\n" +
		             "B |o|x|o|\tgame ongoing\n" +
				     "C | | | |\thuman's turn\n",
				     res);
	}

	@Test
	public void testRenderHumanWin() throws Exception {
		when(game.getStatus()).thenReturn(Game.Status.HUMAN_WIN);
		when(game.getRow(0)).thenReturn("x  ".toCharArray());
		when(game.getRow(1)).thenReturn("oxo".toCharArray());
		when(game.getRow(2)).thenReturn("  x".toCharArray());

		String res = Controller.renderGame(game);
		assertEquals("   A B C\n" +
					 "A |x| | |\n" +
					 "B |o|x|o|\n" +
					 "C | | |x|\tHuman won!\n",
					 res);
	}

	@Test
	public void testRenderComputerWin() throws Exception {
		when(game.getStatus()).thenReturn(Game.Status.COMPUTER_WIN);
		when(game.getRow(0)).thenReturn("x  ".toCharArray());
		when(game.getRow(1)).thenReturn("ooo".toCharArray());
		when(game.getRow(2)).thenReturn("x x".toCharArray());

		String res = Controller.renderGame(game);
		assertEquals("   A B C\n" +
					 "A |x| | |\n" +
					 "B |o|o|o|\n" +
					 "C |x| |x|\tComputer won!\n",
					 res);
	}

	@Test
	public void testRenderDraw() throws Exception {
		when(game.getStatus()).thenReturn(Game.Status.DRAW);
		when(game.getRow(0)).thenReturn("xoo".toCharArray());
		when(game.getRow(1)).thenReturn("oxx".toCharArray());
		when(game.getRow(2)).thenReturn("xxo".toCharArray());

		String res = Controller.renderGame(game);
		assertEquals("   A B C\n" +
					 "A |x|o|o|\n" +
					 "B |o|x|x|\n" +
					 "C |x|x|o|\tDraw!\n",
					 res);
	}

}
