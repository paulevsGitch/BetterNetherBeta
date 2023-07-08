package paulevs.bnb.block;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;
import paulevs.bnb.block.properties.BNBBlockMaterials;

public class NetherPlanksBlock extends TemplateBlockBase {
	public NetherPlanksBlock(Identifier id) {
		super(id, BNBBlockMaterials.NETHER_WOOD);
		setHardness(WOOD.getHardness());
		setSounds(WOOD_SOUNDS);
	}
}
