package paulevs.bnb.noise;

import net.modificationstation.stationapi.api.util.math.MathHelper;

import java.util.Random;

public class FractalPerlin extends FloatNoise {
	private final Random random = new Random(0);
	private final PerlinNoise[] noises = new PerlinNoise[] {
		new PerlinNoise(), new PerlinNoise()
	};
	
	private int octaves = 1;
	
	public void setOctaves(int octaves) {
		this.octaves = octaves;
	}
	
	@Override
	public float get(double x, double y) {
		float value = noises[0].get(x, y);
		for (int i = 1; i < octaves; i++) {
			int scale = 1 << i;
			float noise = noises[i & 1].get(x * scale, y * scale);
			value = MathHelper.lerp(1F / (scale + 1), value, noise);
		}
		return value;
	}
	
	@Override
	public float get(double x, double y, double z) {
		float value = noises[0].get(x, y, z);
		for (int i = 1; i < octaves; i++) {
			int scale = 1 << i;
			float noise = noises[i & 1].get(x * scale, y * scale, z * scale);
			value = MathHelper.lerp(1F / (scale + 1), value, noise);
		}
		return value;
	}
	
	@Override
	public void setSeed(int seed) {
		random.setSeed(seed);
		noises[0].setSeed(random.nextInt());
		noises[1].setSeed(random.nextInt());
	}
}
