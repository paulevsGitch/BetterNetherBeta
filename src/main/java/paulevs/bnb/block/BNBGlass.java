package paulevs.bnb.block;

import net.modificationstation.stationapi.api.template.block.TemplateTranslucentBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.block.property.BNBBlockMaterials;

public class BNBGlass extends TemplateTranslucentBlock {
	public BNBGlass(Identifier id) {
		super(id, 0, BNBBlockMaterials.NETHER_CLOTH, false);
		setSounds(GLASS_SOUNDS);
		setHardness(1.0F);
	}
	
	@Override
	public int getRenderPass() {
		return 1;
	}
}
