package paulevs.bnb.world.generator.biome;

import net.minecraft.level.dimension.DimensionData;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.NBTIO;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.world.biome.NetherBiome;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BiomeMap {
	private static final double SIN = Math.sin(0.8);
	private static final double COS = Math.cos(0.8);
	
	private final Map<Long, byte[]> chunks = new HashMap<>();
	private final FractalNoise distortionX = new FractalNoise(PerlinNoise::new);
	private final FractalNoise distortionY = new FractalNoise(PerlinNoise::new);
	private final FractalNoise distortionZ = new FractalNoise(PerlinNoise::new);
	private final Random random = new Random(0);
	private final List<NetherBiome> biomes;
	private File folder;
	private int seed;
	
	public BiomeMap(List<NetherBiome> biomes) {
		distortionX.setOctaves(2);
		distortionY.setOctaves(2);
		distortionZ.setOctaves(2);
		this.biomes = biomes;
	}
	
	public void setData(DimensionData data, int seed) {
		this.seed = seed;
		random.setSeed(seed);
		distortionX.setSeed(random.nextInt());
		distortionY.setSeed(random.nextInt());
		distortionZ.setSeed(random.nextInt());
		folder = new File(data.getFile("").getParentFile(), "bnb_biomes");
		if (!folder.exists()) folder.mkdirs();
	}
	
	public NetherBiome getBiome(int x, int y, int z) {
		double preX = COS * x - SIN * z;
		double preZ = SIN * x + COS * z;
		
		double dx = preX * 0.0075;
		double dy = preX * 0.0075;
		double dz = preX * 0.0075;
		
		int px = (int) Math.round(preX / 256.0 + distortionX.get(dx, dy, dz) * 1.5F);
		int py = (int) Math.round(y / 256.0 + distortionY.get(dx, dy, dz) * 1.5F);
		int pz = (int) Math.round(preZ / 256.0 + distortionZ.get(dx, dy, dz) * 1.5F);
		
		long pos = getKey(px >> 4, pz >> 4);
		byte[] chunk = chunks.computeIfAbsent(pos, p -> {
			byte[] data = null;
			boolean loaded = false;
			
			File file = new File(folder, "chunk_" + px + "_" + pz + ".nbt");
			if (file.exists()) {
				try {
					FileInputStream fileInputStream = new FileInputStream(file);
					CompoundTag tag = NBTIO.readGzipped(fileInputStream);
					data = tag.getByteArray("biomes");
					fileInputStream.close();
					loaded = true;
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (!loaded) {
				data = new byte[4096];
				
				random.setSeed(MathHelper.hashCode(px >> 4, seed, pz >> 4));
				for (short i = 0; i < 4096; i++) {
					data[i] = (byte) random.nextInt(biomes.size());
				}
				
				CompoundTag tag = new CompoundTag();
				tag.put("biomes", data);
				
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
		return null;
		//int index =
		//return chunk[(px & 15) << 4 | (pz & 15)];
	}
	
	private long getKey(int x, int z) {
		return (long) x << 32L | ((long) z & 0xFFFFFFFFL);
	}
}
