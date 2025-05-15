package paulevs.bnb.entity.ai;

import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.WalkingEntity;
import net.minecraft.util.maths.VectorCache;

public class TargetAttackAITask <T extends LivingEntity, E extends WalkingEntity> extends AITask<E> {
	private final Class<T> targetClass;
	private final int range;
	
	private LivingEntity target;
	private VectorCache path;
	private int updateTick;
	
	public TargetAttackAITask(E entity, Class<T> targetClass, int range) {
		super(entity);
		this.targetClass = targetClass;
		this.range = range;
	}
	
	@Override
	public void process() {
	
	}
	
	/*@Override
	public void process() {
		if (path != null) EntityAI.moveOnPath(entity, path);
		if (target != null) EntityAI.lookAt(entity, target);
		
		if (updateTick++ < 8) return;
		updateTick = 0;
		findTarget(entity);
	}
	
	@Override
	public boolean canStart() {
		findTarget(entity);
		return target != null;
	}
	
	@Override
	public boolean isFinished() {
		return path == null;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	private void findTarget(E entity) {
		target = null;
		path = null;
		
		List entities = entity.level.getEntities(targetClass, Box.createAndCache(
			entity.x - range,
			entity.y - range,
			entity.z - range,
			entity.x + range,
			entity.y + range,
			entity.z + range
		));
		
		if (entities.isEmpty()) return;
		
		entities.sort((o1, o2) -> {
			double d1 = ((T) o1).distanceTo(entity);
			double d2 = ((T) o2).distanceTo(entity);
			return Double.compare(d1, d2);
		});
		
		for (Object obj : entities) {
			target = (T) obj;
			path = entity.level.getPath(entity, target, range);
			if (path != null) break;
		}
		
		//if (path != null) System.out.println("Path " + path.size);
		((NetherSpiderEntity) entity).path = path;
	}*/
}
