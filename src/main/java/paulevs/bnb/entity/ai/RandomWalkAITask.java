package paulevs.bnb.entity.ai;

import net.minecraft.entity.living.LivingEntity;

public class RandomWalkAITask<E extends LivingEntity> extends AITask<E> {
	protected RandomWalkAITask(E entity) {
		super(entity);
	}
	
	@Override
	public void process() {
	
	}
}
