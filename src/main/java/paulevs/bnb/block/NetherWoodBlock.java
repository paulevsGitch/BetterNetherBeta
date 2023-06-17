package paulevs.bnb.block;

import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.bnb.block.material.NetherMaterials;

public class NetherWoodBlock extends PillarBlock {
	public NetherWoodBlock(Identifier id) {
		super(id, NetherMaterials.NETHER_WOOD);
		setHardness(WOOD.getHardness());
		setSounds(WOOD_SOUNDS);
	}
}
