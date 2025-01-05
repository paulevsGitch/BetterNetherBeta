package paulevs.bnb.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.sound.SoundEntry;
import net.minecraft.client.sound.SoundHelper;
import net.minecraft.client.sound.SoundMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.BNBClient;
import paulevs.bnb.sound.BNBSoundManager;
import paulevs.bnb.sound.BNBWeatherSounds;
import paulscode.sound.SoundSystem;

import java.util.List;

@Mixin(SoundHelper.class)
public class SoundHelperMixin {
	@Shadow private static boolean initialized;
	@Shadow private GameOptions gameOptions;
	@Shadow private static SoundSystem soundSystem;
	@Shadow private SoundMap music;
	
	@Unique private boolean bnb_clearList = true;
	
	@Inject(method = "setLibsAndCodecs", at = @At("TAIL"))
	private void bnb_setLibsAndCodecs(CallbackInfo info) {
		BNBSoundManager.init(gameOptions, soundSystem);
	}
	
	@Inject(method = "handleBackgroundMusic", at = @At("HEAD"), cancellable = true)
	private void bnb_handleBackgroundMusic(CallbackInfo info) {
		if (!initialized) return;
		if (bnb_clearList) {
			bnb_clearList = false;
			@SuppressWarnings("rawtypes")
			List list = ((SoundMapAccessor) music).bnb_getSoundList();
			for (int i = 0; i < list.size(); i++) {
				SoundEntry sound = (SoundEntry) list.get(i);
				if (!sound.soundName.startsWith("bnb:")) continue;
				list.remove(i--);
			}
		}
		Minecraft minecraft = BNBClient.getMinecraft();
		boolean isNether = minecraft != null && minecraft.level != null && minecraft.level.dimension.id == -1;
		BNBSoundManager.setInTheNether(isNether);
		BNBWeatherSounds.setInTheNether(isNether);
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
