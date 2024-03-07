package paulevs.bnb.entity;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNB;

public class CrimsonSpiderEntity extends NetherSpiderEntity {
	private static final Identifier ID = BNB.id("crimson_spider");
	
	public CrimsonSpiderEntity(Level level) {
		super(level);
		texture = "/assets/bnb/stationapi/textures/entity/crimson_spider.png";
	}
	
	@Override
	public Identifier getHandlerIdentifier() {
		return ID;
	}
}
