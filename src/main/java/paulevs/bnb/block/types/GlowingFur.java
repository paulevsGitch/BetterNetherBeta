package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum GlowingFur implements BlockEnum {
	CRIMSON_GLOWING_FUR(0, 0, "crimson_glowing_fur", "Crimson Glowing Fur"),
	WARPED_GLOWING_FUR(1, 1, "warped_glowing_fur", "Warped Glowing Fur"),
	POISON_GLOWING_FUR_BOTTOM(2, 2, "poison_glowing_fur_bottom", "Poison Glowing Fur"),
	POISON_GLOWING_FUR_TOP(3, 2, "poison_glowing_fur_top", "Poison Glowing Fur");
	
	private final String localizedName;
	private final String name;
	private final int meta;
	private final int drop;
	
	GlowingFur(int meta, int drop, String name, String localizedName) {
		this.localizedName = localizedName;
		this.name = name;
		this.meta = meta;
		this.drop = drop;
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
		return drop;
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
