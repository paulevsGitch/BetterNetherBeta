package paulevs.bnb.mixin.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.sortme.GameRenderer;
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
	@Shadow private float field_2350;
	@Shadow float field_2346;
	@Shadow float field_2347;
	@Shadow float field_2348;
	
	@Inject(method = "method_1842", at = @At("HEAD"))
	private void bnb_changeFogColor(int i, float par2, CallbackInfo info) {
		if (this.minecraft.level.dimension.id != -1) return;
		FogInfo.setColor(0.2F, 0.03F, 0.03F);
		this.field_2346 = FogInfo.COLOR[0];
		this.field_2347 = FogInfo.COLOR[1];
		this.field_2348 = FogInfo.COLOR[2];
	}
	
	@Inject(method = "method_1852", at = @At(
		value = "INVOKE",
		target = "Lorg/lwjgl/opengl/GL11;glClearColor(FFFF)V",
		shift = Shift.AFTER
	))
	private void method_1852(float f, CallbackInfo info) {
		if (this.minecraft.level.dimension.id != -1) return;
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(
			FogInfo.COLOR[0],
			FogInfo.COLOR[1],
			FogInfo.COLOR[2],
			1F
		);
	}
	
	@Inject(method = "method_1842", at = @At(
		value = "INVOKE",
		target = "Lorg/lwjgl/opengl/GL11;glFogf(IF)V",
		ordinal = 7,
		shift = Shift.AFTER
	))
	private void bnb_changeNetherFog(int i, float par2, CallbackInfo info) {
		if (this.minecraft.level.dimension.id != -1) return;
		if (BNB_FARVIEW) {
			GL11.glFogf(GL11.GL_FOG_START, 64F);
			GL11.glFogf(GL11.GL_FOG_END, 375F);
			return;
		}
		GL11.glFogf(GL11.GL_FOG_START, this.field_2350 * 0.5F);
		GL11.glFogf(GL11.GL_FOG_END, this.field_2350);
	}
}
