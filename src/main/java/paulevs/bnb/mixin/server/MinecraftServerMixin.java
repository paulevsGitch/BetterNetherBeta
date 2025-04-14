package paulevs.bnb.mixin.server;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.world.BNBWorldGenerator;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@Inject(method = "stopRunning", at = @At("HEAD"))
	private void bnb_onExit(CallbackInfo info) {
		BNBWorldGenerator.stop();
	}
	
	@Inject(method = "run", at = @At(
		value = "INVOKE",
		target = "Ljava/lang/System;currentTimeMillis()J",
		ordinal = 1,
		shift = Shift.AFTER,
		remap = false
	), remap = false)
	private void bnb_onServerTick(CallbackInfo info) {
		BNBWorldGenerator.tick(MinecraftServer.class.cast(this));
	}
}
