package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum UmbraPlantType implements BlockEnum {
	CYANIA(0, "cyania", true),
	SMALL_DARKSHROOM(1, "small_darkshroom", true),
	DARK_WILLOW(2, "dark_willow", true),
	DEEP_ROSE(3, "deep_rose", true);
	
	private final boolean cross;
	private final String name;
	private final int meta;
	
	UmbraPlantType(int meta, String name, boolean cross) {
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
