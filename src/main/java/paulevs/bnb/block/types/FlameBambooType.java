package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.util.BlockUtil;

public enum FlameBambooType implements BlockEnum {
	SAPLING(0, "flame_bamboo_sapling"),
	STEM(1, "flame_bamboo_stem"),
	MIDDLE(2, "flame_bamboo_middle"),
	TOP(3, "flame_bamboo_top"),
	TOP_INACTIVE(4, "flame_bamboo_top"),
	UNNATURAL_STEM(5, "flame_bamboo_stem"),
	LADDER(6, "flame_bamboo_ladder");
	
	private final String name;
	private final int meta;
	
	FlameBambooType(int meta, String name) {
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
		return meta == SAPLING.getMeta() || meta == UNNATURAL_STEM.getMeta() || meta == LADDER.getMeta();
	}
}