package paulevs.bnb.world.terrain.features.legacy;

import net.minecraft.util.maths.MCMath;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.VoronoiNoise;
import paulevs.bnb.world.terrain.features.TerrainFeature;

public class LegacyBridgesFeature extends TerrainFeature {
	private final VoronoiNoise bridges = new VoronoiNoise();
	private final FractalNoise distortX = new FractalNoise(PerlinNoise::new);
	private final FractalNoise distortZ = new FractalNoise(PerlinNoise::new);
	private final FractalNoise floor = new FractalNoise(PerlinNoise::new);
	private final FractalNoise ceiling = new FractalNoise(PerlinNoise::new);
	
	public LegacyBridgesFeature() {
		floor.setOctaves(3);
		ceiling.setOctaves(3);
		distortX.setOctaves(2);
		distortZ.setOctaves(2);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float height = floor.get(x * 0.005 * 0.5F, z * 0.005 * 0.5F) * 20 + 96 + 16;
		float dx = distortX.get(x * 0.01 * 0.5F, z * 0.01 * 0.5F);
		float dz = distortZ.get(x * 0.01 * 0.5F, z * 0.01 * 0.5F);
		float density = bridges.getF1F2(x * 0.01 * 0.5F + dx, z * 0.01 * 0.5F + dz) - 0.3F;
		
		float support = floor.get(x * 0.03 * 0.5F, z * 0.03 * 0.5F) + density - 0.7F;
		support -= gradient(y, height, height + 10, 0.0F, 2.0F);
		support += gradient(y, height - 10, height, 0.0F, 0.1F);
		
		float depth = gradient(y, height - 10, height + 10, -1.0F, 1.0F);
		depth = MCMath.sqrt(1 - depth * depth);
		density -= 1 - depth;
		
		density = Math.max(density, support);
		density += floor.get(x * 0.03 * 0.5F, y * 0.03 * 0.5F, z * 0.03 * 0.5F) * 0.1F;
		
		float ceil = MCMath.cos(ceiling.get(x * 0.02 * 0.5F, z * 0.02 * 0.5F) * PI_HALF) * 0.5F + 0.5F;
		ceil = ceil * ceil * ceil;
		ceil += gradient(y, 200, 256, -1.0F, 0.5F);
		density = Math.max(density, ceil);
		
		float floor = MCMath.cos(this.floor.get(x * 0.02 * 0.5F, z * 0.02 * 0.5F) * PI_HALF) * 0.5F + 0.5F;
		floor = floor * floor * floor;
		floor += gradient(y, 0, 60, 1.0F, -2.0F);
		density = smoothMax(density, floor, 0.5F);
		
		return density;
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		bridges.setSeed(RANDOM.nextInt());
		distortX.setSeed(RANDOM.nextInt());
		distortZ.setSeed(RANDOM.nextInt());
		floor.setSeed(RANDOM.nextInt());
		ceiling.setSeed(RANDOM.nextInt());
	}
}