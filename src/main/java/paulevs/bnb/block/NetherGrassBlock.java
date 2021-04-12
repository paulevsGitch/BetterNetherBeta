package paulevs.bnb.block;

import paulevs.bnb.block.types.NetherPlants;

public class NetherGrassBlock extends NetherPlantBlock {
	public NetherGrassBlock(String name, int id) {
		super(name, id, NetherPlants.class, true);
	}
}
