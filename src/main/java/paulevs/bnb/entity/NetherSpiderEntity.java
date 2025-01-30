package paulevs.bnb.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.living.monster.SpiderEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.maths.MCMath;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.server.entity.MobSpawnDataProvider;
import paulevs.bnb.BNB;
import paulevs.bnb.block.BNBBlockTags;
import paulevs.bnb.util.WorldUtil;

public abstract class NetherSpiderEntity extends SpiderEntity implements MobSpawnDataProvider {
	private double fearVelocityX;
	private double fearVelocityZ;
	private int fearTicks;
	
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
	protected void tryAttack(Entity target, float distance) {
		if (distance < 2.0F || distance > 6.0F || random.nextInt(10) != 0) {
			super.tryAttack(target, distance);
		}
		else if (onGround) {
			float dx = (float) (target.x - x);
			float dz = (float) (target.z - z);
			float dist = MCMath.sqrt(dx * dx + dz * dz);
			velocityX = dx / dist * 0.4F + velocityX * 0.2F;
			velocityZ = dz / dist * 0.4F + velocityZ * 0.2F;
			velocityY = 0.15F;
		}
	}
	
	@Override
	public Entity getAttackTarget() {
		if (fearTicks > 0) return null;
		Entity target = super.getAttackTarget();
		if (target instanceof PlayerEntity player && BNB.isCreative(player)) return null;
		return entity;
	}
	
	@Override
	protected void tickHandSwing() {
		super.tickHandSwing();
		processRepellent();
		processFear();
	}
	
	private void processRepellent() {
		if ((this.ticks & 7) > 0) return;
		double centerX;
		double centerZ;
		boolean isInFear = containsRepellent((int) x, (int) y, (int) z);
		if (!isInFear) {
			HitResult hit = WorldUtil.raycast(level, this);
			if (hit == null) return;
			isInFear = containsRepellent(hit.x, hit.y, hit.z);
			if (!isInFear) return;
			centerX = hit.x + 0.5;
			centerZ = hit.z + 0.5;
			fearTicks = 40;
		}
		else {
			centerX = Math.floor(x) + 0.5;
			centerZ = Math.floor(z) + 0.5;
			fearTicks = 40;
		}
		fearVelocityX = x - centerX;
		fearVelocityZ = z - centerZ;
		double l = Math.sqrt(fearVelocityX * fearVelocityX + fearVelocityZ * fearVelocityZ);
		l = l < 0.001 ? 1.0 : 20.0 / l;
		fearVelocityX *= l;
		fearVelocityZ *= l;
	}
	
	private void processFear() {
		movementSpeed = 0.8F;
		if (fearTicks == 0) return;
		fearTicks--;
		movementSpeed = 1.5F;
		int targetX = (int) (x + fearVelocityX);
		int targetZ = (int) (z + fearVelocityZ);
		setPath(level.getPath(this, targetX, (int) y, targetZ, 20.0F));
	}
	
	private boolean containsRepellent(int x, int y, int z) {
		for (int dx = -5; dx < 5; dx++) {
			int wx = x + dx;
			for (int dy = -5; dy < 5; dy++) {
				int wy = y + dy;
				for (int dz = -5; dz < 5; dz++) {
					int wz = z + dz;
					if (level.getBlockState(wx, wy, wz).isIn(BNBBlockTags.SPIDER_REPELLENT)) return true;
				}
			}
		}
		return false;
	}
}
