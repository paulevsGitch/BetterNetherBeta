package paulevs.bnb.mixin.server;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.world.generator.BNBWorldGenerator;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@Inject(method = "stopRunning", at = @At("HEAD"))
	private void bnb_onExit(CallbackInfo info) {
		BNBWorldGenerator.stop();
	}
}
