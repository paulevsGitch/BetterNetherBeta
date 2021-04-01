package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum NetherVines implements BlockEnum {
	// Crimson plants
	CRIMSON_VINE(0, "crimson_vine", "Crimson Vine"),
	WARPED_VINE(2, "warped_vine", "Warped Vine");
	
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
		return (meta & 1) == 0 ? name + "_bottom" : name;
	}

	@Override
	public int getDropMeta() {
		return meta;
	}
	
	@Override
	public int getMeta() {
		return meta;
	}
}
