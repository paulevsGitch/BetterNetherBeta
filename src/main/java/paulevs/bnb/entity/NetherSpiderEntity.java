package paulevs.bnb.entity;

import net.minecraft.entity.living.monster.SpiderEntity;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.server.entity.MobSpawnDataProvider;

public abstract class NetherSpiderEntity extends SpiderEntity implements MobSpawnDataProvider {
	public NetherSpiderEntity(Level level) {
		super(level);
		setSize(2.5F, 1.25f);
		immuneToFire = true;
		health = 30;
	}
	
	@Override
	public void writeToMessage(MessagePacket message) {
		MobSpawnDataProvider.super.writeToMessage(message);
	}
	
	@Override
	public void readFromMessage(MessagePacket message) {
		MobSpawnDataProvider.super.readFromMessage(message);
	}
}
