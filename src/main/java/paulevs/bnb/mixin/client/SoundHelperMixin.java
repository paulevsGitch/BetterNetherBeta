package paulevs.bnb.mixin.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.sound.SoundHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.sound.BNBSoundManager;
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
		@SuppressWarnings("deprecated")
		Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
		boolean isNether = minecraft != null && minecraft.level != null && minecraft.level.dimension.id == -1;
		BNBSoundManager.setInTheNether(isNether);
		if (isNether) {
			BNBSoundManager.playBackgroundMusic();
			BNBSoundManager.playAmbience(minecraft.player, minecraft.level.dimension.biomeSource);
			info.cancel();
		}
	}
	
	@Inject(method = "updateMusicVolume", at = @At("HEAD"), cancellable = true)
	public void bnb_updateMusicVolume(CallbackInfo info) {
		if (BNBSoundManager.updateMusicVolume()) info.cancel();
	}
}
