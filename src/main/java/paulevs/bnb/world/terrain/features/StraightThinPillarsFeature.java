package paulevs.bnb.world.terrain.features;

import net.minecraft.util.maths.MCMath;
import net.minecraft.util.maths.Vec3D;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.SDFScatter2D;
import paulevs.bnb.world.BNBWorldGenerator;
import paulevs.bnb.world.terrain.TerrainMap;
import paulevs.bnb.world.terrain.TerrainRegion;

public class StraightThinPillarsFeature extends TerrainFeature {
	private final SDFScatter2D pillars = new SDFScatter2D(this::getPillar);
	private final FractalNoise noise = new FractalNoise(PerlinNoise::new);
	private TerrainMap map;
	
	public StraightThinPillarsFeature() {
		noise.setOctaves(3);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float density = pillars.get(x * 0.004, y * 0.004, z * 0.004);
		if (density < 0.13F || density > 0.5F) return density;
		float grad = Math.abs(gradient(y, 96, 256, -1.0F, 1.0F));
		density += grad * grad * grad * 0.02F;
		density += noise.get(x * 0.03, y * 0.03, z * 0.03) * 0.01F;
		density += noise.get(x * 0.02, y * 0.02, z * 0.02) * 0.1F * grad * grad;
		return density;
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		pillars.setSeed(RANDOM.nextInt());
		noise.setSeed(RANDOM.nextInt());
		map = null;
	}
	
	private float getPillar(int seed, Vec3D relativePos, Vec3D worldPos) {
		int wx = MCMath.floor(worldPos.x / 0.007);
		int wz = MCMath.floor(worldPos.z / 0.007);
		if (map == null) map = BNBWorldGenerator.getMapCopy();
		TerrainRegion region = map == null ? TerrainRegion.PLAINS : map.getRegion(wx, wz);
		if (region == TerrainRegion.OCEAN_NORMAL || region == TerrainRegion.OCEAN_MOUNTAINS) return 0;
		if (region == TerrainRegion.SHORE_NORMAL || region == TerrainRegion.SHORE_MOUNTAINS) return 0;
		if (region == TerrainRegion.BRIDGES) return 0;
		float dx = (float) relativePos.x;
		float dz = (float) relativePos.z;
		return 0.52F - MCMath.sqrt(dx * dx + dz * dz);
	}
}
