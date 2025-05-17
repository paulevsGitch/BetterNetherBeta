package paulevs.bnb.entity;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNB;

public class FalurianSpiderEntity extends NetherSpiderEntity {
	public static final Identifier ID = BNB.id("falurian_spider");
	
	public FalurianSpiderEntity(Level level) {
		super(level);
		texture = "/assets/bnb/stationapi/textures/entity/falurian_spider.png";
	}
	
	@Override
	public Identifier getHandlerIdentifier() {
		return ID;
	}
}
