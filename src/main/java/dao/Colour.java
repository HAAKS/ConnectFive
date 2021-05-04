package dao;

public enum Colour {

	R, Y;

	public static Colour valueOf(char c) {
		return c == 'R' || c == 'r' ? Colour.R : Colour.Y;
	}

	public static boolean isColorValid(char c) {
		return c == 'R' || c == 'r' || c == 'y' || c == 'Y' ? true : false;
	}
}

