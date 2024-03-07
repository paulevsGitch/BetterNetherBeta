package paulevs.bnb.entity;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNB;

public class PoisonSpiderEntity extends NetherSpiderEntity {
	private static final Identifier ID = BNB.id("poison_spider");
	
	public PoisonSpiderEntity(Level level) {
		super(level);
		texture = "/assets/bnb/stationapi/textures/entity/poison_spider.png";
	}
	
	@Override
	public Identifier getHandlerIdentifier() {
		return ID;
	}
}
