package paulevs.bnb.entity.ai;

import net.minecraft.entity.living.LivingEntity;

public interface EntityWithAI<E extends LivingEntity> {
	AITask<E> getDefaultTask();
	AITask<E> getCurrentTask();
	void setCurrentTask(AITask<E> task);
}
