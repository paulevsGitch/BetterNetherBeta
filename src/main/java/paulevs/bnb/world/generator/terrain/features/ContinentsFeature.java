package paulevs.bnb.world.generator.terrain.features;

import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.VoronoiNoise;

public class ContinentsFeature extends TerrainFeature {
	private final FractalNoise shape = new FractalNoise(PerlinNoise::new);
	private final VoronoiNoise pillars = new VoronoiNoise();
	private final FractalNoise details = new FractalNoise(PerlinNoise::new);
	private final VoronoiNoise spikes = new VoronoiNoise();
	
	public ContinentsFeature() {
		shape.setOctaves(3);
		details.setOctaves(2);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float noise = shape.get(x * 0.003, z * 0.003);
		
		float density = gradient(y, 10, 40, 1.0F, -1.0F);
		
		float feature = (float) Math.tanh((noise - 0.4F) * 50) * 0.5F + 0.5F;
		feature *= gradient(y, 20, 80, 1.0F, -1.0F);
		density = smoothMax(density, feature, 0.5F);
		
		feature = (float) Math.tanh((noise - 0.5F) * 50) * 0.5F + 0.5F;
		feature *= gradient(y, 20, 120, 1.0F, -1.0F);
		density = smoothMax(density, feature, 0.5F);
		
		feature = (float) Math.tanh((noise - 0.6F) * 50) * 0.5F + 0.5F;
		feature *= gradient(y, 20, 200, 1.0F, -1.0F);
		density = smoothMax(density, feature, 0.5F);
		
		float smallDetails = details.get(x * 0.05, y * 0.05, z * 0.05);
		density += smallDetails * 0.15F;
		
		feature = (noise - 0.25F) * 3F + gradient(y, 180, 235, -2.5F, 0.0F);
		feature += details.get(x * 0.03, y * 0.03, z * 0.03) * 2F;
		feature = smoothMax(feature, gradient(y, 245, 255, -1.0F, 1.0F), 0.3F);
		density = Math.max(density, feature);
		
		feature = this.pillars.get(x * 0.003, z * 0.003);
		float spikeDensity = 1.0F - feature;
		spikeDensity *= spikeDensity;
		
		feature += details.get(x * 0.03, y * 0.03, z * 0.03) * 0.1F;
		feature = 0.6F - feature;
		float topBottom = Math.max(gradient(y, 0, 127, 1.0F, 0.0F), gradient(y, 127, 255, 0.0F, 1.0F));
		feature += topBottom * topBottom * 0.2F;
		
		density = smoothMax(density, feature, 0.5F);
		
		feature = 0.8F - spikes.get(x * 0.06, z * 0.06);
		feature *= gradient(y, 255 - 150 * spikeDensity, 255, 0.0F, 2.0F);
		feature += smallDetails * 0.1F;
		density = smoothMax(density, feature, 0.5F);
		
		feature = 0.2F - spikes.get(x * 0.03, z * 0.03);
		feature *= gradient(y, 0, 200 * spikeDensity * spikeDensity, 5.0F, 0.0F);
		feature += smallDetails * 0.1F;
		density = smoothMax(density, feature, 0.5F);
		
		//density = feature;
		//if (density < 0.5) density = -1;
		
		return density;
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		shape.setSeed(seed);
		pillars.setSeed(seed);
		details.setSeed(seed);
		spikes.setSeed(seed);
	}
}
