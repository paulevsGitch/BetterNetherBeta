package paulevs.bnb.block.stone;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class BNBObsidianBlock extends TemplateBlock {
	public BNBObsidianBlock(Identifier identifier) {
		super(identifier, Material.STONE);
		setHardness(OBSIDIAN.getHardness());
		setBlastResistance(2000.0F);
	}
}
