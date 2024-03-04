package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class NetherOre extends TemplateBlock {
	public NetherOre(Identifier id) {
		super(id, Material.STONE);
		setSounds(STONE_SOUNDS);
		setHardness(1.25F);
	}
}
