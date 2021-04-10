package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.StoneBlockEnum;
import paulevs.bnb.util.BlockUtil;

public enum NetherrackBricks implements StoneBlockEnum {
	NETHERRACK_BRICKS(0, "netherrack_bricks"),
	NETHERRACK_BRICK_LARGE_TILE(1, "netherrack_brick_large_tile"),
	NETHERRACK_BRICK_SMALL_TILE(2, "netherrack_brick_small_tile"),
	NETHERRACK_BRICK_PILLAR_X(3, "netherrack_brick_pillar"),
	NETHERRACK_BRICK_PILLAR_Y(4, "netherrack_brick_pillar"),
	NETHERRACK_BRICK_PILLAR_Z(5, "netherrack_brick_pillar");
	
	private final String name;
	private final int meta;
	
	NetherrackBricks(int meta, String name) {
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
		if (name.contains("pillar")) {
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
			return meta == NETHERRACK_BRICK_PILLAR_Y.meta;
		}
		return true;
	}

	@Override
	public boolean isPillar() {
		return meta == getPillarXMeta() || meta == getPillarYMeta() || meta == getPillarZMeta();
	}

	@Override
	public int getPillarXMeta() {
		return NETHERRACK_BRICK_PILLAR_X.meta;
	}

	@Override
	public int getPillarYMeta() {
		return NETHERRACK_BRICK_PILLAR_Y.meta;
	}

	@Override
	public int getPillarZMeta() {
		return NETHERRACK_BRICK_PILLAR_Z.meta;
	}
}
