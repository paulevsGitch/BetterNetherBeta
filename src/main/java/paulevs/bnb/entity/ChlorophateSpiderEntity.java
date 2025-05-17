package paulevs.bnb.entity;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNB;

public class ChlorophateSpiderEntity extends NetherSpiderEntity {
	public static final Identifier ID = BNB.id("chlorophate_spider");
	
	public ChlorophateSpiderEntity(Level level) {
		super(level);
		texture = "/assets/bnb/stationapi/textures/entity/chlorophate_spider.png";
	}
	
	@Override
	public Identifier getHandlerIdentifier() {
		return ID;
	}
}
