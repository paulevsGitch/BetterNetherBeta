package paulevs.bnb.util;

import net.minecraft.item.Dye;

public enum MCColors {
	BLACK, RED, GREEN, BROWN, BLUE, PURPLE, CYAN, SILVER, GRAY, PINK, LIME, YELLOW, LIGHTBLUE, MAGENTA, ORANGE, WHITE;
	
	private final String name;
	private final int color;
	private final int id;
	
	MCColors() {
		id = this.ordinal();
		color = Dye.COLOURS[id];
		name = Dye.NAMES[id];
	}
	
	public String getName() {
		return name;
	}
	
	public int getColor() {
		return color;
	}
	
	public int getID() {
		return id;
	}
}
