package paulevs.bnb.world.terrain.features;

import net.minecraft.util.maths.MCMath;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.VoronoiNoise;

public class StalactitesFeature extends TerrainFeature {
	private final VoronoiNoise hugeSpikesCeiling = new VoronoiNoise();
	private final VoronoiNoise bigSpikesCeiling = new VoronoiNoise();
	private final VoronoiNoise smallSpikesCeiling = new VoronoiNoise();
	private final FractalNoise noise = new FractalNoise(PerlinNoise::new);
	private final FractalNoise placement = new FractalNoise(VoronoiNoise::new);
	
	public StalactitesFeature() {
		noise.setOctaves(3);
		placement.setOctaves(2);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float height = Math.max(1.0F - placement.get(x * 0.01, z * 0.01) * 1.5F, 0.0F);
		height *= height;
		
		float h = 256 - 100 * height;
		if (y < h) return 0;
		
		float spikes = MCMath.cos(this.bigSpikesCeiling.get(x * 0.1, z * 0.1) * PI_HALF) * 0.5F + 0.5F;
		spikes = spikes * spikes * spikes;
		spikes += gradient(y, h, 256, -1.0F, 0.1F);
		float density = spikes;
		
		h = 256 - 50 * height;
		if (y < h) return density;
		
		spikes = MCMath.cos(this.smallSpikesCeiling.get(x * 0.3, z * 0.3) * PI_HALF) * 0.5F + 0.5F;
		spikes = spikes * spikes * spikes;
		spikes += gradient(y, h, 256, -1.0F, 0.1F);
		density = Math.max(density, spikes);
		
		density += noise.get(x * 0.03, y * 0.03, z * 0.03) * 0.2F;
		
		return density;
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		hugeSpikesCeiling.setSeed(RANDOM.nextInt());
		bigSpikesCeiling.setSeed(RANDOM.nextInt());
		smallSpikesCeiling.setSeed(RANDOM.nextInt());
		noise.setSeed(RANDOM.nextInt());
		placement.setSeed(RANDOM.nextInt());
	}
}
