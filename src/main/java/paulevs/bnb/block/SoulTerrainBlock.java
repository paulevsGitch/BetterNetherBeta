package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.Identifier;

public class SoulTerrainBlock extends NetherTerrainBlock {
	public SoulTerrainBlock(Identifier id) {
		super(id, Material.DIRT);
		setHardness(SOUL_SAND.getHardness());
		setSounds(SOUL_SAND.sounds);
	}
}
