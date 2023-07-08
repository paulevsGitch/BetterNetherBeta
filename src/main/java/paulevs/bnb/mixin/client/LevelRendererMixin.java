package paulevs.bnb.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.LevelRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.rendering.SkyRenderer;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
	@Shadow private Level level;
	
	@Inject(method = "<init>", at = @At("TAIL"))
	private void bnb_onInit(Minecraft mc, TextureManager manager, CallbackInfo info) {
		SkyRenderer.init(manager);
	}
	
	@Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
	public void bnb_renderSky(float delta, CallbackInfo info) {
		if (this.level.dimension.id == -1) {
			SkyRenderer.renderSky();
			info.cancel();
		}
	}
}
