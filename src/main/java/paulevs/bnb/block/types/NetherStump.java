package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.util.BlockUtil;

public enum NetherStump implements BlockEnum {
	CRIMSON_STUMP(0, "crimson_stump", "Crimson Wood"),
	CRIMSON_STUMP_FULL(1, "crimson_stump_full", "Crimson Wood"),
	WARPED_STUMP(2, "warped_stump", "Warped Wood"),
	WARPED_STUMP_FULL(3, "warped_stump_full", "Warped Wood"),
	POISON_STUMP(4, "poison_stump", "Poison Wood"),
	POISON_STUMP_FULL(5, "poison_stump_full", "Poison Wood");
	
	private final String localizedName;
	private final String name;
	private final int meta;
	
	NetherStump(int meta, String name, String localizedName) {
		this.localizedName = localizedName;
		this.name = name;
		this.meta = meta;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getLocalizedName() {
		return localizedName;
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
