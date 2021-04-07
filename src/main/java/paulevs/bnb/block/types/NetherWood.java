package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.util.BlockUtil;

public enum NetherWood implements BlockEnum {
	CRIMSON_WOOD(0, "crimson_wood"),
	WARPED_WOOD(1, "warped_wood"),
	POISON_WOOD(2, "poison_wood");
	
	private final String name;
	private final int meta;
	
	NetherWood(int meta, String name) {
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
		return BlockUtil.isHorizontalSide(side) ? name + "_side" : name + "_top";
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
