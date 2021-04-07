package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum NetherVines implements BlockEnum {
	// Crimson plants
	CRIMSON_VINE_BOTTOM(0, "crimson_vine_bottom", "crimson_vine"),
	CRIMSON_VINE_TOP(1, "crimson_vine", "crimson_vine"),
	WARPED_VINE_BOTTOM(2, "warped_vine_bottom", "warped_vine"),
	WARPED_VINE_TOP(3, "warped_vine", "warped_vine");
	
	private final String name;
	private final String key;
	private final int meta;
	
	NetherVines(int meta, String name, String key) {
		this.name = name;
		this.meta = meta;
		this.key = key;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTranslationKey() {
		return key;
	}

	@Override
	public String getTexture(int side) {
		return name;
	}

	@Override
	public int getDropMeta() {
		return meta & 0b11111110;
	}
	
	@Override
	public int getMeta() {
		return meta;
	}
	
	@Override
	public boolean isInCreative() {
		return name.contains("bottom");
	}
}
