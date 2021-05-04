package dao;

import org.json.JSONObject;

public class Player {
	private String name;
	private Colour color;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Colour getColor() {
		return color;
	}

	public void setColor(Colour color) {
		this.color = color;
	}

	public Player(String name, Colour color) {
		this.name = name;
		this.color = color;
	}

	public JSONObject getJSONPlayer() {
		JSONObject obj = new JSONObject();
		obj.put("name", name);
		obj.put("color", color);
		return obj;
	}
}