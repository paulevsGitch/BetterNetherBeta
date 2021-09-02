package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum NetherTreeFurType implements BlockEnum {
	CRIMSON_GLOWING_FUR(0, "crimson_fur", "crimson_tree_fur"),
	WARPED_GLOWING_FUR(1, "warped_fur", "warped_tree_fur"),
	POISON_GLOWING_FUR(2, "poison_fur", "poison_tree_fur");
	
	private final String name;
	private final String key;
	private final int meta;
	
	NetherTreeFurType(int meta, String name, String key) {
		this.key = key;
		this.name = name;
		this.meta = meta;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTranslationKey() {
		return key;
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
