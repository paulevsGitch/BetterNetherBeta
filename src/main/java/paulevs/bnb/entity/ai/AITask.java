package paulevs.bnb.entity.ai;

import net.minecraft.entity.living.LivingEntity;

public abstract class AITask<E extends LivingEntity> {
	public abstract void process(E entity);
	public abstract boolean canStart(E entity);
	public abstract boolean isFinished();
}
