package paulevs.bnb.sound;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.sound.SoundEntry;
import paulscode.sound.SoundSystem;

import java.util.Random;

public class BNBSoundManager {
	private static final String BNB_STREAMING_KEY = "streaming";
	private static final String BNB_MUSIC_KEY = "BgMusic";
	private static final Random RANDOM = new Random();
	
	private static boolean inTheNether = false;
	private static int musicCountdown = 0;
	private static GameOptions gameOptions;
	private static SoundSystem soundSystem;
	
	public static void setInTheNether(boolean inTheNether) {
		if (BNBSoundManager.inTheNether != inTheNether) {
			soundSystem.stop(BNB_MUSIC_KEY);
		}
		BNBSoundManager.inTheNether = inTheNether;
	}
	
	public static void init(GameOptions gameOptions, SoundSystem soundSystem) {
		BNBSoundManager.gameOptions = gameOptions;
		BNBSoundManager.soundSystem = soundSystem;
	}
	
	public static void playBackgroundMusic() {
		if (gameOptions.music == 0.0f) return;
		Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
		if (minecraft == null) return;
		
		if (inTheNether && (minecraft.level == null || minecraft.level.dimension.id != -1)) {
			soundSystem.stop(BNB_MUSIC_KEY);
			inTheNether = false;
			musicCountdown = 0;
			return;
		}
		
		inTheNether = minecraft.level != null && minecraft.level.dimension.id == -1;
		if (!inTheNether) return;
		
		if (musicCountdown > 150) {
			musicCountdown = 50 + RANDOM.nextInt(100);
		}
		
		if (!soundSystem.playing(BNB_MUSIC_KEY) && !soundSystem.playing(BNB_STREAMING_KEY)) {
			if (--musicCountdown > 0) return;
			SoundEntry soundEntry = NetherSounds.getRandomMusic(RANDOM);
			musicCountdown = 50 + RANDOM.nextInt(100);
			soundSystem.backgroundMusic(BNB_MUSIC_KEY, soundEntry.soundUrl, soundEntry.soundName, false);
			soundSystem.setVolume(BNB_MUSIC_KEY, gameOptions.music * 0.25F);
			soundSystem.play(BNB_MUSIC_KEY);
		}
	}
	
	public static boolean updateMusicVolume() {
		if (!inTheNether || gameOptions.music == 0.0f) return false;
		soundSystem.setVolume(BNB_MUSIC_KEY, gameOptions.music * 0.25F);
		return true;
	}
}
