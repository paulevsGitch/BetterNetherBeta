package paulevs.bnb.block.stone;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class SoulSandstoneBlock extends TemplateBlock {
	public SoulSandstoneBlock(Identifier id) {
		super(id, Material.STONE);
		setSounds(STONE_SOUNDS);
		setHardness(SANDSTONE.getHardness());
	}
}
