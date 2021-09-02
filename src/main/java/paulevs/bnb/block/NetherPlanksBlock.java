package paulevs.bnb.block;

import paulevs.bnb.block.material.NetherMaterials;
import paulevs.bnb.block.types.NetherPlanksType;

public class NetherPlanksBlock extends MultiBlock {
	public NetherPlanksBlock(String name, int id) {
		super(name, id, NetherMaterials.NETHER_WOOD, NetherPlanksType.class);
		this.setHardness(WOOD.getHardness());
		this.sounds(WOOD_SOUNDS);
	}
}
