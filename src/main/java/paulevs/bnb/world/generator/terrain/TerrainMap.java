package paulevs.bnb.world.generator.terrain;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.level.dimension.DimensionData;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.NBTIO;
import net.minecraft.util.maths.Vec2I;
import paulevs.bnb.BNB;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.VoronoiNoise;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

public class TerrainMap {
	private static final double SIN = Math.sin(0.8);
	private static final double COS = Math.cos(0.8);
	private static final Vec2I[] OFFSETS;
	
	private final EnumMap<TerrainRegion, ByteList> regionTerrain = new EnumMap<>(TerrainRegion.class);
	private final Long2ObjectMap<byte[]> chunks = new Long2ObjectOpenHashMap<>();
	private final FractalNoise distortionX = new FractalNoise(PerlinNoise::new);
	private final FractalNoise distortionZ = new FractalNoise(PerlinNoise::new);
	private final FractalNoise oceanNoise = new FractalNoise(PerlinNoise::new);
	private final FractalNoise mountainNoise = new FractalNoise(PerlinNoise::new);
	private final VoronoiNoise bridgesNoise = new VoronoiNoise();
	private final VoronoiNoise cellNoise = new VoronoiNoise();
	private final Random random = new Random(0);
	private File folder;
	private int seed;
	
	public TerrainMap() {
		distortionX.setOctaves(2);
		distortionZ.setOctaves(2);
		oceanNoise.setOctaves(3);
		mountainNoise.setOctaves(2);
	}
	
	public void setData(DimensionData data, int seed) {
		this.seed = seed;
		random.setSeed(seed);
		distortionX.setSeed(random.nextInt());
		distortionZ.setSeed(random.nextInt());
		oceanNoise.setSeed(random.nextInt());
		mountainNoise.setSeed(random.nextInt());
		bridgesNoise.setSeed(random.nextInt());
		cellNoise.setSeed(random.nextInt());
		folder = new File(data.getFile("").getParentFile(), "bnb_terrain");
		if (!folder.exists()) folder.mkdirs();
	}
	
	public void getDensity(int x, int z, float[] data) {
		Arrays.fill(data, 0F);
		for (Vec2I offset : OFFSETS) {
			int index = getSDFIndex(x + offset.x, z + offset.z);
			data[index] += 1;
		}
		for (short i = 0; i < data.length; i++) {
			if (data[i] == 0) continue;
			data[i] /= (float) OFFSETS.length;
		}
	}
	
	public int getSDFIndex(int x, int z) {
		double preX = (COS * x - SIN * z) / 16.0 + distortionX.get(x * 0.03, z * 0.03) * 1.5F;
		double preZ = (SIN * x + COS * z) / 16.0 + distortionX.get(x * 0.03, z * 0.03) * 1.5F;
		
		int px = (int) Math.floor(preX);
		int pz = (int) Math.floor(preZ);
		
		float dx = (float) (preX - px);
		float dz = (float) (preZ - pz);
		
		int a = getSDF(px, pz);
		
		if (dx < 0.333F && dz < 0.333F) {
			int b = getSDF(px - 1, pz - 1);
			int c = getSDF(px - 1, pz);
			int d = getSDF(px, pz - 1);
			if (b == c && c == d) {
				float v = dx + dz;
				return v < 0.333F ? c : a;
			}
		}
		
		if (dx > 0.666F && dz < 0.333F) {
			int b = getSDF(px + 1, pz - 1);
			int c = getSDF(px + 1, pz);
			int d = getSDF(px, pz - 1);
			if (b == c && c == d) {
				float v = (1.0F - dx) + dz;
				return v < 0.333F ? c : a;
			}
		}
		
		if (dx < 0.333F && dz > 0.666F) {
			int b = getSDF(px - 1, pz + 1);
			int c = getSDF(px - 1, pz);
			int d = getSDF(px, pz + 1);
			if (b == c && c == d) {
				float v = dx + (1.0F - dz);
				return v < 0.333F ? c : a;
			}
		}
		
		if (dx > 0.666F && dz > 0.666F) {
			int b = getSDF(px + 1, pz + 1);
			int c = getSDF(px + 1, pz);
			int d = getSDF(px, pz + 1);
			if (b == c && c == d) {
				float v = (1.0F - dx) + (1.0F - dz);
				return v < 0.333F ? c : a;
			}
		}
		
		return a;
	}
	
	private int getSDF(int x, int z) {
		return getChunk(x >> 6, z >> 6)[getIndex(x, z)];
	}
	
