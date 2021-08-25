package paulevs.bnb.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.InGame;
import net.minecraft.client.util.ScreenScaler;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.effects.AdditionalHealthEffect;
import paulevs.bnb.effects.StatusEffect;
import paulevs.bnb.interfaces.StatusEffectable;
import paulevs.bnb.util.CreativeUtil;

@Mixin(InGame.class)
public class InGameMixin extends DrawableHelper {
	private static int bnb_textureID = -1;
	
	@Shadow
	private Minecraft minecraft;
	
	@Inject(method = "renderHud", at = @At("TAIL"))
	private void bnb_renderHud(float f, boolean flag, int i, int j, CallbackInfo info) {
		if (CreativeUtil.isInCreative(this.minecraft.player)) {
			return;
		}
		
		StatusEffect effect = ((StatusEffectable) this.minecraft.player).getEffect("additional_health");
		if (effect == null) {
			return;
		}
		int health = ((AdditionalHealthEffect) effect).getHealth();
		double delta = effect.getDelta();
		boolean isFading = delta > 0.75;
		
		ScreenScaler scaler = new ScreenScaler(this.minecraft.options, this.minecraft.actualWidth, this.minecraft.actualHeight);
		int width = scaler.getScaledWidth();
		int height = scaler.getScaledHeight();
		
		
		if (bnb_textureID < 0) {
			bnb_textureID = this.minecraft.textureManager.getTextureId("/assets/" + BetterNetherBeta.MOD_ID + "/textures/icons.png");
		}
		if (isFading) {
			delta = (delta - 0.75) / 0.25;
			delta = Math.cos(delta * Math.PI * 13) * 0.5 + 0.5;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, (float) delta);
		}
		else {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, bnb_textureID);
		
		int h = 0;
		for (h = 0; h < 5; h++) {
			int posY = height - 32 - 10;
			int posX = width / 2 - 91 + h * 8;
			this.blit(posX, posY, 0, 0, 9, 9);
		}
		
		int max = health >> 1;
		for (h = 0; h < max; h++) {
			int posY = height - 32 - 10;
			int posX = width / 2 - 91 + h * 8;
			this.blit(posX, posY, 9, 0, 9, 9);
		}
		
		if (health > 0 && (health & 1) == 1) {
			int posY = height - 32 - 10;
			int posX = width / 2 - 91 + h * 8;
			this.blit(posX, posY, 18, 0, 9, 9);
		}
		
		if (isFading) {
			GL11.glDisable(GL11.GL_BLEND);
		}
	}
}
