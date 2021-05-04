package dao;

import java.util.Objects;

public class Game {
	Board board = new Board(6, 9);
	final String winner = "1";
	final String turnsFinished = "-1";
	final String gameNotValid = "2";
	final String validGame = "0";

	public Board getBoard() {
		return board;
	}

	Player one;
	Player two;
	int turn = 54;
	Player current;
	boolean gameOver = false;

	public Player getOne() {
		return one;
	}

	public void setOne(Player one) {
		this.one = one;
	}

	public Player getTwo() {
		return two;
	}

	public void setPlayer(Player play) {
		if (Objects.isNull(one)) {
			this.one = play;
		} else {
			this.two = play;
		}
	}

	public void setTwo(Player two) {
		this.two = two;
	}

	public Game(Player one, Player two) {
		this.one = one;
		this.two = two;
	}

	public Game() {
	}

	public Player getNextPlayer(String name) {
		current = (one.getName().equals(name)) ? two : one;
		return current;
	}

	public Player getCurrentPlayer() {
		return current;
	}

	public Player getPlayerTurn() {
		current = one;
		return one;
	}

	public String game(int col, Player play) {
		String gameOver = isGameOver();
		if (gameOver.equals(validGame)) {
			if (isPlayValid(col, play)) {
				turn--;
				if (checkisWinner(play)) {
					return winner;
				}
			} else {
				return gameNotValid;// play not valid
			}
		}
		return gameOver;
	}

	public String finishTurns() {
		turn = 0;
		return turnsFinished;
	}

	public String isGameOver() {
		if (turn == 0)
			return turnsFinished;
		if (gameOver)
			return winner;
		return validGame;
	}

	public boolean checkisWinner(Player play) {
		gameOver = checkisWinner(play.getColor());
		return gameOver;
	}

	public boolean isPlayValid(int col, Player play) {
		return board.markGamePlayedValid(col, play.getColor());
	}

	private boolean checkisWinner(Colour color) {
		if (board.getLastCol() == -1 || board.getLastRow() == -1) {
			return false;
		}
		return gameOver = board.isWin(color);
	}

	public boolean checkPlayer(String name, char color) {
		if (!Objects.isNull(one) && (one.getName().equals(name) || one.getColor().equals(Colour.valueOf(color)))) {
			return false;
		}
		if (!Colour.isColorValid(color))
			return false;
		return true;
	}

	public String getBoardRepresentation() {
		return board.boardToString();
	}
}
