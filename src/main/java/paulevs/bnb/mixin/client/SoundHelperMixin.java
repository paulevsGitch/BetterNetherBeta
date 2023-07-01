package paulevs.bnb.mixin.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.sound.SoundEntry;
import net.minecraft.client.sound.SoundHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.sound.NetherSounds;
import paulscode.sound.SoundSystem;

import java.util.Random;

@Mixin(SoundHelper.class)
public class SoundHelperMixin {
	@Unique private static final String BNB_STREAMING_KEY = "streaming";
	@Unique private static final String BNB_MUSIC_KEY = "BgMusic";
	@Unique private boolean bnb_isNether;
	
	@Shadow private static boolean initialized;
	@Shadow private GameOptions gameOptions;
	@Shadow private static SoundSystem soundSystem;
	@Shadow private int musicCountdown;
	
	@Shadow private Random rand;
	
	@SuppressWarnings("deprecated")
	@Inject(method = "handleBackgroundMusic", at = @At("HEAD"), cancellable = true)
	private void bnb_handleBackgroundMusic(CallbackInfo info) {
		if (!initialized || gameOptions.music == 0.0f) return;
		Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
		if (minecraft == null) return;
		
		info.cancel();
		
		if (bnb_isNether && (minecraft.level == null || minecraft.level.dimension.id != -1)) {
			soundSystem.stop(BNB_MUSIC_KEY);
			bnb_isNether = false;
			musicCountdown = 0;
			return;
		}
		
		bnb_isNether = minecraft.level != null && minecraft.level.dimension.id == -1;
		if (!bnb_isNether) return;
		
		if (musicCountdown > 150) {
			musicCountdown = 50 + rand.nextInt(100);
		}
		
		if (!soundSystem.playing(BNB_MUSIC_KEY) && !soundSystem.playing(BNB_STREAMING_KEY)) {
			if (--musicCountdown > 0) return;
			SoundEntry soundEntry = NetherSounds.MUSIC[rand.nextInt(NetherSounds.MUSIC.length)];
			this.musicCountdown = 50 + rand.nextInt(100);
			soundSystem.backgroundMusic(BNB_MUSIC_KEY, soundEntry.soundUrl, soundEntry.soundName, false);
			soundSystem.setVolume(BNB_MUSIC_KEY, this.gameOptions.music * 0.25F);
			soundSystem.play(BNB_MUSIC_KEY);
		}
	}
	
	@Inject(method = "updateMusicVolume", at = @At("HEAD"), cancellable = true)
	public void bnb_updateMusicVolume(CallbackInfo info) {
		if (!bnb_isNether || !initialized || gameOptions.music == 0.0f) return;
		soundSystem.setVolume(BNB_MUSIC_KEY, this.gameOptions.music * 0.25F);
		info.cancel();
	}
}
