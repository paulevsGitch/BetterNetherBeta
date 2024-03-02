package paulevs.bnb.world.generator.terrain.features;

import net.minecraft.util.maths.MathHelper;
import net.minecraft.util.maths.Vec3D;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.SDFScatter2D;

import java.util.Random;

public class SmallPillarsFeature extends TerrainFeature {
	private final SDFScatter2D scatter = new SDFScatter2D(this::getPillar);
	private final FractalNoise floor = new FractalNoise(PerlinNoise::new);
	private final FractalNoise ceiling = new FractalNoise(PerlinNoise::new);
	private final Random random = new Random();
	
	public SmallPillarsFeature() {
		floor.setOctaves(3);
		ceiling.setOctaves(3);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float density = gradient(y, 1, 60, 0.7F, -1.0F) + floor.get(x * 0.01, z * 0.01);
		
		float feature = scatter.get(x * 0.03, y * 0.03, z * 0.03);
		density = smoothMax(density, feature, 1F);
		
		feature = gradient(y, 200, 255, -1.0F, 1.0F) + ceiling.get(x * 0.01, z * 0.01);
		density = Math.max(density, feature);
		
		return density;
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		scatter.setSeed(RANDOM.nextInt());
		floor.setSeed(RANDOM.nextInt());
		ceiling.setSeed(RANDOM.nextInt());
	}
	
	private float getPillar(int seed, Vec3D pos) {
		random.setSeed(seed);
		
		float height = (40 + random.nextFloat() * 80) * 0.03F;
		float radius = 0.6F + random.nextFloat() * 0.3F;
		
		float x = (float) pos.x;
		float y = (float) pos.y - height;
		float z = (float) pos.z;
		
		float dx = seed * 0.1F;
		float dy = seed * 0.3F;
		float dz = seed * 0.7F;
		float noise = floor.get(x * 3 + dx, y * 3 + dy, z * 3 + dz) * 0.2F - 0.1F;
		
		float density;
		if (y > 0) {
			y *= 2;
			density = radius - (MathHelper.sqrt(x * x + z * z + y * y));
		}
		else {
			density = radius - MathHelper.sqrt(x * x + z * z) + y;
		}
		
		float support = y > 0 ? 0 : radius * 0.9F - (MathHelper.sqrt(x * x + z * z));
		density = smoothMax(density, support, 1F);
		
		density += noise;
		
		if (density > 0.5F) {
			density = (density - 0.5F) * 10F + 0.5F;
		}
		
		return density;
	}
}