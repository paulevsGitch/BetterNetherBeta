package paulevs.bnb.block;

import paulevs.bnb.block.material.NetherMaterials;
import paulevs.bnb.block.types.NetherWood;
import paulevs.bnb.interfaces.BlockWithLight;

public class NetherWoodBlock extends MultiBlock implements BlockWithLight {
	public NetherWoodBlock(String name, int id) {
		super(name, id, NetherMaterials.NETHER_WOOD, NetherWood.class);
		this.setHardness(WOOD.getHardness());
		this.sounds(WOOD_SOUNDS);
	}
}
