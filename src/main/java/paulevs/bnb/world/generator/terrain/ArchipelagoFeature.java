package paulevs.bnb.world.generator.terrain;

import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.SphericalVoronoiNoise;
import paulevs.bnb.noise.VoronoiNoise;

public class ArchipelagoFeature extends TerrainFeature {
	private final VoronoiNoise islandsPower = new VoronoiNoise();
	private final SphericalVoronoiNoise bigIslands = new SphericalVoronoiNoise();
	private final SphericalVoronoiNoise smallIslands = new SphericalVoronoiNoise();
	private final FractalNoise ceilingSpikes = new FractalNoise(PerlinNoise::new);
	private static final float PI_HALF = (float) (Math.PI * 0.5);
	
	public ArchipelagoFeature() {
		ceilingSpikes.setOctaves(3);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		if (y < 8 || y > 248) return 2.0F;
		
		float density = -2F;
		
		if (y < 64) {
			density = gradient(y, 0, 100, 0.5F, -2.5F);
			
			float power = MathHelper.cos(islandsPower.get(x * 0.005, z * 0.005) * PI_HALF) * 0.5F + 0.5F;
			power = power * power * power;
			
			density += bigIslands.get(x * 0.02, z * 0.02) * power;
			density += smallIslands.get(x * 0.08, z * 0.08) * 0.2F * power;
		}
		
		if (y > 150) {
			density = gradient(y, 200, 256, -1.0F, 0.5F);
			
			float spikes = MathHelper.cos(ceilingSpikes.get(x * 0.02, z * 0.02) * PI_HALF) * 0.5F + 0.5F;
			spikes = spikes * spikes * spikes;
			density += spikes;
			
			float power = MathHelper.cos(islandsPower.get(x * 0.005, z * 0.005) * PI_HALF) * 0.5F + 0.5F;
			power = power * power * power;
			density += power * spikes* gradient(y, 100, 200, 0.0F, 1.0F);
		}
		
		return density;
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		islandsPower.setSeed(RANDOM.nextInt());
		bigIslands.setSeed(RANDOM.nextInt());
		smallIslands.setSeed(RANDOM.nextInt());
		ceilingSpikes.setSeed(RANDOM.nextInt());
	}
}
