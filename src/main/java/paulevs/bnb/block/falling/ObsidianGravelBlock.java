package paulevs.bnb.block.falling;

import net.modificationstation.stationapi.api.template.block.TemplateSandBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class ObsidianGravelBlock extends TemplateSandBlock {
	public ObsidianGravelBlock(Identifier id) {
		super(id, 0);
		setHardness(2.0F);
		setBlastResistance(1000.0f);
		setSounds(GRAVEL_SOUNDS);
	}
}
