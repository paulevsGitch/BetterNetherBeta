package paulevs.bnb.mixin.common;

import net.minecraft.entity.living.FlyingEntity;
import net.minecraft.entity.living.monster.GhastEntity;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GhastEntity.class)
public abstract class GhastEntityMixin extends FlyingEntity {
	public GhastEntityMixin(Level arg) {
		super(arg);
	}
	
	@Inject(method = "canSpawn", at = @At("HEAD"), cancellable = true)
	private void bnb_canSpawn(CallbackInfoReturnable<Boolean> info) {
		Box bounds = Box.createAndCache(x - 128, y - 128, z - 128, x + 128, y + 128, z + 128);
		if (level.getEntities(GhastEntity.class, bounds).size() > 1) {
			info.setReturnValue(false);
		}
	}
}
