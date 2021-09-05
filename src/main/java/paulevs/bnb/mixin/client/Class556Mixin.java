package paulevs.bnb.mixin.client;

import net.minecraft.class_556;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationloader.api.client.texture.TextureRegistry;
import net.modificationstation.stationloader.api.client.texture.TextureRegistry.Vanilla;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.item.SpawnEggItem;

@Mixin(class_556.class)
public class Class556Mixin {
	@Shadow
	private Minecraft field_2401;
	
	@Inject(method = "method_1862", at = @At("HEAD"), cancellable = true)
	private void method_1862(Living entity, ItemInstance itemInstance, CallbackInfo info) {
		if (ItemBase.byId[itemInstance.itemId] instanceof SpawnEggItem) {
			SpawnEggItem egg = (SpawnEggItem) ItemBase.byId[itemInstance.itemId];
			int damage = itemInstance.getDamage();
			
			int color = egg.getBackColor(damage);
			float cr = (color >> 16 & 255) / 255.0F;
			float cg = (color >> 8 & 255) / 255.0F;
			float cb = (color & 255) / 255.0F;
			GL11.glColor4f(cr, cg, cb, 1.0F);
			bnb_renderItemLayer(egg.getTexturePosition(damage));
			
			color = egg.getDotsColor(damage);
			cr = (color >> 16 & 255) / 255.0F;
			cg = (color >> 8 & 255) / 255.0F;
			cb = (color & 255) / 255.0F;
			GL11.glColor4f(cr, cg, cb, 1.0F);
			bnb_renderItemLayer(egg.getOverlayTexturePosition(damage));
			
			info.cancel();
		}
	}
	
	private void bnb_renderItemLayer(int texture) {
		GL11.glPushMatrix();
		
		Tessellator tessellator = Tessellator.INSTANCE;
		TextureRegistry.getRegistry(Vanilla.GUI_ITEMS).bindAtlas(field_2401.textureManager, texture >> 8);
		
		float u1 = (((texture & 15) << 4)) / 256.0F;
		float u2 = (((texture & 15) << 4) + 15.99F) / 256.0F;
		float v1 = (((texture >> 4) << 4)) / 256.0F;
		float v2 = (((texture >> 4) << 4) + 15.99F) / 256.0F;
		
		GL11.glEnable(32826);
		GL11.glTranslatef(0.0F, -0.3F, 0.0F);
		GL11.glScalef(1.5F, 1.5F, 1.5F);
		GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
		GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
		
		tessellator.start();
		tessellator.method_1697(0.0F, 0.0F, 1.0F);
		tessellator.vertex(0.0D, 0.0D, 0.0D, u2, v2);
		tessellator.vertex(1.0F, 0.0D, 0.0D, u1, v2);
		tessellator.vertex(1.0F, 1.0D, 0.0D, u1, v1);
		tessellator.vertex(0.0D, 1.0D, 0.0D, u2, v1);
		tessellator.draw();
		
		tessellator.start();
		tessellator.method_1697(0.0F, 0.0F, -1.0F);
		tessellator.vertex(0.0D, 1.0D, 0.0F - 0.0625F, u2, v1);
		tessellator.vertex(1.0F, 1.0D, 0.0F - 0.0625F, u1, v1);
		tessellator.vertex(1.0F, 0.0D, 0.0F - 0.0625F, u1, v2);
		tessellator.vertex(0.0D, 0.0D, 0.0F - 0.0625F, u2, v2);
		tessellator.draw();
		
		tessellator.start();
		tessellator.method_1697(-1.0F, 0.0F, 0.0F);
		
		for(int var14 = 0; var14 < 16; ++var14) {
			float py = var14 / 16.0F;
			float u = u2 + (u1 - u2) * py - 0.001953125F;
			tessellator.vertex(py, 0.0D, 0.0F - 0.0625F, u, v2);
			tessellator.vertex(py, 0.0D, 0.0D, u, v2);
			tessellator.vertex(py, 1.0D, 0.0D, u, v1);
			tessellator.vertex(py, 1.0D, 0.0F - 0.0625F, u, v1);
		}
		
		tessellator.draw();
		
		tessellator.start();
		tessellator.method_1697(1.0F, 0.0F, 0.0F);
		
		for(int i = 0; i < 16; ++i) {
			float delta = i / 16.0F;
			float u = u2 + (u1 - u2) * delta - 0.001953125F;
			float py = delta + 0.0625F;
			tessellator.vertex(py, 1.0D, 0.0F - 0.0625F, u, v1);
			tessellator.vertex(py, 1.0D, 0.0D, u, v1);
			tessellator.vertex(py, 0.0D, 0.0D, u, v2);
			tessellator.vertex(py, 0.0D, 0.0F - 0.0625F, u, v2);
		}
		
		tessellator.draw();
		
		tessellator.start();
		tessellator.method_1697(0.0F, 1.0F, 0.0F);
		
		for(int i = 0; i < 16; ++i) {
			float delta = i / 16.0F;
			float v = v2 + (v1 - v2) * delta - 0.001953125F;
			float py = delta + 0.0625F;
			tessellator.vertex(0.0D, py, 0.0D, u2, v);
			tessellator.vertex(1.0F, py, 0.0D, u1, v);
			tessellator.vertex(1.0F, py, 0.0F - 0.0625F, u1, v);
			tessellator.vertex(0.0D, py, 0.0F - 0.0625F, u2, v);
		}
		
		tessellator.draw();
		
		tessellator.start();
		tessellator.method_1697(0.0F, -1.0F, 0.0F);
		
		for(int i = 0; i < 16; ++i) {
			float py = i / 16.0F;
			float v = v2 + (v1 - v2) * py - 0.001953125F;
			tessellator.vertex(1.0F, py, 0.0D, u1, v);
			tessellator.vertex(0.0D, py, 0.0D, u2, v);
			tessellator.vertex(0.0D, py, 0.0F - 0.0625F, u2, v);
			tessellator.vertex(1.0F, py, 0.0F - 0.0625F, u1, v);
		}
		
		tessellator.draw();
		GL11.glDisable(32826);
		
		GL11.glPopMatrix();
	}
}
