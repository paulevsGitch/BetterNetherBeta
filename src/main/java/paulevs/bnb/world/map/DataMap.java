package paulevs.bnb.world.map;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.level.dimension.DimensionData;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.NBTIO;
import paulevs.bnb.BNB;
import paulevs.bnb.BNBClient;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public abstract class DataMap<T> {
	protected static final double SIN = Math.sin(0.8);
	protected static final double COS = Math.cos(0.8);
	
	protected final Random random = new Random(0);
	protected final FractalNoise distortionX = new FractalNoise(PerlinNoise::new);
	protected final FractalNoise distortionZ = new FractalNoise(PerlinNoise::new);
	
	private final Long2ObjectMap<MapChunk<T>> chunks = new Long2ObjectOpenHashMap<>();
	private final String dataKey;
	private File folder;
	
	public DataMap(String dataKey) {
		distortionX.setOctaves(2);
		distortionZ.setOctaves(2);
		this.dataKey = dataKey;
	}
	
	protected abstract String serialize(T value);
	protected abstract T deserialize(String name);
	public abstract T generateData(int x, int z);
	
	public void setData(DimensionData data, int seed) {
		random.setSeed(seed);
		distortionX.setSeed(random.nextInt());
		distortionZ.setSeed(random.nextInt());
		folder = new File(data.getFile("").getParentFile(), dataKey);
		if (!folder.exists()) {
			//noinspection ResultOfMethodCallIgnored
			folder.mkdirs();
		}
	}
	
	public void setSeed(int seed) {
		random.setSeed(seed);
		distortionX.setSeed(random.nextInt());
		distortionZ.setSeed(random.nextInt());
		System.out.println("Set server seed");
	}
	
	public T getData(int x, int z) {
		double preX = (COS * x - SIN * z) / 16.0 + distortionX.get(x * 0.03, z * 0.03) * 1.5F;
		double preZ = (SIN * x + COS * z) / 16.0 + distortionX.get(x * 0.03, z * 0.03) * 1.5F;
		
		int px = (int) Math.floor(preX);
		int pz = (int) Math.floor(preZ);
		
		float dx = (float) (preX - px);
		float dz = (float) (preZ - pz);
		
		T a = getChunkData(px, pz);
		
		if (dx < 0.333F && dz < 0.333F) {
			T b = getChunkData(px - 1, pz - 1);
			T c = getChunkData(px - 1, pz);
			T d = getChunkData(px, pz - 1);
			if (b == c && c == d) {
				float v = dx + dz;
				return v < 0.333F ? c : a;
			}
		}
		
		if (dx > 0.666F && dz < 0.333F) {
			T b = getChunkData(px + 1, pz - 1);
			T c = getChunkData(px + 1, pz);
			T d = getChunkData(px, pz - 1);
			if (b == c && c == d) {
				float v = (1.0F - dx) + dz;
				return v < 0.333F ? c : a;
			}
		}
		
		if (dx < 0.333F && dz > 0.666F) {
			T b = getChunkData(px - 1, pz + 1);
			T c = getChunkData(px - 1, pz);
			T d = getChunkData(px, pz + 1);
			if (b == c && c == d) {
				float v = dx + (1.0F - dz);
				return v < 0.333F ? c : a;
			}
		}
		
		if (dx > 0.666F && dz > 0.666F) {
			T b = getChunkData(px + 1, pz + 1);
			T c = getChunkData(px + 1, pz);
			T d = getChunkData(px, pz + 1);
			if (b == c && c == d) {
				float v = (1.0F - dx) + (1.0F - dz);
				return v < 0.333F ? c : a;
			}
		}
		
		return a;
	}
	
	private T getChunkData(int x, int z) {
		return getChunk(x >> 6, z >> 6).get(getIndex(x, z));
	}
	
	private void generateChunk(MapChunk<T> chunk, int cx, int cz) {
		int wx = cx << 6;
		int wz = cz << 6;
		for (short i = 0; i < 4096; i++) {
			int posX = wx | (i >> 6);
			int posZ = wz | (i & 63);
			chunk.set(i, generateData(posX, posZ));
		}
	}
	
	protected MapChunk<T> getChunk(int cx, int cz) {
		return chunks.computeIfAbsent(getKey(cx, cz), p -> {
			MapChunk<T> chunk = new MapChunk<>();
			
			if (folder == null) {
				generateChunk(chunk, cx, cz);
				onRemoteDataGen(p);
				return chunk;
			}
			
			boolean loaded = false;
			
			File file = new File(folder, "chunk_" + cx + "_" + cz + ".nbt");
			if (file.exists()) {
				try {
					FileInputStream fileInputStream = new FileInputStream(file);
					CompoundTag tag = NBTIO.readGzipped(fileInputStream);
					loaded = chunk.load(tag, this::deserialize);
					fileInputStream.close();
				}
				catch (IOException e) {
					BNB.LOGGER.warn("Failed to load region " + cx + " " + cz + " for map " + dataKey + ", reason: " + e.getLocalizedMessage());
				}
			}
			
			if (!loaded) {
				generateChunk(chunk, cx, cz);
				
				CompoundTag tag = new CompoundTag();
				chunk.save(tag, this::serialize);
				
				try {
					FileOutputStream fileOutputStream = new FileOutputStream(file);
					NBTIO.writeGzipped(tag, fileOutputStream);
					fileOutputStream.flush();
					fileOutputStream.close();
				}
				catch (IOException e) {
					BNB.LOGGER.warn("Failed to save region " + cx + " " + cz + " for map " + dataKey + ", reason: " + e.getLocalizedMessage());
				}
			}
			
			return chunk;
		});
	}
	
	protected void onRemoteDataGen(long position) {}
	
	private int getIndex(int x, int z) {
		return (x & 63) << 6 | (z & 63);
	}
	
	private long getKey(int x, int z) {
		return (long) x << 32L | ((long) z & 0xFFFFFFFFL);
	}
}
