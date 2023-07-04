package paulevs.bnb.world.generator.terrain;

import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;

public class LavaOceanFeature extends TerrainFeature {
	private final FractalNoise ceilingSpikes = new FractalNoise(PerlinNoise::new);
	private final FractalNoise floorSpikes = new FractalNoise(PerlinNoise::new);
	
	public LavaOceanFeature() {
		ceilingSpikes.setOctaves(3);
		floorSpikes.setOctaves(3);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float floor = gradient(y, 0, 80, 0.5F, -2.5F);
		float spikes = MathHelper.cos(floorSpikes.get(x * 0.02, z * 0.02) * PI_HALF) * 0.5F + 0.5F;
		spikes = spikes * spikes * spikes;
		floor += spikes;
		
		float ceil = gradient(y, 200, 256, -1.0F, 0.5F);
		spikes = MathHelper.cos(ceilingSpikes.get(x * 0.02, z * 0.02) * PI_HALF) * 0.5F + 0.5F;
		spikes = spikes * spikes * spikes;
		ceil += spikes;
		
		return Math.max(floor, ceil);
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		ceilingSpikes.setSeed(RANDOM.nextInt());
	}
}
