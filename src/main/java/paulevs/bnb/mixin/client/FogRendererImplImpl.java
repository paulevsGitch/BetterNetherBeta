package paulevs.bnb.mixin.client;

import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.impl.worldgen.FogRendererImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.weather.BNBWeatherRenderer;

@Mixin(value = FogRendererImpl.class, remap = false)
public class FogRendererImplImpl {
	@Shadow @Final private static float[] FOG_COLOR;
	
	@Inject(method = "setupFog", at = @At("RETURN"))
	private static void bnb_correctFog(Minecraft minecraft, float delta, CallbackInfo info) {
		if (minecraft.level == null || minecraft.level.dimension.id != -1) return;
		BNBWeatherRenderer.updateFog(FOG_COLOR);
	}
}
