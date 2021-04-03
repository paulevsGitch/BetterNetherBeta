package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum NetherPlants implements BlockEnum {
	// Crimson plants
	CRIMSON_ROOTS(0, "crimson_roots", "Crimson Roots"),
	LAMELLARIUM(1, "lamellarium", "Lamellarium"),
	LANTERN_GRASS(2, "lantern_grass", "Lantern Grass"),
	CRIMSON_BUSH(3, "crimson_bush", "Crimson Bush"),
	
	// Warped plants
	WARPED_ROOTS(4, "warped_roots", "Warped Roots"),
	GLOWTAIL(5, "glowtail", "Glowtail"),
	WARPED_CORAL(6, "warped_coral", "Warped Coral"),
	WARPED_MOSS(7, "warped_moss", "Warped Moss"),
	
	BUBBLE_GRASS(8, "bubble_grass", "Bubble Grass"),
	LONGWEED(9, "longweed", "Longweed"),
	JELLYSHROOM(10, "jellyshroom", "Jellyshroom"),
	TAILGRASS(11, "tailgrass", "Tailgrass");
	
	private final String localizedName;
	private final String name;
	private final int meta;
	
	NetherPlants(int meta, String name, String localizedName) {
		this.localizedName = localizedName;
		this.name = name;
		this.meta = meta;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getLocalizedName() {
		return localizedName;
	}

	@Override
	public String getTexture(int side) {
		return name;
	}

	@Override
	public int getDropMeta() {
		return meta;
	}
	
	@Override
	public int getMeta() {
		return meta;
	}
	
	@Override
	public boolean isInCreative() {
		return true;
	}
}
