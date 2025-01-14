package paulevs.bnb.block.tree;

import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.block.BNBPillarBlock;
import paulevs.bnb.block.property.BNBBlockMaterials;

public class NetherLogBlock extends BNBPillarBlock {
	public NetherLogBlock(Identifier id) {
		super(id, BNBBlockMaterials.NETHER_LOG);
		setHardness(LOG.getHardness());
		setSounds(WOOD_SOUNDS);
	}
}
