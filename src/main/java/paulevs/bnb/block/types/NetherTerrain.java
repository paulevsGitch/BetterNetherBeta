package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.util.BlockUtil;

public enum NetherTerrain implements BlockEnum {
	CRIMSON_NYLIUM(0, "crimson_nylium", "Crimson Nylium"),
	WARPED_NYLIUM(1, "warped_nylium", "Warped Nylium"),
	POISON_NYLIUM(2, "poison_nylium", "Poison Nylium");
	
	private final String localizedName;
	private final String name;
	private final int meta;
	
	NetherTerrain(int meta, String name, String localizedName) {
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
		return BlockUtil.isHorizontalSide(side) ? name + "_side" : BlockUtil.isTopSide(side) ? name + "_top" : "netherrack";
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
