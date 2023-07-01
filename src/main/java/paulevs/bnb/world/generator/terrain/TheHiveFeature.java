package paulevs.bnb.world.generator.terrain;

import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.VoronoiNoise;

public class TheHiveFeature extends TerrainFeature {
	private final FractalNoise distortionX = new FractalNoise(PerlinNoise::new);
	private final FractalNoise distortionY = new FractalNoise(PerlinNoise::new);
	private final FractalNoise distortionZ = new FractalNoise(PerlinNoise::new);
	private final FractalNoise thickness = new FractalNoise(PerlinNoise::new);
	private final VoronoiNoise hive = new VoronoiNoise();
	private final float[] buffer = new float[28];
	
	public TheHiveFeature() {
		distortionX.setOctaves(2);
		distortionY.setOctaves(2);
		distortionZ.setOctaves(2);
		thickness.setOctaves(2);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float density = Math.max(gradient(y, 0, 60, 1.5F, -1.0F), gradient(y, 195, 255, -1.0F, 1.5F));
		float dx = distortionX.get(x * 0.01, y * 0.01, z * 0.01) * 1.5F;
		float dy = distortionY.get(x * 0.01, y * 0.01, z * 0.01) * 1.5F;
		float dz = distortionZ.get(x * 0.01, y * 0.01, z * 0.01) * 1.5F;
		float thick = MathHelper.lerp(thickness.get(x * 0.003, y * 0.003, z * 0.003), 0.3F, 0.4F);
		float feature = hive.getF1F3(x * 0.015 + dx, y * 0.015 + dy, z * 0.015 + dz, buffer) - thick;
		density = smoothMax(density, feature, 0.5F);
		return density;
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
