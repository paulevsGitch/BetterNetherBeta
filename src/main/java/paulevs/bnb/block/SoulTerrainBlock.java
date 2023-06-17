package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.bnb.BNB;

public class SoulTerrainBlock extends NetherTerrainBlock {
	public SoulTerrainBlock(Identifier id) {
		super(id, Material.DIRT);
		setHardness(SOUL_SAND.getHardness());
		setSounds(SOUL_SAND.sounds);
	}
	
	@Override
	public Identifier[] getTextureNames(Identifier id) {
		return new Identifier[] {
			BNB.id("block/soul_soil"),
			BNB.id("block/" + id.id + "_top"),
			BNB.id("block/" + id.id + "_side")
		};
	}
}
