package paulevs.bnb.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.menu.AchievementsScreen;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.particle.BNBParticleManager;
import paulevs.bnb.rendering.BNBWeatherRenderer;
import paulevs.bnb.world.generator.BNBWorldGenerator;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Shadow public volatile boolean paused;
	@Shadow public Screen currentScreen;
	@Shadow public TextureManager textureManager;
	@Shadow public Level level;
	
	@Shadow public LivingEntity viewEntity;
	
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
		if (paused && currentScreen instanceof AchievementsScreen) textureManager.tick();
		if (viewEntity != null && level != null && level.dimension.id == -1) {
			Minecraft minecraft = Minecraft.class.cast(this);
			BNBParticleManager.tick(minecraft);
			BNBWorldGenerator.tick(minecraft);
		}
	}
	
	@Inject(method = "scheduleStop", at = @At("HEAD"))
	private void bnb_onExit(CallbackInfo info) {
		BNBWorldGenerator.stop();
		BNBWeatherRenderer.stop();
	}
}
