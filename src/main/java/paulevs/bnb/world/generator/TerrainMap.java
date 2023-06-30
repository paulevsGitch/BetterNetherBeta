package paulevs.bnb.world.generator;

import net.minecraft.util.maths.Vec2i;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.world.generator.terrain.TerrainFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TerrainMap implements TerrainSDF {
	private static final double SIN = Math.sin(0.8);
	private static final double COS = Math.cos(0.8);
	private static final Vec2i[] OFFSETS;
	
	private final List<TerrainFeature> features = new ArrayList<>();
	private final Map<Integer, Integer> closestTerrain = new HashMap<>();
	private final Map<Long, byte[]> chunks = new HashMap<>();
	private final FractalNoise distortionX = new FractalNoise(PerlinNoise::new);
	private final FractalNoise distortionZ = new FractalNoise(PerlinNoise::new);
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
		features.forEach(feature -> feature.setSeed(random.nextInt()));
	}
	
	public void addFeature(TerrainFeature feature) {
		features.add(feature);
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
			preData[0] += features.get(id).getDensity(x, y, z) * power;
		});
		
		return preData[0];
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
				data[i] = (byte) random.nextInt(features.size());
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
		System.out.println("OFFSETS: " + offsets.size());
	}
}
