package paulevs.bnb.block;

import net.modificationstation.stationapi.api.registry.Identifier;

public class GhostPumpkinBlock extends NetherLanternBlock {
	public GhostPumpkinBlock(Identifier id) {
		super(id);
		setHardness(PUMPKIN.getHardness());
		setSounds(PUMPKIN.sounds);
	}
}
