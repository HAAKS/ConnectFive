package test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import dao.Colour;
import dao.Game;
import dao.Player;
import socket.Connection;

@TestMethodOrder(OrderAnnotation.class)
public class AppTest {
	static String link = Connection.getLink();
	Player play1 = new Player("ABC", Colour.R);
	Player play2 = new Player("XYZ", Colour.Y);

	@Test
	void testConnection() throws MalformedURLException, IOException {
		String query = Connection.getRequest(new URL(link));
		System.out.println(query);
		String result = "Add your first name and choose your color (Red (R) or Yellow (Y)).";
		System.out.println(result);
		Assertions.assertEquals(query, result);
	}

	@Test
	@Order(1)
	void testPlayerOneSaved() {
		try {
			boolean saved = Connection.postDetails(new URL(link));
			Assertions.assertEquals(saved, true);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	@Order(2)
	void testPlayerTwoSaved() {
		try {
			boolean saved = Connection.postDetails(new URL(link));
			Assertions.assertEquals(saved, true);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@Order(3)
	void noThirdPlayer() {
		try {
			boolean saved = Connection.postDetails(new URL(link));
			Assertions.assertEquals(saved, false);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testWinVertically() {
		Game game = getGame();
		game.getBoardRepresentation();
		game.game(1, play1);
		game.game(3, play2);
		game.game(1, play1);
		game.game(2, play2);
		game.game(1, play1);
		game.game(2, play2);
		game.game(1, play1);
		game.game(2, play2);
		String result = game.game(1, play1);
		Assertions.assertEquals(result, "1");
	}

	@Test
	void testWinHorizontally() {
		Game game = getGame();
		game.getBoardRepresentation();
		game.game(1, play1);
		game.game(1, play2);
		game.game(2, play1);
		game.game(2, play2);
		game.game(1, play2);
		game.game(3, play1);
		game.game(3, play2);
		game.game(4, play1);
		game.game(4, play2);
		game.game(5, play1);
		game.game(2, play2);

		String result = game.game(1, play1);
		Assertions.assertEquals(result, "1");
	}

	Game getGame() {
		return new Game();
	}

	@Test
	void testWinForwardDiagonal() {

		Game game = getGame();

		game.game(1, play1);

		game.game(1, play2);

		game.game(2, play2);
		game.game(2, play1);

		game.game(1, play2);
		game.game(3, play1);
		game.game(3, play2);
		game.game(3, play1);

		game.game(3, play2);
		game.game(4, play1);
		game.game(4, play2);
		game.game(4, play1);
		game.game(4, play1);

		game.game(2, play2);
		game.game(5, play1);
		game.game(5, play2);
		game.game(5, play1);
		game.game(5, play2);
		String result = game.game(5, play1);
		Assertions.assertEquals(result, "1");
	}

	@Test
	void testWinBackDiagonal() {

		Game game = getGame();
		game.game(1, play1);
		game.game(1, play2);
		game.game(1, play1);
		game.game(1, play2);
		game.game(1, play1);

		game.game(2, play1);
		game.game(2, play2);
		game.game(2, play1);
		game.game(3, play2);

		game.game(2, play1);

		game.game(3, play2);
		game.game(3, play1);

		game.game(4, play1);
		game.game(4, play1);

		game.game(5, play1);
		String result = game.game(1, play1);
		Assertions.assertEquals(result, "1");
	}

	@Test
	void noWin() {
		boolean enter = false;
		Player play = play1;
		String result = "";
		Game game = getGame();
		for (int i = 0; i <= 9; i++) {
			enter = false;
			for (int j = 0; j <= 6; j++) {
				if (i == 4 && !enter || i == 7 && !enter) {
					play = getAnotherplay(play);
					enter = true;
				}
				result = (game.game(i, play));
				play = getAnotherplay(play);
			}
		}
		Assertions.assertEquals(result, "-1");

	}

	private Player getAnotherplay(Player play) {
		return play.getName().equals(play1.getName()) ? play2 : play1;
	}

	@Test
	void userNoResponseDisconnect() throws MalformedURLException, IOException {
		String url = link + "/finishTurns";
		String result = Connection.signalGameOver();
		Assertions.assertEquals("-1", result);

	}

	@Test
	void userChoosingWrongColumn() {
		Game game = new Game();
		String result = game.game(10, play1);
		String negative = game.game(0, play1);
		Assertions.assertEquals("2", result);
		Assertions.assertEquals("2", negative);

	}

}
