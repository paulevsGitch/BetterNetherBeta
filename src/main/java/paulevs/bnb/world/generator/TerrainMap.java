package paulevs.bnb.world.generator;

import net.minecraft.util.maths.Vec2i;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.noise.FractalPerlin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TerrainMap implements TerrainSDF {
	private static final double SIN = Math.sin(0.8);
	private static final double COS = Math.cos(0.8);
	private static final Vec2i[] OFFSETS;
	
	private final List<TerrainSDF> densityFunctions = new ArrayList<>();
	private final Map<Integer, Integer> closestTerrain = new HashMap<>();
	private final Map<Long, byte[]> chunks = new HashMap<>();
	private final FractalPerlin distortionX = new FractalPerlin();
	private final FractalPerlin distortionZ = new FractalPerlin();
	private final float[] preData = new float[1];
	private final Random random = new Random(0);
	private int seed;
	
	public TerrainMap() {
		distortionX.setOctaves(2);
		distortionZ.setOctaves(2);
	}
	
	public void setSeed(int seed) {
		this.seed = seed;
		random.setSeed(seed);
		distortionX.setSeed(random.nextInt());
		distortionZ.setSeed(random.nextInt());
	}
	
	public void addSDF(TerrainSDF sdf) {
		densityFunctions.add(sdf);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		closestTerrain.clear();
		for (Vec2i offset : OFFSETS) {
			int index = getSDFIndex(x + offset.x, z + offset.z);
			closestTerrain.put(index, closestTerrain.getOrDefault(index, 0) + 1);
		}
		
		preData[0] = 0;
		closestTerrain.forEach((id, count) -> {
			float power = (float) count / OFFSETS.length;
			preData[0] += densityFunctions.get(id).getDensity(x, y, z) * power;
		});
		
		return preData[0];
	}
	
	public int getDebugColor(int x, int y, int z) {
		closestTerrain.clear();
		for (Vec2i offset : OFFSETS) {
			int index = getSDFIndex(x + offset.x, z + offset.z);
			closestTerrain.put(index, closestTerrain.getOrDefault(index, 0) + 1);
		}
		
		float[] rgb = new float[] {0, 0, 0};
		
		closestTerrain.forEach((id, count) -> {
			float power = (float) count / OFFSETS.length;
			if (id == 0) rgb[0] += power;
			if (id == 1) rgb[1] += power;
			if (id == 2) rgb[2] += power;
		});
		
		int r = (int) (rgb[0] * 255);
		int g = (int) (rgb[1] * 255);
		int b = (int) (rgb[2] * 255);
		
		return 255 << 24 | r << 16 | g << 8 | b;
	}
	
	private int getSDFIndex(int x, int z) {
		double preX = COS * x - SIN * z;
		double preZ = SIN * x + COS * z;
		int px = (int) Math.round(preX / 256.0 + (double) distortionX.get(preX * 0.0075, preZ * 0.0075) * 1.5F);
		int pz = (int) Math.round(preZ / 256.0 + (double) distortionZ.get(preX * 0.0075, preZ * 0.0075) * 1.5F);
		long pos = getKey(px >> 4, pz >> 4);
		byte[] chunk = chunks.computeIfAbsent(pos, p -> {
			byte[] data = new byte[256];
			random.setSeed(MathHelper.hashCode(px >> 4, seed, pz >> 4));
			for (short i = 0; i < 256; i++) {
				data[i] = (byte) random.nextInt(densityFunctions.size());
			}
			return data;
		});
		return chunk[(px & 15) << 4 | (pz & 15)];
	}
	
	private long getKey(int x, int z) {
		return (long) x << 32L | ((long) z & 0xFFFFFFFFL);
	}
	
	static {
		int radius = 5;
		List<Vec2i> offsets = new ArrayList<>();
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				if (x * x + z * z <= radius) {
					offsets.add(new Vec2i(x << 2, z << 2));
				}
			}
		}
		OFFSETS = offsets.toArray(Vec2i[]::new);
	}
}
