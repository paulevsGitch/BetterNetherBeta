package paulevs.bnb.mixin.client;

import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationloader.api.client.texture.TextureRegistry;
import net.modificationstation.stationloader.api.client.texture.TextureRegistry.Vanilla;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.item.SpawnEggItem;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin extends EntityRenderer {
	@Inject(method = "method_1486", at = @At("HEAD"), cancellable = true)
	private void bnb_renderSpawnEgg(TextRenderer textRenderer, TextureManager manager, int itemID, int damage, int texture, int l, int m, CallbackInfo info) {
		if (ItemBase.byId[itemID] instanceof SpawnEggItem) {
			SpawnEggItem egg = (SpawnEggItem) ItemBase.byId[itemID];
			GL11.glDisable(2896);
			
			int color = egg.getBackColor(damage);
			texture = egg.getTexturePosition(damage);
			TextureRegistry.getRegistry(Vanilla.GUI_ITEMS).bindAtlas(manager, texture >> 8);
			float cr = (color >> 16 & 255) / 255.0F;
			float cg = (color >> 8 & 255) / 255.0F;
			float cb = (color & 255) / 255.0F;
			GL11.glColor4f(cr, cg, cb, 1.0F);
			this.method_1483(l, m, (texture & 15) << 4, (texture >> 4) << 4, 16, 16);
			
			color = egg.getDotsColor(damage);
			texture = egg.getOverlayTexturePosition(damage);
			TextureRegistry.getRegistry(Vanilla.GUI_ITEMS).bindAtlas(manager, texture >> 8);
			cr = (color >> 16 & 255) / 255.0F;
			cg = (color >> 8 & 255) / 255.0F;
			cb = (color & 255) / 255.0F;
			GL11.glColor4f(cr, cg, cb, 1.0F);
			this.method_1483(l, m, (texture & 15) << 4, (texture >> 4) << 4, 16, 16);
			
			GL11.glEnable(2896);
			GL11.glEnable(2884);
			info.cancel();
		}
	}
	
	@Shadow
	public void method_1483(int i, int j, int k, int i1, int j1, int k1) {}
}
