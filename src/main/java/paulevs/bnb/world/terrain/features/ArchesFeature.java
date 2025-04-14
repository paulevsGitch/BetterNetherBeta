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

public class ArchesFeature extends TerrainFeature {
	private static final Identifier FEATURE_ID = BNB.id("arches");
	private final SDFScatter2D scatter = new SDFScatter2D(this::getArches);
	private final FractalNoise noise = new FractalNoise(PerlinNoise::new);
	private final PerlinNoise distortionX = new PerlinNoise();
	private final PerlinNoise distortionY = new PerlinNoise();
	private final PerlinNoise distortionZ = new PerlinNoise();
	private final Matrix3F matrix = new Matrix3F();
	private final Vec3D axis = Vec3D.make(0, 0, 0);
	private final Random random = new Random();
	private TerrainMap map;
	private int lastSeed;
	
	public ArchesFeature() {
		noise.setOctaves(5);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float density = gradient(y, 96, 128, 1, -1);
		density = Math.max(density, gradient(y, 224, 256, -1, 1));
		density += noise.get(x * 0.03, z * 0.03) * 0.8F - 0.4F;
		
		float arches = scatter.get(x * 0.03, y * 0.03, z * 0.03);
		arches += noise.get(x * 0.02, y * 0.02, z * 0.02) * 0.1F;
		density = smoothMax(density, arches, 0.75F);
		
		return density;
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		scatter.setSeed(RANDOM.nextInt());
		noise.setSeed(RANDOM.nextInt());
		distortionX.setSeed(RANDOM.nextInt());
		distortionY.setSeed(RANDOM.nextInt());
		distortionZ.setSeed(RANDOM.nextInt());
	}
	
	private float getArches(int seed, Vec3D relativePos, Vec3D worldPos) {
		int px = MCMath.floor(worldPos.x / 0.03);
		int pz = MCMath.floor(worldPos.z / 0.03);
		if (map == null) map = BNBWorldGenerator.getMapCopy();
		if (map != null && map.getData(px, pz) != FEATURE_ID) {
			return 0;
		}
		
		random.setSeed(seed);
		
		float radiusBig = MathHelper.lerp(random.nextFloat(), 0.1F, 0.75F);
		float radiusSmall = MathHelper.lerp(random.nextFloat(), radiusBig * 0.13F, radiusBig * 0.25F);
		float angle = random.nextFloat() * 6.283F;
		
		relativePos.y -= (random.nextFloat() * 20 + 96) * 0.03;
		
		if (seed != lastSeed) {
			lastSeed = seed;
			set(axis, random.nextFloat() * 0.2F - 0.1F, 1F, random.nextFloat() * 0.2F - 0.1F);
			normalize(axis);
			matrix.rotation(axis, angle);
		}
		
		matrix.transform(relativePos);
		
		float dx = seed / 300.0F;
		float dy = seed / 600.0F;
		float dz = seed / 900.0F;
		
		float x = (float) (relativePos.x + distortionX.get(relativePos.x + dx, relativePos.y + dy, relativePos.z + dz) * 0.3F - 0.15F);
		float y = (float) (relativePos.y + distortionY.get(relativePos.x + dx, relativePos.y + dy, relativePos.z + dz) * 0.3F - 0.15F);
		float z = (float) (relativePos.z + distortionZ.get(relativePos.x + dx, relativePos.y + dy, relativePos.z + dz) * 0.3F - 0.15F);
		
		return 0.5F - torus(x, y, z, radiusBig, radiusSmall);
	}
	
	float torus(float x, float y, float z, float radiusBig, float radiusSmall) {
		float l = MCMath.sqrt(x * x + y * y);
		l = l - radiusBig;
		l = MCMath.sqrt(l * l + z * z);
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
