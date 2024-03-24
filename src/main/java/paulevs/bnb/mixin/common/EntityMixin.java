package paulevs.bnb.mixin.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.entity.ObsidianBoatEntity;
import paulevs.bnb.item.BNBItemTags;

@Mixin(Entity.class)
public class EntityMixin {
	@Inject(method = "setOnFire", at = @At("HEAD"), cancellable = true)
	private void bnb_disableFireDamage(CallbackInfo info) {
		if (!(Entity.class.cast(this) instanceof LivingEntity entity)) return;
		if ((entity.vehicle instanceof ObsidianBoatEntity)) {
			info.cancel();
			return;
		}
		if (entity instanceof PlayerEntity player && bnb_damageArmor(player)) {
			info.cancel();
		}
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
		if ((entity.vehicle instanceof ObsidianBoatEntity)) {
			info.cancel();
			return;
		}
		if (entity instanceof PlayerEntity player && player.fire > 0 && bnb_damageArmor(player)) {
			info.cancel();
		}
	}
	
	@Inject(method = "damageByFire", at = @At("HEAD"), cancellable = true)
	private void bnb_disableFireDamage(int damage, CallbackInfo info) {
		if (!(Entity.class.cast(this) instanceof PlayerEntity player)) return;
		for (int i = 3; i >= 0; i--) {
			ItemStack stack = player.inventory.armor[i];
			if (stack != null && stack.isIn(BNBItemTags.FIREPROOF_ARMOR)) {
				info.cancel();
				return;
			}
		}
	}
	
	@Unique
	private boolean bnb_damageArmor(PlayerEntity player) {
		for (int i = 3; i >= 0; i--) {
			ItemStack stack = player.inventory.armor[i];
			if (stack != null && stack.isIn(BNBItemTags.FIREPROOF_ARMOR)) {
				stack.applyDamage(1, player);
				if (stack.count < 1) {
					player.inventory.armor[i] = null;
				}
				player.fire = 0;
				return true;
			}
		}
		return false;
	}
}
