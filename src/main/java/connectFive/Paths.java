package connectFive;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.JSONObject;

import dao.Colour;
import dao.Game;
import dao.Player;

@Path("/connect")
@Produces("application/json")
@Consumes({ "application/json" })

public class Paths {
	public static Game game = new Game();
	static int counter = 2;

	@GET
	@Path("startFirstPlayer")
	public String startFirstPlayer() {
		return game.getPlayerTurn().getJSONPlayer().toString();
	}

	@POST
	@Path("markGamePlayed")
	public String markGamePlayed(String map) {
		String checkGameOver = game.isGameOver();
		if (!game.isGameOver().equals("0"))
			return checkGameOver;
		System.out.println(map);
		JSONObject obj = new JSONObject(map);
		int col = obj.getInt("column");
		String name = obj.getString("name");
		Player current = game.getCurrentPlayer();
		Player next = game.getNextPlayer(name);
		String result = game.game(col, current);
		if (result.equals("2"))
			return map;
		if (result == "0")
			return next.getJSONPlayer().toString();
		return result;
	}

	@GET
	@Path("isMyTurn")
	public String isMyTurn() {
		String gameCheck = game.isGameOver();
		System.out.println("is My Turn " + game.getCurrentPlayer().getJSONPlayer().toString());
		return gameCheck.equals("0") || gameCheck.equals("2") ? game.getCurrentPlayer().getJSONPlayer().toString()
				: gameCheck;
	}

	@GET
	@Path("finishTurns")
	public String finishTurns() {
		return game.finishTurns();
	}

	@GET
	public String getWins() {
		System.out.println("HERE IN PATHS");
		return "Add your first name and choose your color (Red (R) or Yellow (Y)).";
	}

	@GET
	@Path("startGame")
	public String startGame() {
		return game.getBoardRepresentation();
	}

	@GET
	@Path("checkPlayers")
	public String checkPlayersOnline() {
		return counter + "";
	}

	@POST
	public String saveInformation(String map) {
		if (counter <= 0) {
			return "Players Limit Reached";
		}
		JSONObject obj = new JSONObject(map);
		String name = obj.getString("name");
		String color = obj.getString("color");
		boolean validDetails = isValidDetails(name, color);
		System.out.println("valid? " + validDetails);
		if (!validDetails) {
			return "Type a name and a color";
		}
		char c = color.charAt(0);
		String response = "";
		if (game.checkPlayer(name, c)) {
			Player play = new Player(name, Colour.valueOf(c));
			game.setPlayer(play);
			counter--;
			System.out.println("counter: " + counter);
			response = "Success";
		} else {
			response = "Choose a unique name and correct color";
		}
		return response;
	}

	private boolean isValidDetails(String name, String color) {
		if (name == null || name.isEmpty() || name.equals("0") || name.equalsIgnoreCase("Success") || name.equals("1")
				|| name.equals("2") || name.equals("-1") || color.isEmpty() || color == null) {

			return false;
		}
		return true;
	}
}
