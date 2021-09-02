package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.util.BlockUtil;

public enum DarkshroomType implements BlockEnum {
	CENTER(0, "center"),
	SIDE(1, "side");
	
	private final String name;
	private final int meta;
	
	DarkshroomType(int meta, String name) {
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
		if (BlockUtil.isTopSide(side)) {
			return "darkshroom_top";
		}
		else if (BlockUtil.isHorizontalSide(side)) {
			return "darkshroom_side";
		}
		return "darkshroom_bottom";
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
		return false;
	}
}
