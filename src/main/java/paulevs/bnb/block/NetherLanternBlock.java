package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

public class NetherLanternBlock extends TemplateBlockBase {
	public NetherLanternBlock(Identifier id) {
		super(id, Material.FOLIAGE);
		setSounds(WOOL_SOUNDS);
		setLightEmittance(1F);
		setHardness(0.5F);
	}
}