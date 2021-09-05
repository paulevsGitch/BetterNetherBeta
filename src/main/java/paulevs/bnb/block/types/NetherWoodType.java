package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.util.BlockUtil;

public enum NetherWoodType implements BlockEnum {
	CRIMSON_WOOD(0, "crimson_wood"),
	WARPED_WOOD(1, "warped_wood"),
	POISON_WOOD(2, "poison_wood"),
	PALE_WOOD(3, "pale_wood"),
	EMBER_WOOD(4, "ember_wood"),
	FLAME_BAMBOO_BLOCK(5, "flame_bamboo_block");
	
	private final String name;
	private final int meta;
	
	NetherWoodType(int meta, String name) {
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
