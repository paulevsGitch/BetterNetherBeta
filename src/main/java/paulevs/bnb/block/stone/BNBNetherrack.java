package paulevs.bnb.block.stone;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class BNBNetherrack extends TemplateBlock {
	public BNBNetherrack(Identifier identifier) {
		super(identifier, Material.STONE);
		setHardness(NETHERRACK.getHardness() * 3.0F);
	}
}
