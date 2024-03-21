package paulevs.bnb.weather;

import java.util.Random;

public enum WeatherType {
	CLEAR(300, 1200, 1.0F),
	FOG(30, 120, 0.55F),
	LAVA_RAIN(60, 300, 0.5F);
	
	private static final WeatherType[] VALUES = values();
	private final int minTicks;
	private final int deltaTicks;
	public final float fogIntensity;
	
	WeatherType(int minSeconds, int maxSeconds, float fogIntensity) {
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
}
