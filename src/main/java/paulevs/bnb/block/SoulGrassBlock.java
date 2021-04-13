package paulevs.bnb.block;

import paulevs.bnb.block.types.SoulGrass;
import paulevs.bnb.util.BlockUtil;

public class SoulGrassBlock extends NetherPlantBlock {
	public SoulGrassBlock(String name, int id) {
		super(name, id, SoulGrass.class, true);
	}
	
	@Override
	protected boolean canPlantOnTopOf(int id) {
		return BlockUtil.isSoulTerrain(id);
	}
}
