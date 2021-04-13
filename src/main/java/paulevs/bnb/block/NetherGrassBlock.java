package paulevs.bnb.block;

import paulevs.bnb.block.types.NetherGrass;

public class NetherGrassBlock extends NetherPlantBlock {
	public NetherGrassBlock(String name, int id) {
		super(name, id, NetherGrass.class, true);
	}
}
