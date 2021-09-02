package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.StoneBlockEnum;
import paulevs.bnb.util.BlockUtil;

public enum UmbralithType implements StoneBlockEnum {
	UMBRALITH(0, "umbralith"),
	UMBRALITH_POLISHED(1, "umbralith_polished"),
	UMBRALITH_TILES(2, "umbralith_tiles"),
	UMBRALITH_PILLAR_X(3, "umbralith_pillar"),
	UMBRALITH_PILLAR_Y(4, "umbralith_pillar"),
	UMBRALITH_PILLAR_Z(5, "umbralith_pillar"),
	UMBRALITH_BRICKS(6, "umbralith_bricks"),
	DARK_NYLIUM(7, "dark_nylium");
	
	private final String name;
	private final int meta;
	
	UmbralithType(int meta, String name) {
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
		if (meta == DARK_NYLIUM.getMeta()) {
			if (BlockUtil.isTopSide(side)) {
				return "dark_nylium_top";
			}
			else if (BlockUtil.isHorizontalSide(side)) {
				return "dark_nylium_side";
			}
			else {
				return "umbralith";
			}
		}
		else if (name.contains("pillar")) {
			if (meta == getPillarYMeta()) {
				return BlockUtil.isSideY(side) ? name + "_top" : name + "_side";
			}
			else if (meta == getPillarXMeta()) {
				return BlockUtil.isSideX(side) ? name + "_top" : name + "_side";
			}
			else {
				return BlockUtil.isSideZ(side) ? name + "_top" : name + "_side";
			}
		}
		return name;
	}

	@Override
	public int getDropMeta() {
		return isPillar() ? getPillarYMeta() : meta;
	}

	@Override
	public int getMeta() {
		return meta;
	}
	
	@Override
	public boolean isInCreative() {
		if (isPillar()) {
			return meta == UMBRALITH_PILLAR_Y.meta;
		}
		return true;
	}

	@Override
	public boolean isPillar() {
		return meta == getPillarXMeta() || meta == getPillarYMeta() || meta == getPillarZMeta();
	}

	@Override
	public int getPillarXMeta() {
		return UMBRALITH_PILLAR_X.meta;
	}

	@Override
	public int getPillarYMeta() {
		return UMBRALITH_PILLAR_Y.meta;
	}

	@Override
	public int getPillarZMeta() {
		return UMBRALITH_PILLAR_Z.meta;
	}
}
