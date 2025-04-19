package paulevs.bnb.world.terrain.features;

import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;

public class RiversFeature extends TerrainFeature {
	private static final FractalNoise REGION_DISTORTION_NOISE_X = new FractalNoise(PerlinNoise::new);
	private static final FractalNoise REGION_DISTORTION_NOISE_Z = new FractalNoise(PerlinNoise::new);
	private static final PerlinNoise REGION_RIVERS_NOISE = new PerlinNoise();
	
	private final PerlinNoise riversNoise = new PerlinNoise();
	private final FractalNoise distortionNoiseX = new FractalNoise(PerlinNoise::new);
	private final FractalNoise distortionNoiseZ = new FractalNoise(PerlinNoise::new);
	
	public RiversFeature() {
		distortionNoiseX.setOctaves(2);
		distortionNoiseZ.setOctaves(2);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		if (y < 80 || y > 240) return 2.0F;
		double dx = x * 0.007;
		double dz = z * 0.007;
		double px = dx * 2.0 + distortionNoiseX.get(dx, dz) * 2.0F;
		double pz = dz * 2.0 + distortionNoiseZ.get(dx, dz) * 2.0F;
		float density = riversNoise.get(px * 0.2, pz * 0.2);
		density = Math.abs(density - 0.5F) * 2.0F + 0.48F;
		density += gradient(y, 80, 96, 0.02F, 0.0F);
		density += gradient(y, 97, 128, 0.0F, -0.1F);
		density += gradient(y, 129, 240, 0.0F, 0.5F);
		return density;
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		riversNoise.setSeed(RANDOM.nextInt());
		distortionNoiseX.setSeed(RANDOM.nextInt());
		distortionNoiseZ.setSeed(RANDOM.nextInt());
	}
	
	@Override
	public float blendDensity(float density, int x, int y, int z, float scale) {
		return Math.min(density, getDensity(x, y, z));
	}
	
	public static void setSeedForRegion(int seed) {
		RANDOM.setSeed(seed);
		REGION_RIVERS_NOISE.setSeed(RANDOM.nextInt());
		REGION_DISTORTION_NOISE_X.setSeed(RANDOM.nextInt());
		REGION_DISTORTION_NOISE_Z.setSeed(RANDOM.nextInt());
	}
	
	public static boolean isRiverRegion(double x, double z) {
		double dx = x * 0.007;
		double dz = z * 0.007;
		double px = dx * 2.0 + REGION_DISTORTION_NOISE_X.get(dx, dz) * 2.0F;
		double pz = dz * 2.0 + REGION_DISTORTION_NOISE_Z.get(dx, dz) * 2.0F;
		float density = REGION_RIVERS_NOISE.get(px * 0.2, pz * 0.2);
		density = Math.abs(density - 0.5F) * 2.0F + 0.45F;
		return density < 0.5F;
	}
}
