package paulevs.bnb.world.generator.terrain;

import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.VoronoiNoise;

public class TheWallFeature extends TerrainFeature {
	private final FractalNoise distortionX = new FractalNoise(PerlinNoise::new);
	private final FractalNoise distortionY = new FractalNoise(PerlinNoise::new);
	private final FractalNoise distortionZ = new FractalNoise(PerlinNoise::new);
	private final FractalNoise thickness = new FractalNoise(PerlinNoise::new);
	private final VoronoiNoise hive = new VoronoiNoise();
	
	public TheWallFeature() {
		distortionX.setOctaves(2);
		distortionY.setOctaves(2);
		distortionZ.setOctaves(2);
		thickness.setOctaves(2);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float dx = distortionX.get(x * 0.01, y * 0.01, z * 0.01) * 1.5F;
		float dy = distortionY.get(x * 0.01, y * 0.01, z * 0.01) * 1.5F;
		float dz = distortionZ.get(x * 0.01, y * 0.01, z * 0.01) * 1.5F;
		float thick = MathHelper.lerp(thickness.get(x * 0.003, y * 0.003, z * 0.003), 1.3F, 1.4F);
		return thick - hive.getF1F3(x * 0.015 + dx, y * 0.015 + dy, z * 0.015 + dz);
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		distortionX.setSeed(RANDOM.nextInt());
		distortionY.setSeed(RANDOM.nextInt());
		distortionZ.setSeed(RANDOM.nextInt());
		thickness.setSeed(RANDOM.nextInt());
		hive.setSeed(RANDOM.nextInt());
	}
}
