package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;
import paulevs.bnb.sound.BNBSounds;

public class NetherTerrainBlock extends TemplateBlockBase {
	public NetherTerrainBlock(Identifier id, Material material) {
		super(id, material);
	}
	
	public NetherTerrainBlock(Identifier id) {
		super(id, Material.STONE);
		setHardness(NETHERRACK.getHardness());
		setSounds(BNBSounds.NYLIUM_BLOCK);
	}
}
