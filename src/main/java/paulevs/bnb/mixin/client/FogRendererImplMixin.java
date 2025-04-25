package paulevs.bnb.mixin.client;

import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.worldgen.FogRendererImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.BNBClient;
import paulevs.bnb.block.property.BNBBlockMaterials;
import paulevs.bnb.rendering.BNBWeatherRenderer;
import paulevs.bnb.util.ColorUtil;
import paulevs.bnb.world.biome.BNBBiomes;

@Mixin(value = FogRendererImpl.class, remap = false)
public class FogRendererImplMixin {
	@Unique private static float bnb_lastLight;
	@Unique private static float bnb_nextLight;
	@Unique private static float bnb_delta;
	
	@Shadow @Final private static float[] FOG_COLOR;
	
	@Inject(method = "setupFog", at = @At(value = "RETURN", ordinal = 0))
	private static void bnb_correctFog(Minecraft minecraft, float delta, CallbackInfo info) {
		if (minecraft.level == null || minecraft.level.dimension.id != -1) return;
		
		if (minecraft.viewEntity.isInFluid(BNBBlockMaterials.SULPHURIC_ACID)) {
			if (bnb_delta == 0.0F) {
				bnb_lastLight = bnb_nextLight;
				bnb_nextLight = minecraft.viewEntity.getBrightnessAtEyes(delta);
			}
			
			float light = MathHelper.lerp(bnb_delta, bnb_lastLight, bnb_nextLight);
			
			FOG_COLOR[0] = 0.341F * light;
			FOG_COLOR[1] = 0.690F * light;
			FOG_COLOR[2] = 0.270F * light;
			
			bnb_delta = Math.min(bnb_delta + delta * 0.01F, 1.0F);
			if (bnb_delta == 1.0F) bnb_delta = 0.0F;
		}
		else if (minecraft.viewEntity.y < 80) {
			float depthBlend = BNBClient.getDepthBlend();
			int deepColor = BNBBiomes.DEEP_NETHER.getFogColor().getColor(
				minecraft.level.getBiomeSource(),
				(int) minecraft.viewEntity.x,
				(int) minecraft.viewEntity.z
			);
			FOG_COLOR[0] = MathHelper.lerp(depthBlend, FOG_COLOR[0], ColorUtil.getR(deepColor) / 255.0F);
			FOG_COLOR[1] = MathHelper.lerp(depthBlend, FOG_COLOR[1], ColorUtil.getG(deepColor) / 255.0F);
			FOG_COLOR[2] = MathHelper.lerp(depthBlend, FOG_COLOR[2], ColorUtil.getB(deepColor) / 255.0F);
		}
		
		BNBWeatherRenderer.updateFog(FOG_COLOR);
	}
}
