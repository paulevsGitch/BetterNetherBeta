package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.StoneBlockEnum;
import paulevs.bnb.util.BlockUtil;

public enum BasaltBlockType implements StoneBlockEnum {
	BASALT(0, "basalt"),
	BASALT_SMOOTH(1, "smooth_basalt"),
	BASALT_PILLAR_X(2, "basalt_pillar"),
	BASALT_PILLAR_Y(3, "basalt_pillar"),
	BASALT_PILLAR_Z(4, "basalt_pillar");
	
	private final String name;
	private final int meta;
	
	BasaltBlockType(int meta, String name) {
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
		if (meta == 0) {
			return BlockUtil.isHorizontalSide(side) ? name + "_side" : name + "_top";
		}
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
			return meta == BASALT_PILLAR_Y.meta;
		}
		return true;
	}

	@Override
	public boolean isPillar() {
		return meta == getPillarXMeta() || meta == getPillarYMeta() || meta == getPillarZMeta();
	}

	@Override
	public int getPillarXMeta() {
		return BASALT_PILLAR_X.meta;
	}

	@Override
	public int getPillarYMeta() {
		return BASALT_PILLAR_Y.meta;
	}

	@Override
	public int getPillarZMeta() {
		return BASALT_PILLAR_Z.meta;
	}
}
