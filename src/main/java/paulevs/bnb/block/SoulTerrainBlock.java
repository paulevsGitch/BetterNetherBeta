package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import paulevs.bnb.block.types.SoulTerrainType;

public class SoulTerrainBlock extends MultiBlock {
	public SoulTerrainBlock(String name, int id) {
		super(name, id, Material.SAND, SoulTerrainType.class);
		this.setHardness(SOUL_SAND.getHardness());
		this.sounds(SOUL_SAND.sounds);
	}
}
