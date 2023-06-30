package paulevs.bnb.mixin.client;

import net.minecraft.sortme.GameRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.rendering.FogInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Shadow private float field_2350;
	@Shadow float field_2346;
	@Shadow float field_2347;
	@Shadow float field_2348;
	
	@Inject(method = "method_1842", at = @At("HEAD"))
	private void bnb_changeFogColor(int i, float par2, CallbackInfo info) {
		FogInfo.setColor(0.2F, 0.03F, 0.03F);
		this.field_2346 = FogInfo.COLOR[0];
		this.field_2347 = FogInfo.COLOR[1];
		this.field_2348 = FogInfo.COLOR[2];
	}
	
	@Inject(method = "method_1842", at = @At(
		value = "INVOKE",
		target = "Lorg/lwjgl/opengl/GL11;glFogf(IF)V",
		ordinal = 7,
		shift = Shift.AFTER
	))
	private void bnb_changeNetherFog(int i, float par2, CallbackInfo info) {
		GL11.glFogf(GL11.GL_FOG_START, this.field_2350 * 0.5F);
		GL11.glFogf(GL11.GL_FOG_END, this.field_2350);
	}
}
