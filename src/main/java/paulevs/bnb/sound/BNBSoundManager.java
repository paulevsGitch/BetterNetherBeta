package paulevs.bnb.sound;

import it.unimi.dsi.fastutil.objects.Reference2FloatMap;
import it.unimi.dsi.fastutil.objects.Reference2FloatOpenHashMap;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.sound.SoundEntry;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.biome.BiomeSource;
import net.minecraft.util.maths.MCMath;
import net.modificationstation.stationapi.api.util.Identifier;
import paulscode.sound.SoundSystem;

import java.util.Random;

public class BNBSoundManager {
	private static final String STREAMING_KEY = "streaming";
	private static final String MUSIC_KEY = "BgMusic";
	private static final Random RANDOM = new Random();
	
	private static boolean inTheNether = false;
	private static int musicCountdown = 0;
	private static GameOptions gameOptions;
	private static SoundSystem soundSystem;
	
	private static final Reference2FloatMap<Identifier> OLD_AMBIENCE_MAP = new Reference2FloatOpenHashMap<>();
	private static final Reference2FloatMap<Identifier> AMBIENCE_MAP = new Reference2FloatOpenHashMap<>();
	
	public static void setInTheNether(boolean inTheNether) {
		if (BNBSoundManager.inTheNether != inTheNether) {
			soundSystem.stop(MUSIC_KEY);
			musicCountdown = 50;
			AMBIENCE_MAP.putAll(OLD_AMBIENCE_MAP);
			for (Identifier sound : AMBIENCE_MAP.keySet()) {
				String key = sound.toString();
				if (soundSystem.playing(key)) soundSystem.stop(key);
			}
			OLD_AMBIENCE_MAP.clear();
			AMBIENCE_MAP.clear();
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
		musicCountdown = 400 + RANDOM.nextInt(800);
		soundSystem.backgroundMusic(MUSIC_KEY, soundEntry.soundUrl, soundEntry.soundName, false);
		soundSystem.setVolume(MUSIC_KEY, gameOptions.music * 0.25F);
		soundSystem.play(MUSIC_KEY);
	}
	
	public static boolean updateMusicVolume() {
		if (!inTheNether || gameOptions.music == 0.0f) return false;
		soundSystem.setVolume(MUSIC_KEY, gameOptions.music * 0.25F);
		return true;
	}
	
	public static void playAmbience(PlayerEntity player, BiomeSource biomeSource) {
		if (!inTheNether) return;
		
		int x1 = MCMath.floor(player.x / 16.0);
		int z1 = MCMath.floor(player.z / 16.0);
		float dx = (float) (player.x / 16.0 - x1);
		float dz = (float) (player.z / 16.0 - z1);
		x1 <<= 4;
		z1 <<= 4;
		int x2 = x1 + 16;
		int z2 = z1 + 16;
		
		Identifier sa = biomeSource.getBiome(x1, z1).bnb_getBiomeAmbience();
		Identifier sb = biomeSource.getBiome(x2, z1).bnb_getBiomeAmbience();
		Identifier sc = biomeSource.getBiome(x1, z2).bnb_getBiomeAmbience();
		Identifier sd = biomeSource.getBiome(x2, z2).bnb_getBiomeAmbience();
		
		float va = (1.0F - dx) * (1.0F - dz);
		float vb = dx * (1.0F - dz);
		float vc = (1.0F - dx) * dz;
		float vd = dx * dz;
		
		AMBIENCE_MAP.clear();
		if (sa != null) AMBIENCE_MAP.put(sa, va);
		if (sb != null) AMBIENCE_MAP.put(sb, AMBIENCE_MAP.getOrDefault(sb, 0.0F) + vb);
		if (sc != null) AMBIENCE_MAP.put(sc, AMBIENCE_MAP.getOrDefault(sc, 0.0F) + vc);
		if (sd != null) AMBIENCE_MAP.put(sd, AMBIENCE_MAP.getOrDefault(sd, 0.0F) + vd);
		
		for (Identifier sound : AMBIENCE_MAP.keySet()) {
			OLD_AMBIENCE_MAP.removeFloat(sound);
			SoundEntry entry = BNBClientSounds.getSound(sound);
			String key = sound.toString();
			float volume = AMBIENCE_MAP.getFloat(sound) * gameOptions.sound;
			boolean playing = soundSystem.playing(key);
			if (!playing && volume > 0.01F) {
				soundSystem.backgroundMusic(key, entry.soundUrl, entry.soundName, true);
			}
			if (playing && volume < 0.01F) {
				soundSystem.stop(key);
				continue;
			}
			soundSystem.setVolume(key, volume);
		}
		
		for (Identifier sound : OLD_AMBIENCE_MAP.keySet()) {
			String key = sound.toString();
			if (soundSystem.playing(key)) soundSystem.stop(key);
		}
		
		OLD_AMBIENCE_MAP.clear();
		OLD_AMBIENCE_MAP.putAll(AMBIENCE_MAP);
	}
}
