package paulevs.bnb.weather;

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;

import java.util.Map;
import java.util.Random;

public enum WeatherType {
	CLEAR("clear", 300, 1200, 1.0F),
	FOG("fog", 30, 120, 0.55F),
	LAVA_RAIN("lava_rain", 60, 300, 0.5F);
	
	private static final Map<String, WeatherType> BY_NAME = new Object2ReferenceOpenHashMap<>();
	private static final WeatherType[] VALUES = values();
	
	private final String name;
	private final int minTicks;
	private final int deltaTicks;
	public final float fogIntensity;
	
	WeatherType(String name, int minSeconds, int maxSeconds, float fogIntensity) {
		this.name = name;
		minTicks = minSeconds * 20;
		deltaTicks = maxSeconds * 20 - minTicks + 1;
		this.fogIntensity = fogIntensity;
	}
	
	public int getTime(Random random) {
		return minTicks + random.nextInt(deltaTicks);
	}
	
	public static WeatherType getByID(byte id) {
		return VALUES[id];
	}
	
	public static WeatherType getByName(String name) {
		return BY_NAME.get(name);
	}
	
	static {
		for (WeatherType type : VALUES) {
			BY_NAME.put(type.name, type);
		}
	}
}
