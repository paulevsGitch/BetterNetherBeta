package paulevs.bnb.block;

import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.block.property.BNBBlockMaterials;

public class NetherCloth extends TemplateBlock {
	public NetherCloth(Identifier id) {
		super(id, BNBBlockMaterials.NETHER_CLOTH);
		setSounds(WOOL_SOUNDS);
		setHardness(1.0F);
	}
}
