package paulevs.bnb.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.sound.SoundHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.BNBClient;
import paulevs.bnb.sound.BNBSoundManager;
import paulevs.bnb.weather.BNBWeatherSounds;
import paulscode.sound.SoundSystem;

@Mixin(SoundHelper.class)
public class SoundHelperMixin {
	@Shadow private static boolean initialized;
	@Shadow private GameOptions gameOptions;
	@Shadow private static SoundSystem soundSystem;
	
	@Inject(method = "setLibsAndCodecs", at = @At("TAIL"))
	private void bnb_setLibsAndCodecs(CallbackInfo info) {
		BNBSoundManager.init(gameOptions, soundSystem);
	}
	
	@Inject(method = "handleBackgroundMusic", at = @At("HEAD"), cancellable = true)
	private void bnb_handleBackgroundMusic(CallbackInfo info) {
		if (!initialized) return;
		Minecraft minecraft = BNBClient.getMinecraft();
		boolean isNether = minecraft != null && minecraft.level != null && minecraft.level.dimension.id == -1;
		BNBSoundManager.setInTheNether(isNether);
		if (isNether) {
			BNBSoundManager.playBackgroundMusic();
			BNBSoundManager.playAmbience(minecraft.player, minecraft.level.dimension.biomeSource);
			BNBWeatherSounds.updateSound(minecraft.level, minecraft.player, soundSystem, gameOptions.sound);
			info.cancel();
		}
	}
	
	@Inject(method = "updateMusicVolume", at = @At("HEAD"), cancellable = true)
	public void bnb_updateMusicVolume(CallbackInfo info) {
		if (BNBSoundManager.updateMusicVolume()) info.cancel();
	}
}
