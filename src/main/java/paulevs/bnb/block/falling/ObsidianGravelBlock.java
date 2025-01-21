package paulevs.bnb.block.falling;

import net.modificationstation.stationapi.api.util.Identifier;

public class ObsidianGravelBlock extends BNBFallingBlock {
	public ObsidianGravelBlock(Identifier id) {
		super(id);
		setHardness(2.0F);
		setBlastResistance(1000.0f);
	}
}
