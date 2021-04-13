package paulevs.bnb.block;

import net.minecraft.block.material.Material;

public class SoulSoilBlock extends SimpleNetherBlock {
	public SoulSoilBlock(String name, int id) {
		super(name, id, Material.SAND);
		this.setHardness(SOUL_SAND.getHardness());
		this.sounds(SOUL_SAND.sounds);
	}
}
