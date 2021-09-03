package paulevs.bnb.mixin.common;

import net.minecraft.entity.EntityBase;
import net.minecraft.entity.monster.ZombiePigman;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ZombiePigman.class)
public interface ZombiePigmanAccessor {
	@Accessor("anger")
	void setAnger(int anger);
	
	@Invoker
	void callSetTarget(EntityBase target);
	
	@Invoker
	EntityBase callGetAttackTarget();
}
