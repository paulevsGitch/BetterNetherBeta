package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.util.BlockUtil;

public enum NetherLanternType implements BlockEnum {
	CRIMSON_LANTERN(0, "crimson_lantern"),
	WARPED_LANTERN(1, "warped_lantern"),
	POISON_LANTERN(2, "poison_lantern"),
	GHOST_PUMPKIN(3, "ghost_pumpkin");
	
	private final String name;
	private final int meta;
	
	NetherLanternType(int meta, String name) {
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
		if (this == GHOST_PUMPKIN) {
			if (BlockUtil.isTopSide(side)) {
				return name + "_top";
			}
			if (BlockUtil.isHorizontalSide(side)) {
				return name + "_side";
			}
			return name + "_bottom";
		}
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
