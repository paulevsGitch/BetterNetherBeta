package paulevs.bnb.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.entity.ObsidianBoatEntity;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity {
	public BoatEntityMixin(Level arg) {
		super(arg);
	}
	
	@WrapOperation(method = "tick", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/Level;containsMaterialMetaCheck(Lnet/minecraft/util/maths/Box;Lnet/minecraft/block/material/Material;)Z"
	))
	private boolean bnb_boatTick(Level level, Box box, Material material, Operation<Boolean> original) {
		if (BoatEntity.class.cast(this) instanceof ObsidianBoatEntity) {
			return original.call(level, box, Material.LAVA);
		}
		return original.call(level, box, material);
	}
	
	@WrapOperation(method = "tick", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/Level;addParticle(Ljava/lang/String;DDDDDD)V")
	)
	private void bnb_changeParticle(Level level, String name, double x, double y, double z, double vx, double vy, double vz, Operation<Void> original) {
		if (BoatEntity.class.cast(this) instanceof ObsidianBoatEntity) {
			if (level.random.nextInt(16) > 0) return;
			name = level.random.nextBoolean() ? "flame" : "lava";
		}
		original.call(level, name, x, y, z, vx, vy, vz);
	}
	
	@Inject(method = "tick", at = @At(
		value = "INVOKE",
		target = "Ljava/lang/Math;sqrt(D)D",
		shift = Shift.AFTER
	))
	private void bnb_fixBoatDrop(CallbackInfo info) {
		if (BoatEntity.class.cast(this) instanceof ObsidianBoatEntity) {
			this.field_1624 = false;
		}
	}
	
	@Inject(method = "interact", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/entity/living/player/PlayerEntity;stopRiding(Lnet/minecraft/entity/Entity;)V",
		shift = Shift.AFTER
	))
	private void bnb_stopRiding(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
		player.setPosition(x, y + 2.5, z);
		player.setVelocity(velocityX, velocityY, velocityZ);
	}
}