	private byte[] getChunk(int cx, int cz) {
		return chunks.computeIfAbsent(getKey(cx, cz), p -> {
			byte[] data = null;
			boolean loaded = false;
			
			File file = new File(folder, "chunk_" + cx + "_" + cz + ".nbt");
			if (file.exists()) {
				try {
					FileInputStream fileInputStream = new FileInputStream(file);
					CompoundTag tag = NBTIO.readGzipped(fileInputStream);
					data = tag.getByteArray("terrain");
					fileInputStream.close();
					loaded = data.length == 4096;
					if (!loaded) {
						BNB.LOGGER.warn("Terrain region " + cx + " " + cz + " contain corrupted data and will be regenerated");
					}
				}
				catch (IOException e) {
					BNB.LOGGER.warn("Failed to load terrain region " + cx + " " + cz + ", reason: " + e.getLocalizedMessage());
				}
			}
			
			if (!loaded) {
				data = new byte[4096];
				int wx = cx << 6;
				int wz = cz << 6;
				for (short i = 0; i < 4096; i++) {
					int posX = wx | (i >> 6);
					int posZ = wz | (i & 63);
					TerrainRegion region = getRegionInternal(posX, posZ);
					ByteList list = regionTerrain.get(region);
					if (list == null) {
						data[i] = 0;
						continue;
					}
					byte id = (byte) Math.floor(cellNoise.getID(posX * 0.25, posZ * 0.25) * list.size());
					data[i] = list.getByte(id);
				}
				
				CompoundTag tag = new CompoundTag();
				tag.put("terrain", data);
				
				try {
					FileOutputStream fileOutputStream = new FileOutputStream(file);
					NBTIO.writeGzipped(tag, fileOutputStream);
					fileOutputStream.close();
				}
				catch (IOException e) {
					BNB.LOGGER.warn("Failed to save terrain region " + cx + " " + cz + ", reason: " + e.getLocalizedMessage());
				}
			}
			
			return data;
		});
	}
	
	public TerrainRegion getRegion(int x, int z) {
		double preX = (COS * x - SIN * z) / 16.0 + distortionX.get(x * 0.03, z * 0.03) * 1.5F;
		double preZ = (SIN * x + COS * z) / 16.0 + distortionX.get(x * 0.03, z * 0.03) * 1.5F;
		
		int px = (int) Math.floor(preX);
		int pz = (int) Math.floor(preZ);
		
		float dx = (float) (preX - px);
		float dz = (float) (preZ - pz);
		
		TerrainRegion a = getRegionInternal(px, pz);
		
		if (dx < 0.333F && dz < 0.333F) {
			TerrainRegion b = getRegionInternal(px - 1, pz - 1);
			TerrainRegion c = getRegionInternal(px - 1, pz);
			TerrainRegion d = getRegionInternal(px, pz - 1);
			if (b == c && c == d) {
				float v = dx + dz;
				return v < 0.333F ? c : a;
			}
		}
		
		if (dx > 0.666F && dz < 0.333F) {
			TerrainRegion b = getRegionInternal(px + 1, pz - 1);
			TerrainRegion c = getRegionInternal(px + 1, pz);
			TerrainRegion d = getRegionInternal(px, pz - 1);
			if (b == c && c == d) {
				float v = (1.0F - dx) + dz;
				return v < 0.333F ? c : a;
			}
		}
		
		if (dx < 0.333F && dz > 0.666F) {
			TerrainRegion b = getRegionInternal(px - 1, pz + 1);
			TerrainRegion c = getRegionInternal(px - 1, pz);
			TerrainRegion d = getRegionInternal(px, pz + 1);
			if (b == c && c == d) {
				float v = dx + (1.0F - dz);
				return v < 0.333F ? c : a;
			}
		}
		
		if (dx > 0.666F && dz > 0.666F) {
			TerrainRegion b = getRegionInternal(px + 1, pz + 1);
			TerrainRegion c = getRegionInternal(px + 1, pz);
			TerrainRegion d = getRegionInternal(px, pz + 1);
			if (b == c && c == d) {
				float v = (1.0F - dx) + (1.0F - dz);
				return v < 0.333F ? c : a;
			}
		}
		
		return a;
	}
	
	private TerrainRegion getRegionInternal(int x, int z) {
		float ocean = oceanNoise.get(x * 0.05, z * 0.05);
		float mountains = mountainNoise.get(x * 0.1, z * 0.1);
		if (ocean > 0.5F) {
			double px = x * 0.03 + distortionX.get(x * 0.02, z * 0.02);
			double pz = z * 0.03 + distortionX.get(x * 0.02, z * 0.02);
			float bridges = bridgesNoise.getF1F2(px, pz);
			if (bridges > 0.9) return TerrainRegion.BRIDGES;
			if (ocean > 0.55F) {
				return mountains > 0.6F ? TerrainRegion.OCEAN_MOUNTAINS : TerrainRegion.OCEAN_NORMAL;
			}
			return mountains > 0.6F ? TerrainRegion.SHORE_MOUNTAINS : TerrainRegion.SHORE_NORMAL;
		}
		return mountains > 0.6F ? TerrainRegion.MOUNTAINS : mountains > 0.5F ? TerrainRegion.HILLS : TerrainRegion.PLAINS;
	}
	
	private int getIndex(int x, int z) {
		return (x & 63) << 6 | (z & 63);
	}
	
	private long getKey(int x, int z) {
		return (long) x << 32L | ((long) z & 0xFFFFFFFFL);
	}
	
	public void addTerrain(byte terrainID, TerrainRegion region) {
		regionTerrain.computeIfAbsent(region, r -> new ByteArrayList()).add(terrainID);
	}
	
	static {
		int radius = 5;
		List<Vec2I> offsets = new ArrayList<>();
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				if (x * x + z * z <= radius) {
					offsets.add(new Vec2I(x << 2, z << 2));
				}
			}
		}
		OFFSETS = offsets.toArray(Vec2I[]::new);
	}
}
