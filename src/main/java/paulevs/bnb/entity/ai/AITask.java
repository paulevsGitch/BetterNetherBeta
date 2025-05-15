package paulevs.bnb.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.WalkingEntity;
import net.minecraft.util.maths.MCMath;
import net.minecraft.util.maths.Vec3D;
import net.minecraft.util.maths.VectorCache;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.mixin.common.LivingEntityAccessor;

public abstract class AITask<E extends LivingEntity> {
	protected final E entity;
	
	protected AITask(E entity) {
		this.entity = entity;
	}
	
	public abstract void process();
	
	protected void lookAt(Entity target) {
		float dx = (float) (target.x - entity.x);
		float dy = (float) (target.y - entity.y);
		float dz = (float) (target.z - entity.z);
		float lenXZ = dx * dx + dz * dz;
		entity.yaw = (float) Math.atan2(dz, dx) * 180.0F / (float) Math.PI - 90.0F;
		entity.pitch = (float) Math.atan2(lenXZ, dy) * 180.0F / (float) Math.PI - 90.0F;
	}
	
	protected static void moveOnPath(WalkingEntity entity, VectorCache path) {
		if (path.noMoreData()) return;
		
		Vec3D pos = path.get(entity);
		float maxDist = entity.width * 2.0F;
		maxDist *= maxDist;
		
		for (int i = 0; i < path.size && pos != null && pos.distance(entity.x, pos.y, entity.z) < maxDist; i++) {
			path.increment();
			if (path.noMoreData()) pos = null;
			else pos = path.get(entity);
		}
		
		LivingEntityAccessor accessor = (LivingEntityAccessor) entity;
		boolean jumping = false;
		
		if (pos != null) {
			int iy = MCMath.floor(entity.boundingBox.minY + 0.5);
			
			double px = pos.x - entity.x;
			double py = pos.z - entity.z;
			double pz = pos.y - iy;
			
			float var14 = (float) (Math.atan2(py, px) * 180.0 / (float) Math.PI) - 90.0F;
			float deltaYaw = var14 - entity.yaw;
			accessor.bnb_setParallelMovement(accessor.bnb_getMovementSpeed());
			
			if (deltaYaw < -180.0F) deltaYaw += 360.0F;
			else if (deltaYaw >= 180.0F) deltaYaw -= 360.0F;
			deltaYaw = MathHelper.clamp(deltaYaw, -30.0F, 30.0F);
			
			entity.yaw += deltaYaw;
			if (pz > 0.0) jumping = true;
		}
		
		if (!jumping && entity.field_1624 && !entity.hasPath()) jumping = true;
		if (!jumping && entity.level.random.nextFloat() < 0.8F && (entity.isFallen() || entity.isInLava())) jumping = true;
		
		accessor.bnb_setJumping(jumping);
	}
}
