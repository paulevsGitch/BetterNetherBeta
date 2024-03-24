package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class NetherMetalBlock extends TemplateBlock {
	public NetherMetalBlock(Identifier id) {
		super(id, Material.METAL);
		setSounds(METAL_SOUNDS);
		setHardness(1.0F);
	}
}
