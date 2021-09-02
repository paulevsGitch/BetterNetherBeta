package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.util.BlockUtil;

public enum SoulTerrainType implements BlockEnum {
	SOUL_SOIL(0, "soul_soil"),
	SOUL_NYLIUM(1, "soul_nylium");
	
	private final String name;
	private final int meta;
	
	SoulTerrainType(int meta, String name) {
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
		if (meta == SOUL_SOIL.getMeta()) {
			return "soul_soil";
		}
		return BlockUtil.isHorizontalSide(side) ? name + "_side" : BlockUtil.isTopSide(side) ? name + "_top" : "soul_soil";
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