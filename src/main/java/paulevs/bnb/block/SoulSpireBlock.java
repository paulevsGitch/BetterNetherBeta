package paulevs.bnb.block;

import net.minecraft.block.material.Material;

public class SoulSpireBlock extends SimpleNetherBlock {
	public SoulSpireBlock(String name, int id) {
		super(name, id, Material.SAND);
		this.setLightEmittance(1F);
		this.sounds(WOOD_SOUNDS);
		this.setHardness(0.75F);
	}
}
