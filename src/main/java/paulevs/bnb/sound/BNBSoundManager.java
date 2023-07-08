package paulevs.bnb.sound;

import net.minecraft.client.options.GameOptions;
import net.minecraft.client.sound.SoundEntry;
import paulevs.bnb.world.biome.BNBBiomes;
import paulscode.sound.SoundSystem;

import java.util.Random;

public class BNBSoundManager {
	private static final String STREAMING_KEY = "streaming";
	private static final String AMBIENT_KEY = "BNBAmbience";
	private static final String MUSIC_KEY = "BgMusic";
	private static final Random RANDOM = new Random();
	
	private static boolean inTheNether = false;
	private static int musicCountdown = 0;
	private static GameOptions gameOptions;
	private static SoundSystem soundSystem;
	
	public static void setInTheNether(boolean inTheNether) {
		if (BNBSoundManager.inTheNether != inTheNether) {
			soundSystem.stop(MUSIC_KEY);
			soundSystem.stop(AMBIENT_KEY);
			musicCountdown = 0;
		}
		BNBSoundManager.inTheNether = inTheNether;
	}
	
	@SuppressWarnings("deprecation")
	public static void init(GameOptions gameOptions, SoundSystem soundSystem) {
		BNBSoundManager.gameOptions = gameOptions;
		BNBSoundManager.soundSystem = soundSystem;
	}
	
	public static void playBackgroundMusic() {
		if (gameOptions.music == 0.0f) return;
		if (soundSystem.playing(MUSIC_KEY) || soundSystem.playing(STREAMING_KEY)) return;
		if (--musicCountdown > 0) return;
		SoundEntry soundEntry = BNBSounds.getRandomMusic(RANDOM);
		musicCountdown = 50 + RANDOM.nextInt(100);
		soundSystem.backgroundMusic(MUSIC_KEY, soundEntry.soundUrl, soundEntry.soundName, false);
		soundSystem.setVolume(MUSIC_KEY, gameOptions.music * 0.25F);
		soundSystem.play(MUSIC_KEY);
	}
	
	public static boolean updateMusicVolume() {
		if (!inTheNether || gameOptions.music == 0.0f) return false;
		soundSystem.setVolume(MUSIC_KEY, gameOptions.music * 0.25F);
		return true;
	}
	
	public static void playAmbience() {
		if (gameOptions.sound == 0.0f) return;
		if (soundSystem.playing(AMBIENT_KEY) || soundSystem.playing(STREAMING_KEY)) return;
		SoundEntry soundEntry = BNBBiomes.CRIMSON_FOREST.getAmbientSound();
		soundSystem.backgroundMusic(AMBIENT_KEY, soundEntry.soundUrl, soundEntry.soundName, false);
		soundSystem.setVolume(AMBIENT_KEY, gameOptions.sound);
		soundSystem.play(AMBIENT_KEY);
	}
}
