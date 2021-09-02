package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum SoulPlantType implements BlockEnum {
	SOUL_BULBITE(0, "soul_bulbite", true),
	SOUL_CORAL(1, "soul_coral", false),
	BONE_PEAKS(2, "bone_peaks", true);
	
	private final boolean cross;
	private final String name;
	private final int meta;
	
	SoulPlantType(int meta, String name, boolean cross) {
		this.name = name;
		this.meta = meta;
		this.cross = cross;
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
	
	public boolean isCross() {
		return cross;
	}
}
