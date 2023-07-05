package paulevs.bnb.world.generator.terrain;

import net.minecraft.util.maths.Vec3f;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
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
	private final Matrix4f matrix1 = new Matrix4f();
	private final Matrix4f matrix2 = new Matrix4f();
	private final Matrix4f matrix3 = new Matrix4f();
	private final Vector3f axis = new Vector3f();
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
	
	private float getCubesBig(int seed, Vec3f pos) {
		random.setSeed(seed);
		
		float size = MathHelper.lerp(random.nextFloat(), 0.75F, 1.0F);
		float angle = random.nextFloat() * 6.283F;
		
		pos.y -= (random.nextFloat() * 100 + 30) * 0.01;
		
		float x = (float) pos.x;
		float y = (float) pos.y;
		float z = (float) pos.z;
		
		if (seed != lastSeed1) {
			lastSeed1 = seed;
			
			axis.set(random.nextFloat() - 0.5F, random.nextFloat() - 0.5F, random.nextFloat() - 0.5F);
			axis.normalise();
			
			matrix1.setIdentity();
			Matrix4f.rotate(angle, axis, matrix1, matrix1);
			
			x = matrix1.m00 * (float) pos.x + matrix1.m10 * (float) pos.y + matrix1.m20 * (float) pos.z;
			y = matrix1.m01 * (float) pos.x + matrix1.m11 * (float) pos.y + matrix1.m21 * (float) pos.z;
			z = matrix1.m02 * (float) pos.x + matrix1.m12 * (float) pos.y + matrix1.m22 * (float) pos.z;
		}
		
		float dx = Math.abs(x);
		float dy = Math.abs(y);
		float dz = Math.abs(z);
		
		float d = Math.max(Math.max(dx, dy), dz);
		return size - d;
	}
	
	private float getCubesSmall(int seed, Vec3f pos) {
		random.setSeed(seed);
		
		float size = MathHelper.lerp(random.nextFloat(), 0.75F, 1.0F);
		float angle = random.nextFloat() * 6.283F;
		
		pos.y -= (random.nextFloat() * 30 + 30) * 0.04;
		
		float x = (float) pos.x;
		float y = (float) pos.y;
		float z = (float) pos.z;
		
		if (seed != lastSeed2) {
			lastSeed2 = seed;
			
			axis.set(random.nextFloat() - 0.5F, random.nextFloat() - 0.5F, random.nextFloat() - 0.5F);
			axis.normalise();
			
			matrix2.setIdentity();
			Matrix4f.rotate(angle, axis, matrix2, matrix2);
			
			x = matrix2.m00 * (float) pos.x + matrix2.m10 * (float) pos.y + matrix2.m20 * (float) pos.z;
			y = matrix2.m01 * (float) pos.x + matrix2.m11 * (float) pos.y + matrix2.m21 * (float) pos.z;
			z = matrix2.m02 * (float) pos.x + matrix2.m12 * (float) pos.y + matrix2.m22 * (float) pos.z;
		}
		
		float dx = Math.abs(x);
		float dy = Math.abs(y);
		float dz = Math.abs(z);
		
		float d = Math.max(Math.max(dx, dy), dz);
		return size - d;
	}
	
	private float getCubesCeiling(int seed, Vec3f pos) {
		random.setSeed(seed);
		
		float size = MathHelper.lerp(random.nextFloat(), 0.75F, 1.0F);
		float angle = random.nextFloat() * 6.283F;
		
		pos.y -= (240 - random.nextFloat() * 20) * 0.03;
		
		float x = (float) pos.x;
		float y = (float) pos.y;
		float z = (float) pos.z;
		
		if (seed != lastSeed3) {
			lastSeed3 = seed;
			
			axis.set(random.nextFloat() - 0.5F, random.nextFloat() - 0.5F, random.nextFloat() - 0.5F);
			axis.normalise();
			
			matrix3.setIdentity();
			Matrix4f.rotate(angle, axis, matrix3, matrix3);
			
			x = matrix3.m00 * (float) pos.x + matrix3.m10 * (float) pos.y + matrix3.m20 * (float) pos.z;
			y = matrix3.m01 * (float) pos.x + matrix3.m11 * (float) pos.y + matrix3.m21 * (float) pos.z;
			z = matrix3.m02 * (float) pos.x + matrix3.m12 * (float) pos.y + matrix3.m22 * (float) pos.z;
		}
		
		float dx = Math.abs(x);
		float dy = Math.abs(y);
		float dz = Math.abs(z);
		
		float d = Math.max(Math.max(dx, dy), dz);
		return size - d;
	}
}
