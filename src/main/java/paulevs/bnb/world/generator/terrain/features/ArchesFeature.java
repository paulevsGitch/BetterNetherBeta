package paulevs.bnb.world.generator.terrain.features;

import net.minecraft.util.maths.Vec3D;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.math.Matrix3F;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.SDFScatter2D;

import java.util.Random;

public class ArchesFeature extends TerrainFeature {
	private final SDFScatter2D scatter = new SDFScatter2D(this::getArches);
	private final FractalNoise floor = new FractalNoise(PerlinNoise::new);
	private final FractalNoise ceiling = new FractalNoise(PerlinNoise::new);
	private final PerlinNoise distortionX = new PerlinNoise();
	private final PerlinNoise distortionY = new PerlinNoise();
	private final PerlinNoise distortionZ = new PerlinNoise();
	private final Matrix3F matrix = new Matrix3F();
	private final Vec3D axis = Vec3D.make(0, 0, 0);
	private final Random random = new Random();
	private int lastSeed;
	
	public ArchesFeature() {
		floor.setOctaves(5);
		ceiling.setOctaves(3);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float density = gradient(y, 5, 130, 0.6F, -1.0F) + floor.get(x * 0.003, z * 0.003);
		
		float arches = scatter.get(x * 0.01, y * 0.01, z * 0.01);
		arches += ceiling.get(x * 0.02, y * 0.02, z * 0.02) * 0.1F;
		density = smoothMax(density, arches, 0.1F);
		
		float ceil = gradient(y, 200, 255, -1.0F, 1.0F) + ceiling.get(x * 0.01, z * 0.01);
		density = Math.max(density, ceil);
		
		return density;
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		scatter.setSeed(RANDOM.nextInt());
		floor.setSeed(RANDOM.nextInt());
		ceiling.setSeed(RANDOM.nextInt());
		distortionX.setSeed(RANDOM.nextInt());
		distortionY.setSeed(RANDOM.nextInt());
		distortionZ.setSeed(RANDOM.nextInt());
	}
	
	private float getArches(int seed, Vec3D pos) {
		random.setSeed(seed);
		
		float radiusBig = MathHelper.lerp(random.nextFloat(), 0.1F, 0.5F);
		float radiusSmall = MathHelper.lerp(random.nextFloat(), radiusBig * 0.02F, radiusBig * 0.2F);
		float angle = random.nextFloat() * 6.283F;
		
		pos.y -= (random.nextFloat() * 30 + 40) * 0.01;
		
		if (seed != lastSeed) {
			lastSeed = seed;
			set(axis, random.nextFloat() * 0.2F - 0.1F, 1F, random.nextFloat() * 0.2F - 0.1F);
			normalize(axis);
			matrix.rotation(axis, angle);
		}
		
		matrix.transform(pos);
		
		float dx = seed / 300.0F;
		float dy = seed / 600.0F;
		float dz = seed / 900.0F;
		
		float x = (float) (pos.x + distortionX.get(pos.x + dx, pos.y + dy, pos.z + dz) * 0.3F - 0.15F);
		float y = (float) (pos.y + distortionY.get(pos.x + dx, pos.y + dy, pos.z + dz) * 0.3F - 0.15F);
		float z = (float) (pos.z + distortionZ.get(pos.x + dx, pos.y + dy, pos.z + dz) * 0.3F - 0.15F);
		
		return 0.5F - torus(x, y, z, radiusBig, radiusSmall);
	}
	
	float torus(float x, float y, float z, float radiusBig, float radiusSmall) {
		float l = net.minecraft.util.maths.MathHelper.sqrt(x * x + y * y);
		l = l - radiusBig;
		l = net.minecraft.util.maths.MathHelper.sqrt(l * l + z * z);
		return l - radiusSmall;
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
