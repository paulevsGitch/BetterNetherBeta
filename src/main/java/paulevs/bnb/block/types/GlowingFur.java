package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum GlowingFur implements BlockEnum {
	CRIMSON_GLOWING_FUR(0, "crimson_glowing_fur", "Crimson Glowing Fur"),
	WARPED_GLOWING_FUR(1, "warped_glowing_fur", "Warped Glowing Fur");
	
	private final String localizedName;
	private final String name;
	private final int meta;
	
	GlowingFur(int meta, String name, String localizedName) {
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
		return meta;
	}
	
	@Override
	public int getMeta() {
		return meta;
	}
}
