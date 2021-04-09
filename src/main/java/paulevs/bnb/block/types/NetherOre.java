package paulevs.bnb.block.types;

import paulevs.bnb.interfaces.BlockEnum;

public enum NetherOre implements BlockEnum {
	ORICHALCUM_ORE(0, "orichalcum_ore");
	
	private final String name;
	private final int meta;
	
	NetherOre(int meta, String name) {
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
