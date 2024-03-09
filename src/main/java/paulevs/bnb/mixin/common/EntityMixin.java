package paulevs.bnb.mixin.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.entity.ObsidianBoatEntity;

@Mixin(Entity.class)
public class EntityMixin {
	@Inject(method = "setOnFire", at = @At("HEAD"), cancellable = true)
	private void bnb_disableFireDamage(CallbackInfo info) {
		if (!(Entity.class.cast(this) instanceof LivingEntity entity)) return;
		if (!(entity.vehicle instanceof ObsidianBoatEntity)) return;
		info.cancel();
	}
	
	@Inject(method = "isInLava", at = @At("HEAD"), cancellable = true)
	private void bnb_isInLava(CallbackInfoReturnable<Boolean> info) {
		if (!(Entity.class.cast(this) instanceof LivingEntity entity)) return;
		if (!(entity.vehicle instanceof ObsidianBoatEntity)) return;
		info.setReturnValue(false);
	}
	
	@Inject(method = "move", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/entity/Entity;damageByFire(I)V",
		shift = Shift.BEFORE
	), cancellable = true)
	private void bnb_disableMovementFireDamage(double x, double y, double z, CallbackInfo info) {
		if (!(Entity.class.cast(this) instanceof LivingEntity entity)) return;
		if (!(entity.vehicle instanceof ObsidianBoatEntity)) return;
		info.cancel();
	}
}
