package paulevs.bnb.sound;

import net.minecraft.class_267;
import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.dimension.Nether;
import paulevs.bnb.util.ClientUtil;
import paulevs.bnb.world.biome.NetherBiome;
import paulscode.sound.SoundSystem;

public class MusicManager {
	private static final String AMBIENCE_ID = "bnbAmbientSound";
	private static NetherSoundSource soundSource;
	private static NetherAmbientSound sound;
	private static SoundSystem soundSystem;
	private static GameOptions gameOptions;
	private static int ambientIndex = -1;
	private static Minecraft minecraft;
	private static int ticks = 0;
	
	public static void init(SoundSystem soundSystem, NetherSoundSource soundSource, GameOptions gameOptions) {
		MusicManager.minecraft = ClientUtil.getMinecraft();
		MusicManager.soundSystem = soundSystem;
		MusicManager.soundSource = soundSource;
		MusicManager.gameOptions = gameOptions;
	}
	
	public static void checkStop() {
		if (sound != null) {
			if (!levelIsValid()) {
				soundSystem.stop(AMBIENCE_ID);
				sound = null;
			}
		}
	}
	
	public static void checkStart() {
		if (levelIsValid()) {
			NetherAmbientSound newSound = getAmbientSound();
			if (newSound == sound) {
				return;
			}
			if (sound != null && ticks < 50) {
				soundSystem.setVolume(AMBIENCE_ID, gameOptions.sound * (50 - ticks) * 0.02F);
				ticks++;
			}
			else {
				ticks = 0;
				sound = newSound;
				if (newSound == null) {
					return;
				}
				class_267 source = newSound.getSound(soundSource);
				if (source == null) {
					return;
				}
				soundSystem.backgroundMusic(AMBIENCE_ID, source.field_2127, source.field_2126, true);
				soundSystem.setVolume(AMBIENCE_ID, gameOptions.sound);
				soundSystem.play(AMBIENCE_ID);
			}
		}
	}
	
	private static boolean levelIsValid() {
		Level level = minecraft.level;
		return level != null && level.dimension instanceof Nether;
	}
	
	private static NetherAmbientSound getAmbientSound() {
		AbstractClientPlayer player = minecraft.player;
		Biome biome = minecraft.level.getBiomeSource().getBiome((int) player.x, (int) player.z);
		if (biome instanceof NetherBiome) {
			return ((NetherBiome) biome).getAmbientSound();
		}
		return null;
	}
}
