package paulevs.bnb.world.generator.terrain.features;

import net.minecraft.util.maths.Vec3D;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.math.Matrix3F;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.SDFScatter2D;

import java.util.Random;

public class CubesFeature extends TerrainFeature {
	private final SDFScatter2D scatter1 = new SDFScatter2D(this::getCubesBig);
	private final SDFScatter2D scatter2 = new SDFScatter2D(this::getCubesSmall);
	private final SDFScatter2D scatter3 = new SDFScatter2D(this::getCubesCeiling);
	private final FractalNoise floor = new FractalNoise(PerlinNoise::new);
	private final FractalNoise ceiling = new FractalNoise(PerlinNoise::new);
	private final Matrix3F matrix1 = new Matrix3F();
	private final Matrix3F matrix2 = new Matrix3F();
	private final Matrix3F matrix3 = new Matrix3F();
	private final Vec3D axis = Vec3D.make(0, 0, 0);
	private final Random random = new Random();
	private int lastSeed1;
	private int lastSeed2;
	private int lastSeed3;
	
	public CubesFeature() {
		floor.setOctaves(3);
		ceiling.setOctaves(3);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float density = gradient(y, 5, 130, 0.7F, -1.0F) + floor.get(x * 0.01, z * 0.01);
		
		float cubes = scatter1.get(x * 0.01, y * 0.01, z * 0.01);
		float cubes2 = scatter2.get(x * 0.04, y * 0.04, z * 0.04);
		cubes = Math.max(cubes, cubes2);
		density = smoothMax(density, cubes, 0.1F);
		
		float ceil = gradient(y, 200, 255, -1.0F, 1.0F) + ceiling.get(x * 0.01, z * 0.01);
		cubes = scatter3.get(x * 0.03, y * 0.03, z * 0.03);
		ceil = smoothMax(ceil, cubes, 0.1F);
		density = Math.max(density, ceil);
		
		return density;
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		scatter1.setSeed(RANDOM.nextInt());
		scatter2.setSeed(RANDOM.nextInt());
		scatter3.setSeed(RANDOM.nextInt());
		floor.setSeed(RANDOM.nextInt());
		ceiling.setSeed(RANDOM.nextInt());
	}
	
	private float getCubesBig(int seed, Vec3D pos) {
		random.setSeed(seed);
		
		float size = MathHelper.lerp(random.nextFloat(), 0.75F, 1.0F);
		float angle = random.nextFloat() * 6.283F;
		
		pos.y -= (random.nextFloat() * 100 + 30) * 0.01;
		
		if (seed != lastSeed1) {
			lastSeed1 = seed;
			set(axis, random.nextFloat() - 0.5F, random.nextFloat() - 0.5F, random.nextFloat() - 0.5F);
			normalize(axis);
			matrix1.rotation(axis, angle);
		}
		
		matrix1.transform(pos);
		float dx = Math.abs((float) pos.x);
		float dy = Math.abs((float) pos.y);
		float dz = Math.abs((float) pos.z);
		
		float d = Math.max(Math.max(dx, dy), dz);
		return size - d;
	}
	
	private float getCubesSmall(int seed, Vec3D pos) {
		random.setSeed(seed);
		
		float size = MathHelper.lerp(random.nextFloat(), 0.75F, 1.0F);
		float angle = random.nextFloat() * 6.283F;
		
		pos.y -= (random.nextFloat() * 30 + 30) * 0.04;
		
		if (seed != lastSeed2) {
			lastSeed2 = seed;
			set(axis, random.nextFloat() - 0.5F, random.nextFloat() - 0.5F, random.nextFloat() - 0.5F);
			normalize(axis);
			matrix2.rotation(axis, angle);
		}
		
		matrix2.transform(pos);
		float dx = Math.abs((float) pos.x);
		float dy = Math.abs((float) pos.y);
		float dz = Math.abs((float) pos.z);
		
		float d = Math.max(Math.max(dx, dy), dz);
		return size - d;
	}
	
	private float getCubesCeiling(int seed, Vec3D pos) {
		random.setSeed(seed);
		
		float size = MathHelper.lerp(random.nextFloat(), 0.75F, 1.0F);
		float angle = random.nextFloat() * 6.283F;
		
		pos.y -= (240 - random.nextFloat() * 20) * 0.03;
		
		if (seed != lastSeed3) {
			lastSeed3 = seed;
			set(axis, random.nextFloat() - 0.5F, random.nextFloat() - 0.5F, random.nextFloat() - 0.5F);
			normalize(axis);
			matrix3.rotation(axis, angle);
		}
		
		matrix3.transform(pos);
		float dx = Math.abs((float) pos.x);
		float dy = Math.abs((float) pos.y);
		float dz = Math.abs((float) pos.z);
		
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
