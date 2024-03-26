package paulevs.bnb.entity;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNB;

public class PirozenSpiderEntity extends NetherSpiderEntity {
	private static final Identifier ID = BNB.id("pirozen_spider");
	
	public PirozenSpiderEntity(Level level) {
		super(level);
		texture = "/assets/bnb/stationapi/textures/entity/pirozen_spider.png";
	}
	
	@Override
	public Identifier getHandlerIdentifier() {
		return ID;
	}
}
