package paulevs.bnb.mixin.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.rendering.FogInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Unique private static final boolean BNB_FARVIEW = FabricLoader.getInstance().isModLoaded("farview");
	@Shadow private Minecraft minecraft;
	@Shadow private float fogDistance;
	@Shadow float fogColorR;
	@Shadow float fogColorG;
	@Shadow float fogColorB;
	
	@Inject(method = "setupFog", at = @At("HEAD"))
	private void bnb_changeFogColor(int i, float par2, CallbackInfo info) {
		if (minecraft.level.dimension.id != -1) return;
		FogInfo.setColor(0.2F, 0.03F, 0.03F);
		fogColorR = FogInfo.COLOR[0];
		fogColorG = FogInfo.COLOR[1];
		fogColorB = FogInfo.COLOR[2];
	}
	
	@Inject(method = "renderFog", at = @At(
		value = "INVOKE",
		target = "Lorg/lwjgl/opengl/GL11;glClearColor(FFFF)V",
		shift = Shift.AFTER
	))
	private void method_1852(float f, CallbackInfo info) {
		if (minecraft.level.dimension.id != -1) return;
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(
			FogInfo.COLOR[0],
			FogInfo.COLOR[1],
			FogInfo.COLOR[2],
			1F
		);
	}
	
	@Inject(method = "setupFog", at = @At(
		value = "INVOKE",
		target = "Lorg/lwjgl/opengl/GL11;glFogf(IF)V",
		ordinal = 7,
		shift = Shift.AFTER
	))
	private void bnb_changeNetherFog(int i, float par2, CallbackInfo info) {
		if (this.minecraft.level.dimension.id != -1) return;
		if (BNB_FARVIEW) {
			float delta = minecraft.options.viewDistance / 3F;
			float fogStart = MathHelper.lerp(delta, 64F, 0F);
			float fogEnd = MathHelper.lerp(delta, 375F, 64F);
			GL11.glFogf(GL11.GL_FOG_START, fogStart);
			GL11.glFogf(GL11.GL_FOG_END, fogEnd);
			return;
		}
		GL11.glFogf(GL11.GL_FOG_START, fogDistance * 0.5F);
		GL11.glFogf(GL11.GL_FOG_END, fogDistance);
	}
}
