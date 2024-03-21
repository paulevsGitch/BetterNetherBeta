package paulevs.bnb.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.weather.BNBWeatherRenderer;

@Mixin(GameRenderer.class)
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
		if (this.minecraft.level.dimension.id != -1) return;
		float fog = BNBWeatherRenderer.getFogDensity();
		GL11.glFogf(GL11.GL_FOG_START, fogDistance * 0.5F * fog);
		GL11.glFogf(GL11.GL_FOG_END, fogDistance * fog);
	}
	
	@Inject(method = "renderWeather", at = @At("HEAD"))
	private void bnb_renderWeather(float delta, CallbackInfo info) {
		BNBWeatherRenderer.render(minecraft, delta);
	}
}
