package paulevs.bnb.block.falling;

import net.modificationstation.stationapi.api.template.block.TemplateSandBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class BNBFallingBlock extends TemplateSandBlock {
	public BNBFallingBlock(Identifier id) {
		super(id, 0);
		setHardness(1.0F);
		setSounds(GRAVEL_SOUNDS);
	}
}
