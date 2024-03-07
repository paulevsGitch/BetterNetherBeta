package paulevs.bnb.entity;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNB;

public class WarpedSpiderEntity extends NetherSpiderEntity {
	private static final Identifier ID = BNB.id("warped_spider");
	
	public WarpedSpiderEntity(Level level) {
		super(level);
		texture = "/assets/bnb/stationapi/textures/entity/warped_spider.png";
	}
	
	@Override
	public Identifier getHandlerIdentifier() {
		return ID;
	}
}
