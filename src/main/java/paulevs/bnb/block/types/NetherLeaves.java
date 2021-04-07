package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum NetherLeaves implements BlockEnum {
	CRIMSON_LEAVES(0, "crimson_leaves"),
	WARPED_LEAVES(1, "warped_leaves"),
	POISON_LEAVES(2, "poison_leaves");
	
	private final String name;
	private final int meta;
	
	NetherLeaves(int meta, String name) {
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
