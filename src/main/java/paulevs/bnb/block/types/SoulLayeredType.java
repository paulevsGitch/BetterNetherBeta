package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum SoulLayeredType implements BlockEnum {
	// Crimson plants
	SOUL_SAND(0, "soul_sand_layer", "soul_sand"),
	SOUL_SOIL(1, "soul_soil_layer", "soul_soil");
	
	private final String texture;
	private final String name;
	private final int meta;
	
	SoulLayeredType(int index, String name, String texture) {
		this.texture = texture;
		this.meta = index << 2;
		this.name = name;
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
		return texture;
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
