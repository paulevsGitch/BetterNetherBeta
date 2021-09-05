package paulevs.bnb.mixin.client;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Item;
import net.minecraft.item.ItemBase;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationloader.api.client.texture.TextureRegistry;
import net.modificationstation.stationloader.api.client.texture.TextureRegistry.Vanilla;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.item.SpawnEggItem;

import java.util.Random;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin extends EntityRenderer {
	@Shadow
	private Random rand;
	
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void bnb_renderSpawnEggWorld(Item arg, double d, double d1, double d2, float f, float f1, CallbackInfo info) {
		if (ItemBase.byId[arg.item.itemId] instanceof SpawnEggItem) {
			SpawnEggItem egg = (SpawnEggItem) ItemBase.byId[arg.item.itemId];
			int damage = arg.item.getDamage();
			
			this.rand.setSeed(187L);
			GL11.glPushMatrix();
			float var11 = MathHelper.sin(((float)arg.age + f1) / 10.0F + arg.field_567) * 0.1F + 0.1F;
			int count = 1;
			if (arg.item.count > 1) {
				count = 2;
			}
			
			if (arg.item.count > 5) {
				count = 3;
			}
			
			if (arg.item.count > 20) {
				count = 4;
			}
			
			GL11.glTranslatef((float)d, (float)d1 + var11, (float)d2);
			GL11.glEnable(32826);
			
			int color = egg.getBackColor(damage);
			int texture = egg.getTexturePosition(damage);
			TextureRegistry.getRegistry(Vanilla.GUI_ITEMS).bindAtlas(this.dispatcher.textureManager, texture >> 8);
			float brightness = arg.getBrightnessAtEyes(f1);
			float cr = (color >> 16 & 255) / 255.0F * brightness;
			float cg = (color >> 8 & 255) / 255.0F * brightness;
			float cb = (color & 255) / 255.0F * brightness;
			GL11.glColor4f(cr, cg, cb, 1.0F);
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			Tessellator tessellator = Tessellator.INSTANCE;
			
			float u1 = ((texture & 15) << 4) / 256.0F;
			float u2 = u1 + 16 / 256.0F;
			float v1 = ((texture >> 4) << 4) / 256.0F;
			float v2 = v1 + 16 / 256.0F;
			
			GL11.glPushMatrix();
			GL11.glRotatef(180.0F - this.dispatcher.field_2497, 0.0F, 1.0F, 0.0F);
			tessellator.start();
			tessellator.method_1697(0.0F, 1.0F, 0.0F);
			tessellator.vertex(-0.5F, -0.25F, 0.0D, u1, v2);
			tessellator.vertex(0.5F, -0.25F, 0.0D, u2, v2);
			tessellator.vertex(0.5F, 0.75F, 0.0D, u2, v1);
			tessellator.vertex(-0.5F, 0.75F, 0.0D, u1, v1);
			tessellator.draw();
			GL11.glPopMatrix();
			
			color = egg.getDotsColor(damage);
			texture = egg.getOverlayTexturePosition(damage);
			TextureRegistry.getRegistry(Vanilla.GUI_ITEMS).bindAtlas(this.dispatcher.textureManager, texture >> 8);
			cr = (color >> 16 & 255) / 255.0F * brightness;
			cg = (color >> 8 & 255) / 255.0F * brightness;
			cb = (color & 255) / 255.0F * brightness;
			GL11.glColor4f(cr, cg, cb, 1.0F);
			u1 = ((texture & 15) << 4) / 256.0F;
			u2 = u1 + 16 / 256.0F;
			v1 = ((texture >> 4) << 4) / 256.0F;
			v2 = v1 + 16 / 256.0F;
			
			GL11.glPushMatrix();
			GL11.glRotatef(180.0F - this.dispatcher.field_2497, 0.0F, 1.0F, 0.0F);
			tessellator.start();
			tessellator.method_1697(0.0F, 1.0F, 0.0F);
			tessellator.vertex(-0.5F, -0.25F, 0.0D, u1, v2);
			tessellator.vertex(0.5F, -0.25F, 0.0D, u2, v2);
			tessellator.vertex(0.5F, 0.75F, 0.0D, u2, v1);
			tessellator.vertex(-0.5F, 0.75F, 0.0D, u1, v1);
			tessellator.draw();
			GL11.glPopMatrix();
			
			GL11.glDisable(32826);
			GL11.glPopMatrix();
			info.cancel();
		}
	}
	
	@Inject(method = "method_1486", at = @At("HEAD"), cancellable = true)
	private void bnb_renderSpawnEggGUI(TextRenderer textRenderer, TextureManager manager, int itemID, int damage, int texture, int l, int m, CallbackInfo info) {
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
