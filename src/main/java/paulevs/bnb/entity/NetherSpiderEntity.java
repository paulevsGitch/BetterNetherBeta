package paulevs.bnb.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.living.monster.SpiderEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.Level;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.server.entity.MobSpawnDataProvider;
import paulevs.vbe.utils.CreativeUtil;

public abstract class NetherSpiderEntity extends SpiderEntity implements MobSpawnDataProvider {
	public NetherSpiderEntity(Level level) {
		super(level);
		setSize(1.5F, 1.25f);
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
	
	@Override
	protected Entity getAttackTarget() {
		PlayerEntity player = (PlayerEntity) super.getAttackTarget();
		return player == null || CreativeUtil.isCreative(player) ? null : player;
	}
	
	@Override
	protected void tryAttack(Entity target, float distance) {
		if (distance < 2.0F || distance > 6.0F || random.nextInt(10) != 0) {
			super.tryAttack(target, distance);
		}
		else if (onGround) {
			float dx = (float) (target.x - x);
			float dz = (float) (target.z - z);
			float dist = MathHelper.sqrt(dx * dx + dz * dz);
			velocityX = dx / dist * 0.4F + velocityX * 0.2F;
			velocityZ = dz / dist * 0.4F + velocityZ * 0.2F;
			velocityY = 0.15F;
		}
	}
}
