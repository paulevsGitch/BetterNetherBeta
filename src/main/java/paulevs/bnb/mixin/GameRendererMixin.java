package paulevs.bnb.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.sortme.GameRenderer;
import paulevs.bnb.particles.BiomeParticle;
import paulevs.bnb.util.ClientUtil;

@Mixin(value = GameRenderer.class, priority = 100)
public class GameRendererMixin {
	@Shadow
	private Minecraft minecraft;
	
	@Inject(method = "method_1846", at = @At("HEAD"))
	private void bnb_renderParticles(CallbackInfo info) {
		double x = minecraft.player.x + ClientUtil.getRandom().nextDouble() * 16 - 8;
		double y = minecraft.player.y + ClientUtil.getRandom().nextDouble() * 16 - 8;
		double z = minecraft.player.z + ClientUtil.getRandom().nextDouble() * 16 - 8;
		minecraft.particleManager.addParticle(new BiomeParticle(minecraft.level, x, y, z));
	}
}
