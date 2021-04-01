package paulevs.bnb.block;

import paulevs.bnb.block.material.NetherMaterials;
import paulevs.bnb.block.types.NetherPlanks;

public class NetherSlabBlock extends MultiBlock {
	public NetherSlabBlock(String name, int id) {
		super(name, id, NetherMaterials.NETHER_WOOD, NetherPlanks.class);
		this.setHardness(WOOD.getHardness());
		this.sounds(WOOD_SOUNDS);
	}
}
