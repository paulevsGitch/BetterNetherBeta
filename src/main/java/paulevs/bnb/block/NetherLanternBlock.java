package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class NetherLanternBlock extends TemplateBlock {
	public NetherLanternBlock(Identifier id) {
		super(id, Material.FOLIAGE);
		setSounds(WOOL_SOUNDS);
		setLightEmittance(1F);
		setHardness(0.5F);
	}
}
