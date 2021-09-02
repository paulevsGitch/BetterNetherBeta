package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum NetherPlanksType implements BlockEnum {
	CRIMSON_PLANKS(0, "crimson_planks"),
	WARPED_PLANKS(1, "warped_planks"),
	POISON_PLANKS(2, "poison_planks");
	
	private final String name;
	private final int meta;
	
	NetherPlanksType(int meta, String name) {
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
