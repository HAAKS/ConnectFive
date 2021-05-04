package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

public class Connection {
	static String name;
	private static String link = "http://127.0.0.1:8080/ConnectFive/rest/connect";
	static int wait = 0; // wait trials before disconnect, 20 seconds

	public static String getLink() {
		return link;
	}

	public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException {
		startGame();
	}

	static void startGame() throws MalformedURLException, IOException, InterruptedException {
		URL url = new URL(link);
		getRequest(url); // Gets the details needed from the user
		if (!postDetails(url)) { // post the details
			return;
		}
		String checkPlayerStr = new String(link + "/checkPlayers"); // link to check if the two players are connected to
																	// start game
		String result = "";
		while (!result.equals("0")) {
			System.out.println("Waiting for the other player..");
			waitForTurn();
			result = getRequest(new URL(checkPlayerStr));
		}
		System.out.println(name);
		getBoardRepresentation();

		String getPlayerTurnStr = new String(link + "/startFirstPlayer");
		String response = getRequest(new URL(getPlayerTurnStr));
		String markGamePlayedStr = new String(link + "/markGamePlayed");

		getPlayerTurn(response, new URL(markGamePlayedStr));
	}

	private static void getBoardRepresentation() throws MalformedURLException, IOException {
		String getBoardStr = new String(link + "/startGame");
		String str = getRequest(new URL(getBoardStr));
		System.out.println(str);
	}

	public static void getPlayerTurn(String response, URL url) throws IOException, InterruptedException {
		String isMyTurnStr = new String(link + "/isMyTurn");
		String currentPlayer = getPlayerNameFromJSONObject(response);
		String nextPlayer;
		String player = "0";
		int column = 0;
		while (player.equals("0")) {
			getBoardRepresentation();
			if (currentPlayer.equalsIgnoreCase(name)) {
				column = getColumnNumber(currentPlayer);
				String result = createJSONObject(column, currentPlayer);
				nextPlayer = sendPostData(result + "", url);
				System.out.println("Next player " + nextPlayer);
				if (!(nextPlayer.equals("-1") || nextPlayer.equals("1"))) {
					currentPlayer = getPlayerNameFromJSONObject(nextPlayer);
				} else {
					player = nextPlayer;
					printStatement(currentPlayer, nextPlayer);
					return;
				}

				column = 0;
			} else {
				System.out.println("Its " + currentPlayer + "'s turn");
				while (!currentPlayer.equalsIgnoreCase(name) && player.equals("0")) {
					waitForTurn();
					++wait;
					if (wait >= 5) { // wait for 25 seconds before terminating connection
						signalGameOver();
					}
					String turn = getRequest(new URL(isMyTurnStr));
					if (!(turn.equals("-1") || turn.equals("1"))) {
						currentPlayer = getPlayerNameFromJSONObject(turn);
					} else {
						printStatement(currentPlayer, turn);
						return;
					}
					System.out.println("Waiting player: " + currentPlayer);
				}
				wait = 0;
			}
		}
	}

	public static String signalGameOver() throws MalformedURLException, IOException {
		String url = link + "/finishTurns";
		return getRequest(new URL(url));
	}

	private static int getColumnNumber(String currentPlayer) {
		int column = 0;
		while (!(column >= 1 && column <= 9)) {
			Scanner myObj = new Scanner(System.in); // Create a Scanner object
			System.out.println("Its your turn " + currentPlayer + ", enter a column number (1 - 9)");
			try {
				column = Integer.parseInt(myObj.nextLine());
			} catch (NumberFormatException e) {
				column = 0;
			}
		}
		return column;
	}

	private static void waitForTurn() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			System.out.println("Thread of Waiting Closed");
			e.printStackTrace();
		}

	}

	private static String createJSONObject(int column, String currentPlayer) {
		JSONObject result = new JSONObject();
		result.put("column", column);
		result.put("name", currentPlayer);
		return result.toString();
	}

	private static void printStatement(String currentPlayer, String response)
			throws MalformedURLException, IOException {
		getBoardRepresentation();
		switch (response) {
		case "-1":
			System.out.println("Game is Over");
			break;
		case "1":
			System.out.println("Game Over! " + currentPlayer + " won");
			break;

		}

	}

	private static String getPlayerNameFromJSONObject(String response) {
		JSONObject obj = new JSONObject(response);
		String playerName = obj.getString("name");
		return playerName;
	}

	public static boolean postDetails(URL url) throws IOException {
		String str = enterInformation();
		HttpURLConnection postconnection = createPostConnection(url);
		try (OutputStream output = postconnection.getOutputStream()) {
			System.out.println("output" + str);
			output.write(str.getBytes());
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(postconnection.getInputStream(), "UTF-8"));
		String c = in.readLine();
		System.out.println(c);
		if (c.contains("Limit Reached")) {
			return false;
		}
		while (!(c.contains("Success")) && c != null) {
			in.close();
			postconnection.disconnect();
			str = enterInformation();
			c = sendPostData(str, url);
		}
		return true;
	}

	private static String sendPostData(String str, URL url) throws IOException {
		String c = "";
		HttpURLConnection postconnection2 = createPostConnection(url);
		try (OutputStream output = postconnection2.getOutputStream()) {
			output.write(str.getBytes());
			BufferedReader in = new BufferedReader(new InputStreamReader(postconnection2.getInputStream(), "UTF-8"));
			c = in.readLine();
		}
		return c;
	}

	public static String getRequest(URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		InputStream response = connection.getInputStream();
		String responseBody = "";
		try (Scanner scanner = new Scanner(response)) {
			responseBody = scanner.useDelimiter("\\A").next();
		}
		response.close();
		connection.disconnect();
		return responseBody;
	}

	private static HttpURLConnection createPostConnection(URL url) throws IOException {
		HttpURLConnection postconnection = (HttpURLConnection) url.openConnection();
		postconnection.setRequestMethod("POST");
		postconnection.setRequestProperty("Accept", "application/json");
		postconnection.setRequestProperty("Accept-Charset", java.nio.charset.StandardCharsets.UTF_8.name());
		postconnection.setRequestProperty("Connection", "Keep-Alive");
		postconnection.setRequestProperty("Content-Type", "application/json ");
		postconnection.setDoOutput(true); // Triggers POST.
		postconnection.setDoInput(true);
		return postconnection;
	}

	private static String enterInformation() {
		Scanner myObj = new Scanner(System.in); // Create a Scanner object
		JSONObject map = new JSONObject();
		System.out.println("Enter username");
		String color = "";
//		while (myObj.hasNextLine())
		name = myObj.nextLine(); // Read user input
		System.out.println("name is: " + name);
		System.out.println("Enter Color (Red or Yellow)");
//		while (myObj.hasNextLine())
		color = myObj.nextLine(); // Read user input
		System.out.println("color is: " + color);
		map.put("name", name);
		map.put("color", color);
		String str = map.toString();
		return str;
	}

}
