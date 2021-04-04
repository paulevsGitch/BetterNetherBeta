package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import paulevs.bnb.block.types.NetherLeaves;

public class NetherLeavesBlock extends MultiBlock {
	public NetherLeavesBlock(String name, int id) {
		super(name, id, Material.LEAVES, NetherLeaves.class);
		this.setHardness(LEAVES.getHardness());
		this.sounds(GRASS_SOUNDS);
	}
}
