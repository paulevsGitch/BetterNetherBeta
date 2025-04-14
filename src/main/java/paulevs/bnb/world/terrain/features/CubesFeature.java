package paulevs.bnb.world.terrain.features;

import net.minecraft.util.maths.MCMath;
import net.minecraft.util.maths.Vec3D;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.BNB;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.SDFScatter2D;
import paulevs.bnb.util.Matrix3F;
import paulevs.bnb.world.BNBWorldGenerator;
import paulevs.bnb.world.terrain.TerrainMap;

import java.util.Random;

public class CubesFeature extends TerrainFeature {
	private static final Identifier FEATURE_ID = BNB.id("cubes");
	private final SDFScatter2D scatterFloor = new SDFScatter2D(this::getCubesFloor);
	private final SDFScatter2D scatterCeiling = new SDFScatter2D(this::getCubesCeiling);
	private final FractalNoise noise = new FractalNoise(PerlinNoise::new);
	private final Matrix3F matrixFloor = new Matrix3F();
	private final Matrix3F matrixCeiling = new Matrix3F();
	private final Vec3D axis = Vec3D.make(0, 0, 0);
	private final Random random = new Random();
	private int lastSeedFloor;
	private int lastSeedCeiling;
	private TerrainMap map;
	
	public CubesFeature() {
		noise.setOctaves(3);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float density = gradient(y, 112, 144, 1, -1);
		density = Math.max(density, gradient(y, 224, 256, -1, 1));
		density += noise.get(x * 0.03, z * 0.03) * 0.8F - 0.4F;
		
		if (map == null) map = BNBWorldGenerator.getMapCopy();
		
		float cubes = scatterFloor.get(x * 0.03, y * 0.03, z * 0.03);
		density = smoothMax(density, cubes, 0.5F);
		
		cubes = scatterCeiling.get(x * 0.03, y * 0.03, z * 0.03);
		density = smoothMax(density, cubes, 0.5F);
		
		return density;
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		scatterFloor.setSeed(RANDOM.nextInt());
		scatterCeiling.setSeed(RANDOM.nextInt());
		noise.setSeed(RANDOM.nextInt());
	}
	
	private float getCubesFloor(int seed, Vec3D relativePos, Vec3D worldPos) {
		int px = MCMath.floor(worldPos.x / 0.03);
		int pz = MCMath.floor(worldPos.z / 0.03);
		if (map != null && map.getData(px, pz) != FEATURE_ID) {
			return 0;
		}
		
		random.setSeed(seed);
		
		float size = MathHelper.lerp(random.nextFloat(), 0.75F, 1.0F);
		float angle = random.nextFloat() * 6.283F;
		
		relativePos.y -= (random.nextFloat() * 20 + 110) * 0.03;
		
		if (seed != lastSeedFloor) {
			lastSeedFloor = seed;
			set(axis, random.nextFloat() - 0.5F, random.nextFloat() - 0.5F, random.nextFloat() - 0.5F);
			normalize(axis);
			matrixFloor.rotation(axis, angle);
		}
		
		matrixFloor.transform(relativePos);
		float dx = Math.abs((float) relativePos.x);
		float dy = Math.abs((float) relativePos.y);
		float dz = Math.abs((float) relativePos.z);
		
		float d = Math.max(Math.max(dx, dy), dz);
		return size - d;
	}
	
	private float getCubesCeiling(int seed, Vec3D relativePos, Vec3D worldPos) {
		int px = MCMath.floor(worldPos.x / 0.03);
		int pz = MCMath.floor(worldPos.z / 0.03);
		if (map != null && map.getData(px, pz) != FEATURE_ID) {
			return 0;
		}
		
		random.setSeed(seed);
		
		float size = MathHelper.lerp(random.nextFloat(), 0.75F, 1.0F);
		float angle = random.nextFloat() * 6.283F;
		
		relativePos.y -= (250 - random.nextFloat() * 15) * 0.03;
		
		if (seed != lastSeedCeiling) {
			lastSeedCeiling = seed;
			set(axis, random.nextFloat() - 0.5F, random.nextFloat() - 0.5F, random.nextFloat() - 0.5F);
			normalize(axis);
			matrixCeiling.rotation(axis, angle);
		}
		
		matrixCeiling.transform(relativePos);
		float dx = Math.abs((float) relativePos.x);
		float dy = Math.abs((float) relativePos.y);
		float dz = Math.abs((float) relativePos.z);
		
		float d = Math.max(Math.max(dx, dy), dz);
		return size - d;
	}
	
	private static void set(Vec3D v, float x, float y, float z) {
		v.x = x;
		v.y = y;
		v.z = z;
	}
	
	private static void normalize(Vec3D v) {
		double l = v.x * v.x + v.y * v.y + v.z * v.z;
		if (l < 1E-6) return;
		l = Math.sqrt(l);
		v.x /= l;
		v.y /= l;
		v.z /= l;
	}
}
