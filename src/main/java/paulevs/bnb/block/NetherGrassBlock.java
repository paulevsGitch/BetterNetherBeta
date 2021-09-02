package paulevs.bnb.block;

import paulevs.bnb.block.types.NetherPlantType;

public class NetherGrassBlock extends NetherPlantBlock {
	public NetherGrassBlock(String name, int id) {
		super(name, id, NetherPlantType.class, true);
	}
}
