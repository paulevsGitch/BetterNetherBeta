package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum NetherPlants implements BlockEnum {
	// Crimson plants
	CRIMSON_ROOTS(0, "crimson_roots"),
	LAMELLARIUM(1, "lamellarium"),
	LANTERN_GRASS(2, "lantern_grass"),
	CRIMSON_BUSH(3, "crimson_bush"),
	
	// Warped plants
	WARPED_ROOTS(4, "warped_roots"),
	GLOWTAIL(5, "glowtail"),
	WARPED_CORAL(6, "warped_coral"),
	WARPED_MOSS(7, "warped_moss"),
	
	BUBBLE_GRASS(8, "bubble_grass"),
	LONGWEED(9, "longweed"),
	JELLYSHROOM(10, "jellyshroom"),
	TAILGRASS(11, "tailgrass");
	
	private final String name;
	private final int meta;
	
	NetherPlants(int meta, String name) {
		this.name = name;
		this.meta = meta;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTranslationKey() {
		return name;
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
