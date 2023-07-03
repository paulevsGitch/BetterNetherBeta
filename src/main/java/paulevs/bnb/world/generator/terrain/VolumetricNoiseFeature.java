package paulevs.bnb.world.generator.terrain;

import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;

public class VolumetricNoiseFeature extends TerrainFeature {
	private final FractalNoise noise = new FractalNoise(PerlinNoise::new);
	private final FractalNoise details = new FractalNoise(PerlinNoise::new);
	
	public VolumetricNoiseFeature() {
		noise.setOctaves(2);
		details.setOctaves(3);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float density = noise.get(x * 0.007F, y * 0.02F, z * 0.007F);
		density += noise.get(x * 0.05F, y * 0.05F, z * 0.05F) * 0.1F - 0.005F;
		float grad = Math.max(gradient(y, 0, 20, 1.0F, 0.0F), gradient(y, 235, 255, 0.0F, 1.0F));
		density = smoothMax(density, grad, 0.3F);
		return density;
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		noise.setSeed(RANDOM.nextInt());
		details.setSeed(RANDOM.nextInt());
	}
}
