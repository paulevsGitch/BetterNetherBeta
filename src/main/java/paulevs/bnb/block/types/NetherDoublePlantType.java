package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum NetherDoublePlantType implements BlockEnum {
	RED_CATTAIL(0, "red_cattail");
	
	private final String name;
	private final int meta;
	
	NetherDoublePlantType(int meta, String name) {
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
