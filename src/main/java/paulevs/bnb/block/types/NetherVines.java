package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum NetherVines implements BlockEnum {
	// Crimson plants
	CRIMSON_VINE_BOTTOM(0, "crimson_vine_bottom", "Crimson Vine"),
	CRIMSON_VINE_TOP(1, "crimson_vine", "Crimson Vine"),
	WARPED_VINE_BOTTOM(2, "warped_vine_bottom", "Warped Vine"),
	WARPED_VINE_TOP(3, "warped_vine", "Warped Vine");
	
	private final String localizedName;
	private final String name;
	private final int meta;
	
	NetherVines(int meta, String name, String localizedName) {
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
	public String getTexture(int side, int meta) {
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
		return true;
	}
}
