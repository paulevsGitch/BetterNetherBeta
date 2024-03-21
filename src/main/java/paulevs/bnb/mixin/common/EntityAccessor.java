package paulevs.bnb.mixin.common;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
	@Accessor("immuneToFire")
	boolean bnb_immuneToFire();
	
	@Invoker("setOnFire")
	void bnb_setOnFire();
}
