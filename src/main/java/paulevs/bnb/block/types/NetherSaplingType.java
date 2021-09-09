package paulevs.bnb.block.types;

import net.minecraft.level.structure.Structure;
import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.world.structures.NetherStructures;

public enum NetherSaplingType implements BlockEnum {
	PALE(0, "pale_sapling"),
	EMBER(1, "ember_sapling");
	
	private final String name;
	private final int meta;
	
	NetherSaplingType(int meta, String name) {
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
	
	public Structure getStructure() {
		switch (this) {
			case PALE: return NetherStructures.PALE_TREE;
			case EMBER: return NetherStructures.EMBER_TREE;
		}
		return null;
	}
}
