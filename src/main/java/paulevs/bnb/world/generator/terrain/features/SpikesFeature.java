package paulevs.bnb.world.generator.terrain.features;

import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.VoronoiNoise;

public class SpikesFeature extends TerrainFeature {
	private final VoronoiNoise hugeSpikesFloor = new VoronoiNoise();
	private final VoronoiNoise bigSpikesFloor = new VoronoiNoise();
	private final VoronoiNoise smallSpikesFloor = new VoronoiNoise();
	private final VoronoiNoise hugeSpikesCeiling = new VoronoiNoise();
	private final VoronoiNoise bigSpikesCeiling = new VoronoiNoise();
	private final VoronoiNoise smallSpikesCeiling = new VoronoiNoise();
	private final FractalNoise noise = new FractalNoise(PerlinNoise::new);
	private final VoronoiNoise pillars = new VoronoiNoise();
	
	public SpikesFeature() {
		noise.setOctaves(3);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float density = gradient(y, 0, 60, 1.0F, -1.0F);
		
		float spikes = 1.0F - this.hugeSpikesFloor.get(x * 0.01, z * 0.01);
		float height = spikes;
		spikes = spikes * spikes;
		spikes = spikes + gradient(y, 0, 300, 0.005F, -1.0F);
		density = Math.max(density, spikes);
		
		spikes = MathHelper.cos(this.bigSpikesFloor.get(x * 0.1, z * 0.1) * PI_HALF) * 0.5F + 0.5F;
		spikes = spikes * spikes * spikes;
		spikes += gradient(y, 0, 200 * height, 0.1F, -1.0F);
		density = Math.max(density, spikes);
		
		spikes = MathHelper.cos(this.smallSpikesFloor.get(x * 0.3, z * 0.3) * PI_HALF) * 0.5F + 0.5F;
		spikes = spikes * spikes * spikes;
		spikes += gradient(y, 0, 150 * height, 0.1F, -1.0F);
		density = Math.max(density, spikes);
		
		spikes = 1.0F - this.hugeSpikesCeiling.get(x * 0.01, z * 0.01);
		height = spikes;
		spikes = spikes * spikes;
		spikes = spikes + gradient(y, -44, 256, -1.0F, 0.005F);
		density = Math.max(density, spikes);
		
		spikes = MathHelper.cos(this.bigSpikesCeiling.get(x * 0.1, z * 0.1) * PI_HALF) * 0.5F + 0.5F;
		spikes = spikes * spikes * spikes;
		spikes += gradient(y, 256 - 200 * height, 256, -1.0F, 0.1F);
		density = Math.max(density, spikes);
		
		spikes = MathHelper.cos(this.smallSpikesCeiling.get(x * 0.3, z * 0.3) * PI_HALF) * 0.5F + 0.5F;
		spikes = spikes * spikes * spikes;
		spikes += gradient(y, 256 - 150 * height, 256, -1.0F, 0.1F);
		density = Math.max(density, spikes);
		
		density += noise.get(x * 0.03, y * 0.03, z * 0.03) * 0.2F;
		
		spikes = this.pillars.get(x * 0.003, z * 0.003);
		spikes += noise.get(x * 0.03, y * 0.03, z * 0.03) * 0.1F;
		spikes = 0.6F - spikes;
		float topBottom = Math.max(gradient(y, 0, 127, 1.0F, 0.0F), gradient(y, 127, 255, 0.0F, 1.0F));
		spikes += topBottom * topBottom * 0.2F;
		density = smoothMax(density, spikes, 0.5F);
		
		return density;
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		hugeSpikesFloor.setSeed(RANDOM.nextInt());
		bigSpikesFloor.setSeed(RANDOM.nextInt());
		smallSpikesFloor.setSeed(RANDOM.nextInt());
		hugeSpikesCeiling.setSeed(RANDOM.nextInt());
		bigSpikesCeiling.setSeed(RANDOM.nextInt());
		smallSpikesCeiling.setSeed(RANDOM.nextInt());
		noise.setSeed(RANDOM.nextInt());
		pillars.setSeed(RANDOM.nextInt());
	}
}
