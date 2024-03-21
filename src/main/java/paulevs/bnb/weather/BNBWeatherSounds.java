package paulevs.bnb.weather;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.SoundEntry;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.level.Level;
import net.minecraft.util.maths.MathHelper;
import paulscode.sound.SoundSystem;

import java.net.URL;

@Environment(EnvType.CLIENT)
public class BNBWeatherSounds {
	public static final SoundEntry RAIN = getSound("lava_rain");
	private static final String RAIN_KEY = "bnb.weather.lava_rain";
	
	private static boolean underRoof = false;
	private static SoundSystem soundSystem;
	
	public static SoundEntry getSound(String name) {
		name = "assets/bnb/stationapi/sounds/weather/" + name + ".ogg";
		URL url = Thread.currentThread().getContextClassLoader().getResource(name);
		return new SoundEntry(name, url);
	}
	
	public static void stop() {
		if (soundSystem == null) return;
		if (soundSystem.playing(RAIN_KEY)) soundSystem.stop(RAIN_KEY);
	}
	
	public static void updateSound(Level level, LivingEntity entity, SoundSystem soundSystem, float volume) {
		BNBWeatherSounds.soundSystem = soundSystem;
		
		if (level == null || entity == null || level.dimension.id != -1) {
			stop();
			return;
		}
		
		if (!BNBWeatherRenderer.isCurrentWeather(WeatherType.LAVA_RAIN)) volume = 0.0F;
		volume *= BNBWeatherRenderer.getIntensity(WeatherType.LAVA_RAIN);
		volume *= getWeatherVolume(level, entity);
		if (volume == 0) {
			stop();
			return;
		}
		else if (!soundSystem.playing(RAIN_KEY)) {
			soundSystem.backgroundMusic(RAIN_KEY, RAIN.soundUrl, RAIN.soundName, true);
			soundSystem.play(RAIN_KEY);
		}
		
		int x = MathHelper.floor(entity.x);
		int z = MathHelper.floor(entity.z);
		boolean newRoof = entity.y + entity.height < BNBWeatherManager.getWeatherBottom(level, x, z);
		if (newRoof != underRoof) {
			soundSystem.setPitch(RAIN_KEY, newRoof ? 0.25F : 1.0F);
			underRoof = newRoof;
		}
		
		soundSystem.setVolume(RAIN_KEY, volume);
	}
	
	private static float getWeatherVolume(Level level, LivingEntity entity) {
		int ix = MathHelper.floor(entity.x);
		int iz = MathHelper.floor(entity.z);
		
		double entityY = entity.y + entity.height;
		float volume = 0.0F;
		
		for (int dx = -8; dx <= 8; dx++) {
			int px = ix + dx;
			for (int dz = -8; dz <= 8; dz++) {
				int pz = iz + dz;
				int y = BNBWeatherManager.getWeatherBottom(level, px, pz);
				if (y - entityY < 8) volume += 0.00346F;
			}
		}
		
		return volume;
	}
}
