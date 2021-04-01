package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum NetherPlanks implements BlockEnum {
	CRIMSON_PLANKS(0, "crimson_planks", "Crimson Planks"),
	WARPED_PLANKS(1, "warped_planks", "Warped Planks");
	
	private final String localizedName;
	private final String name;
	private final int meta;
	
	NetherPlanks(int meta, String name, String localizedName) {
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
