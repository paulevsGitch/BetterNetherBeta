package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.sound.BNBSounds;

public class NetherTerrainBlock extends TemplateBlock {
	public NetherTerrainBlock(Identifier id, Material material) {
		super(id, material);
	}
	
	public NetherTerrainBlock(Identifier id) {
		super(id, Material.STONE);
		setHardness(NETHERRACK.getHardness());
		setSounds(BNBSounds.NYLIUM_BLOCK);
	}
}
