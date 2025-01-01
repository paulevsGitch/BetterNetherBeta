package paulevs.bnb.world.generator.terrain.features;

import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.VoronoiNoise;

public class TunnelsFeature extends TerrainFeature {
	private final VoronoiNoise tunnelNoise = new VoronoiNoise();
	private final FractalNoise distortionNoiseX = new FractalNoise(PerlinNoise::new);
	private final FractalNoise distortionNoiseY = new FractalNoise(PerlinNoise::new);
	private final FractalNoise distortionNoiseZ = new FractalNoise(PerlinNoise::new);
	
	public TunnelsFeature() {
		distortionNoiseX.setOctaves(2);
		distortionNoiseY.setOctaves(2);
		distortionNoiseZ.setOctaves(2);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		double dx = x * 0.01;
		double dy = y * 0.01;
		double dz = z * 0.01;
		double px = x * 0.02 + distortionNoiseX.get(dx, dy, dz) * 3.0F;
		double py = y * 0.04 + distortionNoiseY.get(dx, dy, dz) * 3.0F;
		double pz = z * 0.02 + distortionNoiseZ.get(dx, dy, dz) * 3.0F;
		float density = tunnelNoise.getF1F3(px * 0.2, py * 0.2, pz * 0.2);
		//density = Math.max(density, tunnelNoise.getF1F3(px, py + 0.04, pz));
		//density -= gradient(y, 96.0F, 160.0F, 0.0F, 2.0F);
		//density -= gradient(y, 96.0F, 160.0F, 0.0F, 2.0F);
		return (0.98F - density) * 4.0F;
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		tunnelNoise.setSeed(RANDOM.nextInt());
		distortionNoiseX.setSeed(RANDOM.nextInt());
		distortionNoiseY.setSeed(RANDOM.nextInt());
		distortionNoiseZ.setSeed(RANDOM.nextInt());
	}
	
	@Override
	public float getAndMixDensity(float density, int x, int y, int z, float scale) {
		return Math.min(density, getDensity(x, y, z));
	}
}
