package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.util.BlockUtil;

public enum NetherStemType implements BlockEnum {
	DARKSHROOM(0, "darkshroom_stem");
	
	private final String name;
	private final int meta;
	
	NetherStemType(int meta, String name) {
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
