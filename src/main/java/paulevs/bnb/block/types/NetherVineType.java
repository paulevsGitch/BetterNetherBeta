package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum NetherVineType implements BlockEnum {
	CRIMSON_VINE(0, "crimson_vine"),
	WARPED_VINE(1, "warped_vine"),
	VIRID_VINE(2, "virid_vine"),
	DARK_VINE(3, "dark_vine");
	
	private final String name;
	private final int meta;
	
	NetherVineType(int meta, String name) {
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
