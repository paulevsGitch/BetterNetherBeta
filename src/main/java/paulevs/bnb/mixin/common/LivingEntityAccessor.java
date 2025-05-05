package paulevs.bnb.mixin.common;

import net.minecraft.entity.living.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
	@Accessor("jumping")
	void bnb_setJumping(boolean jumping);
	
	@Accessor("movementSpeed")
	float bnb_getMovementSpeed();
	
	@Accessor("parallelMovement")
	void bnb_setParallelMovement(float parallelMovement);
}
