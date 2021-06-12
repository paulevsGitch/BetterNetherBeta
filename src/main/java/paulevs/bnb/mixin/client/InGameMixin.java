package paulevs.bnb.mixin.client;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.InGame;
import net.minecraft.client.util.ScreenScaler;
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
		
		ScreenScaler scaler = new ScreenScaler(this.minecraft.options, this.minecraft.actualWidth, this.minecraft.actualHeight);
		int width = scaler.getScaledWidth();
		int height = scaler.getScaledHeight();
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (bnb_textureID < 0) {
			bnb_textureID = this.minecraft.textureManager.getTextureId("/assets/" + BetterNetherBeta.MOD_ID + "/textures/icons.png");
			System.out.println(bnb_textureID);
		}
		GL11.glBindTexture(3553, bnb_textureID);
		
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
		
		if ((health & 1) == 1) {
			int posY = height - 32 - 10;
			int posX = width / 2 - 91 + h * 8;
			this.blit(posX, posY, 18, 0, 9, 9);
		}
	}
}
