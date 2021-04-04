package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum NetherFungus implements BlockEnum {
	CRIMSON_FUNGUS(0, "crimson_fungus", "Crimson Fungus"),
	WARPED_FUNGUS(1, "warped_fungus", "Warped Fungus");
	
	private final String localizedName;
	private final String name;
	private final int meta;
	
	NetherFungus(int meta, String name, String localizedName) {
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
