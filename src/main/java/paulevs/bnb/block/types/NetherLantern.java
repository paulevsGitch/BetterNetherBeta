package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum NetherLantern implements BlockEnum {
	CRIMSON_LANTERN(0, "crimson_lantern", "Crimson Lantern"),
	WARPED_LANTERN(1, "warped_lantern", "Warped Lantern"),
	POISON_LANTERN(2, "poison_lantern", "Poison Lantern");
	
	private final String localizedName;
	private final String name;
	private final int meta;
	
	NetherLantern(int meta, String name, String localizedName) {
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
