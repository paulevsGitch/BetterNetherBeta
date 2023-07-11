package paulevs.bnb.sound;

import net.minecraft.client.options.GameOptions;
import net.minecraft.client.sound.SoundEntry;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.gen.BiomeSource;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.bnb.world.biome.NetherBiome;
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
	
	private static Identifier currentAmbientSoundID;
	private static Identifier nextAmbientSoundID;
	private static String currentAmbientKey;
	private static String nextAmbientKey;
	private static int ambientTicks = 10;
	private static SoundState state;
	
	public static void setInTheNether(boolean inTheNether) {
		if (BNBSoundManager.inTheNether != inTheNether) {
			soundSystem.stop(MUSIC_KEY);
			if (soundSystem.playing(currentAmbientKey)) soundSystem.stop(currentAmbientKey);
			if (soundSystem.playing(nextAmbientKey)) soundSystem.stop(nextAmbientKey);
			currentAmbientSoundID = null;
			nextAmbientSoundID = null;
			musicCountdown = 50;
		}
		BNBSoundManager.inTheNether = inTheNether;
	}
	
	public static void init(GameOptions gameOptions, SoundSystem soundSystem) {
		BNBSoundManager.gameOptions = gameOptions;
		BNBSoundManager.soundSystem = soundSystem;
	}
	
	public static void playBackgroundMusic() {
		if (gameOptions.music == 0.0f) return;
		if (soundSystem.playing(MUSIC_KEY) || soundSystem.playing(STREAMING_KEY)) return;
		if (--musicCountdown > 0) return;
		SoundEntry soundEntry = BNBClientSounds.getRandomMusic(RANDOM);
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
	
	public static void playAmbience(PlayerBase player, BiomeSource biomeSource) {
		if (gameOptions.sound == 0.0f) {
			if (soundSystem.playing(currentAmbientKey)) soundSystem.stop(currentAmbientKey);
			if (soundSystem.playing(nextAmbientKey)) soundSystem.stop(nextAmbientKey);
			return;
		}
		
		if (ambientTicks == 81) {
			currentAmbientSoundID = nextAmbientSoundID;
			nextAmbientSoundID = getSound(player, biomeSource);
			
			if (nextAmbientSoundID == null) {
				if (currentAmbientSoundID != null) state = SoundState.FADE_DOWN;
				else state = null;
			}
			else {
				if (currentAmbientSoundID != null) {
					if (currentAmbientSoundID == nextAmbientSoundID) state = null;
					else state = SoundState.FADE_BETWEEN;
				}
				else state = SoundState.FADE_UP;
			}
			
			ambientTicks = 0;
			
			if (currentAmbientSoundID != null) currentAmbientKey = currentAmbientSoundID.toString();
			if (nextAmbientSoundID != null) nextAmbientKey = nextAmbientSoundID.toString();
		}
		else ambientTicks++;
		
		if (state == null) return;
		
		float delta = ambientTicks / 80F;
		
		switch (state) {
			case FADE_UP -> {
				if (!soundSystem.playing(nextAmbientKey)) {
					SoundEntry soundEntry = BNBClientSounds.getSound(nextAmbientSoundID);
					soundSystem.backgroundMusic(nextAmbientKey, soundEntry.soundUrl, soundEntry.soundName, true);
					soundSystem.play(nextAmbientKey);
				}
				soundSystem.setVolume(nextAmbientKey, gameOptions.sound * delta);
			}
			case FADE_DOWN -> {
				float volume = gameOptions.sound * (1.0F - delta);
				soundSystem.setVolume(currentAmbientKey, volume);
				if (volume == 0) soundSystem.stop(currentAmbientKey);
			}
			case FADE_BETWEEN -> {
				float volume1 = gameOptions.sound * delta;
				float volume2 = gameOptions.sound * (1.0F - delta);
				
				soundSystem.setVolume(currentAmbientKey, volume1);
				if (volume1 == 0) soundSystem.stop(currentAmbientKey);
				
				if (!soundSystem.playing(nextAmbientKey)) {
					SoundEntry soundEntry = BNBClientSounds.getSound(nextAmbientSoundID);
					soundSystem.backgroundMusic(nextAmbientKey, soundEntry.soundUrl, soundEntry.soundName, true);
					soundSystem.play(nextAmbientKey);
				}
				soundSystem.setVolume(nextAmbientKey, volume2);
			}
		}
	}
	
	private static Identifier getSound(PlayerBase player, BiomeSource source) {
		int x = MathHelper.floor(player.x);
		int z = MathHelper.floor(player.z);
		return source.getBiome(x, z) instanceof NetherBiome biome ? biome.getAmbientSound() : null;
	}
	
	private enum SoundState {
		FADE_UP, FADE_DOWN, FADE_BETWEEN
	}
}
