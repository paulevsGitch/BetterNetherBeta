package paulevs.bnb.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.BNBClient;
import paulevs.bnb.block.property.BNBBlockMaterials;
import paulevs.bnb.rendering.BNBWeatherRenderer;

@Mixin(value = GameRenderer.class, priority = 500)
public class GameRendererMixin {
	@Shadow private Minecraft minecraft;
	@Shadow private float fogDistance;
	
	@Inject(method = "setupFog(IF)V", at = @At(
		value = "INVOKE",
		target = "Lorg/lwjgl/opengl/GL11;glFogf(IF)V",
		remap = false,
		ordinal = 7,
		shift = Shift.AFTER
	))
	private void bnb_changeNetherFog(int i, float par2, CallbackInfo info) {
		BNBClient.updateDepthBlend();
		if (minecraft.level == null || this.minecraft.level.dimension.id != -1) return;
		if (minecraft.viewEntity.isInFluid(BNBBlockMaterials.SULPHURIC_ACID)) {
			GL11.glFogf(GL11.GL_FOG_START, 0.5F);
			GL11.glFogf(GL11.GL_FOG_END, 15.0F);
		}
		else {
			float fog = BNBWeatherRenderer.getFogDensity();
			float fogStart = fogDistance * 0.5F * fog;
			float fogEnd = fogDistance * fog;
			float depthBlend = BNBClient.getDepthBlend();
			if (depthBlend > 0.0F) {
				fogStart = MathHelper.lerp(depthBlend, fogStart, Math.min(fogStart, 10.0F));
				fogEnd = MathHelper.lerp(depthBlend, fogEnd, fogEnd * 0.75F);
			}
			GL11.glFogf(GL11.GL_FOG_START, fogStart);
			GL11.glFogf(GL11.GL_FOG_END, fogEnd);
		}
	}
	
	@Inject(method = "renderWeather", at = @At("HEAD"))
	private void bnb_renderWeather(float delta, CallbackInfo info) {
		if (minecraft.level == null || minecraft.level.dimension.id != -1) return;
		BNBWeatherRenderer.render(minecraft, delta);
	}
}
