package paulevs.bnb.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Item;
import paulevs.bnb.util.BlockUtil;

@Mixin(value = ItemRenderer.class, priority = 100)
public class ItemRendererMixin {
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void bnb_render(Item arg, double d, double d1, double d2, float f, float f1, CallbackInfo info) {
		BlockUtil.setItemRender(true);
	}
	
	@Inject(method = "method_1486", at = @At("HEAD"), cancellable = true)
	private void renderModel(TextRenderer textRenderer, TextureManager textureManager, int id, int meta, int textureID, int x, int y, CallbackInfo info) {
		BlockUtil.setItemRender(true);
	}
}
