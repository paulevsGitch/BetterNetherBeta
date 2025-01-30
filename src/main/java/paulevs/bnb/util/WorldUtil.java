package paulevs.bnb.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.maths.MCMath;
import net.minecraft.util.maths.Vec3D;

public class WorldUtil {
	private static final float TO_RADIANS = (float) (Math.PI / 180.0);
	private static final float PI = (float) Math.PI;
	
	public static HitResult raycast(Level level, PlayerEntity player) {
		double dist = 5.0;
		float pitch = player.prevPitch + (player.pitch - player.prevPitch);
		
		double x = player.prevX + (player.x - player.prevX);
		double y = player.prevY + (player.y - player.prevY) + 1.62 - player.standingEyeHeight;
		double z = player.prevZ + (player.z - player.prevZ);
		Vec3D pos = Vec3D.getFromCacheAndSet(x, y, z);
		
		float yaw = player.prevYaw + (player.yaw - player.prevYaw);
		yaw = -yaw * TO_RADIANS - PI;
		float cosYaw = MCMath.cos(yaw);
		float sinYaw = MCMath.sin(yaw);
		float cosPitch = -MCMath.cos(-pitch * TO_RADIANS);
		
		Vec3D dir = pos.add(
			sinYaw * cosPitch * dist,
			MCMath.sin(-pitch * TO_RADIANS) * dist,
			cosYaw * cosPitch * dist
		);
		
		return level.getHitResult(pos, dir, false);
	}
	
	public static HitResult raycast(Level level, Entity entity) {
		double dist = 5.0;
		float pitch = entity.prevPitch + (entity.pitch - entity.prevPitch);
		
		double x = entity.prevX + (entity.x - entity.prevX);
		double y = entity.prevY + (entity.y - entity.prevY);
		double z = entity.prevZ + (entity.z - entity.prevZ);
		Vec3D pos = Vec3D.getFromCacheAndSet(x, y, z);
		
		float yaw = entity.prevYaw + (entity.yaw - entity.prevYaw);
		yaw = -yaw * TO_RADIANS - PI;
		float cosYaw = MCMath.cos(yaw);
		float sinYaw = MCMath.sin(yaw);
		float cosPitch = -MCMath.cos(-pitch * TO_RADIANS);
		
		Vec3D dir = pos.add(
			sinYaw * cosPitch * dist,
			MCMath.sin(-pitch * TO_RADIANS) * dist,
			cosYaw * cosPitch * dist
		);
		
		return level.getHitResult(pos, dir, false);
	}
}
