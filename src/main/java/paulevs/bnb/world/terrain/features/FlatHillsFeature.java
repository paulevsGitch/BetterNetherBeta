package paulevs.bnb.world.terrain.features;

import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;

public class FlatHillsFeature extends TerrainFeature {
	private final FractalNoise noise = new FractalNoise(PerlinNoise::new);
	
	public FlatHillsFeature() {
		noise.setOctaves(2);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float density = gradient(y, 112, 144, 1, -1);
		density = Math.max(density, gradient(y, 224, 256, -1, 1));
		return density + noise.get(x * 0.02, z * 0.02) - 0.5F;
	}
	
	@Override
	public void setSeed(int seed) {
		noise.setSeed(seed);
	}
}
