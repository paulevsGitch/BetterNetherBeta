package paulevs.bnb.world.generator.terrain;

import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.VoronoiNoise;

public class PillarsFeature extends TerrainFeature {
	private final VoronoiNoise pillarsNoise = new VoronoiNoise();
	private final VoronoiNoise spikes = new VoronoiNoise();
	private final PerlinNoise details = new PerlinNoise();
	private final FractalNoise floorAndCeiling = new FractalNoise(PerlinNoise::new);
	private static final float PI_HALF = (float) (Math.PI * 0.5);
	
	public PillarsFeature() {
		floorAndCeiling.setOctaves(3);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float noiseValue = pillarsNoise.get(x * 0.03, z * 0.03);
		float height = pillarsNoise.getID(x * 0.03, z * 0.03);
		float maxHeight = height * 100 + 40;
		
		if (y < maxHeight && noiseValue < 0.002F) return 100.0F;
		
		float size = MathHelper.cos(height * 1 - 0.5F);
		float movedY = y - maxHeight;
		float density = Math.max(gradient(movedY, -100, 0, 1.0F, 0.0F), gradient(movedY, 0, 5, 0.0F, 1.0F));
		if (y > maxHeight) {
			density = 1.0F - density * density;
			density = MathHelper.sqrt(density);
		}
		else {
			density = 1.0F - MathHelper.sqrt(density);
		}
		
		density *= 0.8F * size - noiseValue;
		
		float support = 0.7F * size - noiseValue;
		support *= gradient(movedY, 0, 5, 1.0F, 0.0F);
		
		float middleHeight = maxHeight * 0.5F;
		size = Math.max(gradient(y, 0, middleHeight, 1.0F, 0.0F), gradient(y, middleHeight, maxHeight, 0.0F, 1.0F));
		support *= size * size * 0.25F + 0.75F;
		
		density = Math.max(density, support);
		
		density += details.get(x * 0.06, y * 0.06, z * 0.06) * 0.15F;
		density += details.get(x * 0.1, y * 0.1, z * 0.1) * 0.03F;
		
		float floor = gradient(y, 0, 80, 0.5F, -1.0F);
		floor += this.floorAndCeiling.get(x * 0.02, z * 0.02);
		density = Math.max(density, floor);
		
		float ceil = gradient(y, 200, 250, -1.0F, 1.0F);
		ceil += this.floorAndCeiling.get(x * 0.05, z * 0.05);
		density = Math.max(density, ceil);
		
		ceil = gradient(y, 200, 250, -1.0F, 1.0F);
		float noise = this.floorAndCeiling.get(x * 0.1, z * 0.1);
		ceil += noise * noise * noise;
		density = Math.max(density, ceil);
		
		float spikes = MathHelper.cos(this.spikes.get(x * 0.1, z * 0.1) * PI_HALF) * 0.5F + 0.5F;
		spikes = spikes * spikes * spikes;
		spikes *= gradient(y, 0, 150, 1.0F, 0.0F);
		spikes *= 1.0F - noiseValue;
		density += spikes;
		
		return density;
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		pillarsNoise.setSeed(RANDOM.nextInt());
		spikes.setSeed(RANDOM.nextInt());
		details.setSeed(RANDOM.nextInt());
		floorAndCeiling.setSeed(RANDOM.nextInt());
	}
}
