package paulevs.bnb.world.terrain.features;

import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.VoronoiNoise;
import paulevs.bnb.world.BNBWorldGenerator;
import paulevs.bnb.world.terrain.TerrainMap;

public class CavesFeature extends TerrainFeature {
	private final FractalNoise noiseBig = new FractalNoise(PerlinNoise::new);
	private final FractalNoise noiseSmall = new FractalNoise(PerlinNoise::new);
	private final VoronoiNoise tunnels = new VoronoiNoise();
	private TerrainMap map;
	
	public CavesFeature() {
		noiseBig.setOctaves(2);
		noiseSmall.setOctaves(2);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		if (y > 140 || y < 8) return 2.0F;
		
		double px = x * 0.01;
		double pz = z * 0.01;
		double py = y * 0.02;
		float densityTunnels = 1.4F - Math.max(tunnels.getF1F3(px, py, pz), tunnels.getF1F3(px, py + 0.02, pz));
		densityTunnels += getRegionDensity(x, z) * gradient(y, 80, 96, 0.0F, 1.0F);
		densityTunnels += gradient(y, 120, 140, 0.0F, 1.0F);
		
		if (y > 60) return densityTunnels;
		
		float densityCaves = noiseBig.get(x * 0.01, y * 0.03, z * 0.01);
		densityCaves += noiseSmall.get(x * 0.1, y * 0.1, z * 0.1) * 0.1F;
		densityCaves += gradient(y, 64, 80, 0.0F, 1.0F);
		
		float density = smoothMin(densityCaves, densityTunnels, 0.05F);
		density += gradient(y, 8, 24, 1.0F, 0.0F);
		
		return density;
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		noiseBig.setSeed(RANDOM.nextInt());
		noiseSmall.setSeed(RANDOM.nextInt());
		tunnels.setSeed(RANDOM.nextInt());
	}
	
	@Override
	public float blendDensity(float density, int x, int y, int z, float scale) {
		return Math.min(density, getDensity(x, y, z));//density < 0.75F ? density : Math.min(density, getDensity(x, y, z));
	}
	
	private float getRegionDensity(int x, int z) {
		if (map == null) map = BNBWorldGenerator.getMapCopy();
		if (map == null) return 1.0F;
		
		int x1 = (x >> 5) << 5;
		int z1 = (z >> 5) << 5;
		int x2 = x1 + 32;
		int z2 = z1 + 32;
		float dx = (x - x1) / 32.0F;
		float dz = (x - x1) / 32.0F;
		
		float a = map.getRegion(x1, z1).isLand() ? 0.0F : 1.0F;
		float b = map.getRegion(x2, z1).isLand() ? 0.0F : 1.0F;
		float c = map.getRegion(x1, z2).isLand() ? 0.0F : 1.0F;
		float d = map.getRegion(x2, z2).isLand() ? 0.0F : 1.0F;
		
		a = MathHelper.lerp(dx, a, b);
		b = MathHelper.lerp(dx, c, d);
		return MathHelper.lerp(dz, a, b);
	}
}
