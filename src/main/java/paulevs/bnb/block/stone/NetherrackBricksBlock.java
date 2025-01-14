package paulevs.bnb.block.stone;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class NetherrackBricksBlock extends TemplateBlock {
	public NetherrackBricksBlock(Identifier identifier) {
		super(identifier, Material.STONE);
		setHardness(1F);
	}
}
