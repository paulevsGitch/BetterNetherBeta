package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import paulevs.bnb.block.types.SoulLayeredType;
import paulevs.bnb.interfaces.BlockEnum;

public class SoulLayeredBlock extends LayeredBlock {
	public <T extends BlockEnum> SoulLayeredBlock(String name, int id) {
		super(name, id, Material.SAND, SoulLayeredType.class);
		this.sounds(SOUL_SAND.sounds);
	}
}
