package paulevs.bnb.world.terrain.features;

import net.minecraft.util.maths.MCMath;
import net.minecraft.util.maths.Vec3D;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNB;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.SDFScatter2D;
import paulevs.bnb.world.BNBWorldGenerator;
import paulevs.bnb.world.terrain.TerrainMap;

import java.util.Random;

public class OceanPillarsFeature extends TerrainFeature {
	public static final Identifier FEATURE_ID = BNB.id("ocean_pillars");
	private final SDFScatter2D scatter = new SDFScatter2D(this::getPillar);
	private final FractalNoise noise = new FractalNoise(PerlinNoise::new);
	private final Random random = new Random();
	private TerrainMap map;
	
	public OceanPillarsFeature() {
		noise.setOctaves(2);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float density = gradient(y, 80, 96, 1, -1);
		density = Math.max(density, gradient(y, 224, 256, -1, 1));
		density += noise.get(x * 0.03, z * 0.03) * 0.8F - 0.4F;
		
		float feature = scatter.get(x * 0.03, y * 0.03, z * 0.03);
		feature += noise.get(x * 0.01, y * 0.01, z * 0.01) * 0.15F;
		feature += noise.get(x * 0.07, y * 0.07, z * 0.07) * 0.1F;
		density = smoothMax(density, feature, 2F);
		
		return density;
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		scatter.setSeed(RANDOM.nextInt());
		noise.setSeed(RANDOM.nextInt());
	}
	
	private float getPillar(int seed, Vec3D localPos, Vec3D worldPos) {
		int px = MCMath.floor(worldPos.x / 0.03);
		int pz = MCMath.floor(worldPos.z / 0.03);
		if (map == null) map = BNBWorldGenerator.getMapCopy();
		if (map != null) {
			if (
				map.getData(px, pz) != FEATURE_ID ||
				map.getData(px + 16, pz) != FEATURE_ID ||
				map.getData(px - 16, pz) != FEATURE_ID ||
				map.getData(px, pz + 16) != FEATURE_ID ||
				map.getData(px, pz - 16) != FEATURE_ID
			) return 0;
		}
		
		random.setSeed(seed);
		
		float height = (100 + random.nextFloat() * 80) * 0.03F;
		float tip = height + (10 + random.nextFloat() * 10) * 0.03F;
		float dist = 0.7F - MCMath.sqrt(localPos.x * localPos.x + localPos.z * localPos.z);
		
		float support = gradient((float) localPos.y, 80 * 0.03F, height, -1.0F, 1.0F);
		float top = gradient((float) localPos.y, height, tip, 0.0F, 1.0F);
		support = Math.min(support * support, (1.0F - top * top * top) * 8.0F - 7.0F);
		
		return dist + support * 0.2F;
	}
}