package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.util.BlockUtil;

public enum DistortedBambooType implements BlockEnum {
	SAPLING(0, "distorted_bamboo_sapling"),
	STEM(1, "distorted_bamboo_stem"),
	MIDDLE(2, "distorted_bamboo_middle"),
	TOP(3, "distorted_bamboo_top"),
	TOP_INACTIVE(4, "distorted_bamboo_top"),
	UNNATURAL_STEM(5, "distorted_bamboo_stem");
	
	private final String name;
	private final int meta;
	
	DistortedBambooType(int meta, String name) {
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