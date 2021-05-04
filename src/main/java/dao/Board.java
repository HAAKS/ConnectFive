package dao;

import java.util.Arrays;

public class Board {
	private static char[][] grid;

	public char[][] getGrid() {
		return grid;
	}

	public String boardToString() {
		StringBuilder result = new StringBuilder();
		for (int i = grid.length - 1; i >= 0; i--) {
			for (int j = 0; j < grid[i].length; j++) {
				result = result.append((grid[i][j] + " "));
			}
			result.append("\n");
		}
		return result.toString();
	}

	private int lastCol = -1;

	public int getLastCol() {

		return lastCol;
	}

	public void setLastCol(int lastCol) {
		this.lastCol = lastCol;
	}

	public int getLastRow() {
		return lastRow;
	}

	public void setLastRow(int lastRow) {
		this.lastRow = lastRow;
	}

	private int lastRow = -1;
	private int row;

	public int getrow() {
		return row;
	}

	public int getcolumn() {
		return column;
	}

	private int column;

	public Board(int row, int column) {
		this.row = row;
		this.column = column;
		grid = new char[row][column];
		// init the grid with blank cell
		for (int i = 0; i < row; i++) {
			Arrays.fill(grid[i] = new char[column], '.');
		}
	}

	private boolean checkHorizontalWin(Colour color) {
		int collect = 5;
		for (int i = 0; i < grid[lastRow].length; i++) {
			if (grid[lastRow][i] == color.toString().charAt(0)) {
				collect--;
				if (collect == 0)
					return true;
			} else {
				collect = 5;
			}
		}
		return false;
	}

	private boolean checkVerticalWin(Colour color) {
		int collect = 5;
		for (int i = grid.length - 1; i >= 0; i--) {
			if (grid[i][lastCol] == color.toString().charAt(0)) {
				collect--;
				if (collect == 0)
					return true;
			} else {
				collect = 5;
			}
		}
		return false;
	}

	private boolean checkForwardDiagonalWin(Colour color) { // Diagonal /
		int rowBlock = lastRow;
		int columnBlock = lastCol;
		int collect = 5;
		while (rowBlock >= 0 && rowBlock < row && columnBlock >= 0 && columnBlock < column) {
			if (grid[rowBlock][columnBlock] == color.toString().charAt(0)) {
				rowBlock--;
				columnBlock++;
				collect--;
				if (collect == 0)
					return true;
			} else {
				break;
			}
		}
		rowBlock = lastRow + 1;
		columnBlock = lastCol - 1;
		while (rowBlock >= 0 && rowBlock < row && columnBlock >= 0 && columnBlock < column) {
			if (grid[rowBlock][columnBlock] == color.toString().charAt(0)) {
				rowBlock++;
				columnBlock--;
				collect--;
				if (collect == 0)
					return true;
			} else {
				break;
			}
		}
		return false;
	}

	private boolean checkBackDiagonalWin(Colour color) { // Diagonal \
		int rowBlock = lastRow;
		int columnBlock = lastCol;
		int collect = 5;
		while (rowBlock >= 0 && rowBlock < row && columnBlock >= 0 && columnBlock < column) {
			if (grid[rowBlock][columnBlock] == color.toString().charAt(0)) {
				rowBlock++;
				columnBlock++;
				collect--;
				if (collect == 0)
					return true;
			} else {
				break;
			}
		}
		rowBlock = --lastRow;
		columnBlock = --lastCol;
		while (rowBlock >= 0 && rowBlock < row && columnBlock >= 0 && columnBlock < column) {
			if (grid[rowBlock][columnBlock] == color.toString().charAt(0)) {
				rowBlock--;
				columnBlock--;
				collect--;
				if (collect == 0)
					return true;
			} else {
				break;
			}
		}
		return false;
	}

	public boolean markGamePlayedValid(int drop, Colour color) {
		System.out.println("markGamePlayedValid " + drop + " color " + color);
		drop--;
		if (drop < column && drop >= 0) {
			this.lastCol = drop;
			System.out.println(grid.length);
			for (int i = 0; i < grid.length; i++) {
				if (grid[i][drop] == '.') {
					grid[i][drop] = color.toString().charAt(0);
					System.out.println("grid at this location :" + grid[i][drop]);
					this.lastRow = i;
					return true;
				}
			}
		}
		return false;
	}

	public boolean isWin(Colour color) {
		return checkHorizontalWin(color) || checkVerticalWin(color) || checkForwardDiagonalWin(color)
				|| checkBackDiagonalWin(color);
	}
}
