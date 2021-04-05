package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum NetherTreeFur implements BlockEnum {
	CRIMSON_GLOWING_FUR(0, "crimson_fur", "Crimson Glowing Fur"),
	WARPED_GLOWING_FUR(1, "warped_fur", "Warped Glowing Fur"),
	POISON_GLOWING_FUR(2, "poison_fur", "Poison Glowing Fur");
	
	private final String localizedName;
	private final String name;
	private final int meta;
	
	NetherTreeFur(int meta, String name, String localizedName) {
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
		return !name.contains("top");
	}
}
