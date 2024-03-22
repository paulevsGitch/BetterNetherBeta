package paulevs.bnb.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.menu.AchievementsScreen;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Shadow public volatile boolean paused;
	@Shadow public Screen currentScreen;
	@Shadow public TextureManager textureManager;
	
	@ModifyConstant(method = "switchDimension", constant = @Constant(doubleValue = 8.0))
	private double bnb_changeNetherScale(double value) {
		return 1.0;
	}
	
	@Inject(method = "tick", at = @At(
		value = "INVOKE",
		target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V",
		remap = false
	))
	private void bnb_animateTextures(CallbackInfo info) {
		if (paused && currentScreen instanceof AchievementsScreen) {
			textureManager.tick();
		}
	}
}
