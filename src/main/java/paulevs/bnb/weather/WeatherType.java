package paulevs.bnb.weather;

import java.util.Random;

public enum WeatherType {
	CLEAR(5, 10),
	LAVA_RAIN(5, 10);
	
	private final int minTicks;
	private final int deltaTicks;
	
	WeatherType(int minSeconds, int maxSeconds) {
		minTicks = minSeconds * 20;
		deltaTicks = maxSeconds * 20 - minTicks + 1;
	}
	
	public int getTime(Random random) {
		return minTicks + random.nextInt(deltaTicks);
	}
}
