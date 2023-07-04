package paulevs.bnb.world.generator;

import net.minecraft.level.dimension.DimensionData;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.NBTIO;
import net.minecraft.util.maths.Vec2i;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TerrainMap {
	private static final double SIN = Math.sin(0.8);
	private static final double COS = Math.cos(0.8);
	private static final Vec2i[] OFFSETS;
	
	private final Map<Long, byte[]> chunks = new HashMap<>();
	private final FractalNoise distortionX = new FractalNoise(PerlinNoise::new);
	private final FractalNoise distortionZ = new FractalNoise(PerlinNoise::new);
	private final Random random = new Random(0);
	private File folder;
	private int seed;
	
	public TerrainMap() {
		distortionX.setOctaves(2);
		distortionZ.setOctaves(2);
	}
	
	public void setData(DimensionData data, int seed) {
		this.seed = seed;
		random.setSeed(seed);
		distortionX.setSeed(random.nextInt());
		distortionZ.setSeed(random.nextInt());
		folder = new File(data.getFile("").getParentFile(), "bnb_terrain");
		if (!folder.exists()) folder.mkdirs();
	}
	
	public void getDensity(int x, int z, float[] data, int count) {
		Arrays.fill(data, 0F);
		for (Vec2i offset : OFFSETS) {
			int index = getSDFIndex(x + offset.x, z + offset.z, count);
			data[index] += 1;
		}
		for (short i = 0; i < data.length; i++) {
			if (data[i] == 0) continue;
			data[i] /= (float) OFFSETS.length;
		}
	}
	
	private int getSDFIndex(int x, int z, int count) {
		double preX = COS * x - SIN * z;
		double preZ = SIN * x + COS * z;
		int px = (int) Math.round(preX / 256.0 + (double) distortionX.get(preX * 0.0075, preZ * 0.0075) * 1.5F);
		int pz = (int) Math.round(preZ / 256.0 + (double) distortionZ.get(preX * 0.0075, preZ * 0.0075) * 1.5F);
		long pos = getKey(px >> 4, pz >> 4);
		byte[] chunk = chunks.computeIfAbsent(pos, p -> {
			byte[] data = null;
			boolean loaded = false;
			
			File file = new File(folder, "chunk_" + px + "_" + pz + ".nbt");
			if (file.exists()) {
				try {
					FileInputStream fileInputStream = new FileInputStream(file);
					CompoundTag tag = NBTIO.readGzipped(fileInputStream);
					data = tag.getByteArray("terrain");
					fileInputStream.close();
					loaded = true;
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (!loaded) {
				data = new byte[256];
				
				random.setSeed(MathHelper.hashCode(px >> 4, seed, pz >> 4));
				for (short i = 0; i < 256; i++) {
					data[i] = (byte) random.nextInt(count);
				}
				
				CompoundTag tag = new CompoundTag();
				tag.put("terrain", data);
				
				try {
					FileOutputStream fileOutputStream = new FileOutputStream(file);
					NBTIO.writeGzipped(tag, fileOutputStream);
					fileOutputStream.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
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
