package paulevs.bnb.world.generator.biome;

import net.minecraft.level.biome.Biome;
import net.minecraft.level.dimension.DimensionData;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.NBTIO;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.noise.PerlinNoise;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BiomeMap {
	private static final HexConverter CONVERTER = new HexConverter();
	private static final double SIN = Math.sin(0.8);
	private static final double COS = Math.cos(0.8);
	
	private final Map<Long, byte[]> chunks = new HashMap<>();
	private final PerlinNoise distortionX = new PerlinNoise();
	private final PerlinNoise distortionZ = new PerlinNoise();
	private final Random random = new Random(0);
	private final Biome[] biomes;
	private final File folder;
	private final int seed;
	
	public BiomeMap(Biome[] biomes, long seed, DimensionData data) {
		this.biomes = biomes;
		random.setSeed(seed);
		this.seed = random.nextInt();
		distortionX.setSeed(random.nextInt());
		distortionZ.setSeed(random.nextInt());
		folder = new File(data.getFile("").getParentFile(), "bnb_biomes");
		if (!folder.exists()) folder.mkdirs();
	}
	
	public Biome getBiome(int x, int z) {
		double preX = COS * x / 24.0 - SIN * z / 24.0;
		double preZ = SIN * x / 24.0 + COS * z / 24.0;
		preX += distortionX.getRange(x * 0.1, z * 0.1, -0.15F, 0.15F);
		preZ += distortionZ.getRange(x * 0.1, z * 0.1, -0.15F, 0.15F);
		
		CONVERTER.update(preX, preZ);
		int px = CONVERTER.getCellX();
		int pz = CONVERTER.getCellZ();
		
		if ((px & 63) == 63 && (pz & 3) == 1) px++;
		if ((pz & 63) == 63 && (px & 3) == 1) pz++;
		
		long pos = getKey(px >> 6, pz >> 6);
		byte[] chunk = chunks.computeIfAbsent(pos, this::makeOrLoad);
		
		int index = (px & 63) << 6 | (pz & 63);
		return biomes[chunk[index]];
	}
	
	private long getKey(int x, int z) {
		return (long) x << 32L | ((long) z & 0xFFFFFFFFL);
	}
	
	private byte[] makeOrLoad(long pos) {
		int x = (int) (pos >> 32L);
		int z = (int) (pos & 0xFFFFFFFFL);
		byte[] data = null;
		
		File file = new File(folder, "chunk_" + x + "_" + z + ".nbt");
		if (file.exists()) {
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				CompoundTag tag = NBTIO.readGzipped(fileInputStream);
				data = tag.getByteArray("biomes");
				fileInputStream.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (data == null) {
			data = generateBiomeChunk(x, z);
			
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
	}
	
	private byte[] generateBiomeChunk(int x, int z) {
		random.setSeed(MathHelper.hashCode(x >> 4, seed, z >> 4));
		
		byte[][] buffer = new byte[2][4096];
		
		byte[] source = buffer[0];
		byte[] result = buffer[1];
		
		Arrays.fill(source, (byte) -1);
		Arrays.fill(result, (byte) -1);
		
		byte[] startData = new byte[64];
		for (byte i = 0; i < 64; i++) {
			startData[i] = (byte) random.nextInt(biomes.length);
			if (i > 0 && startData[i - 1] == startData[i]) {
				startData[i] = (byte) random.nextInt(biomes.length);
			}
			if (i > 8 && startData[i - 8] == startData[i]) {
				startData[i] = (byte) random.nextInt(biomes.length);
			}
		}
		
		for (byte i = 0; i < 8; i++) {
			for (byte j = 0; j < 8; j++) {
				short px = (short) (i << 3 | random.nextInt(8));
				short pz = (short) (j << 3 | random.nextInt(8));
				source[px << 6 | pz] = startData[i << 3 | j];
			}
		}
		
		short[] neighbours = new short[6];
		boolean shouldFill = true;
		
		for (short n = 0; n < 64 && shouldFill; n++) {
			source = buffer[n & 1];
			result = buffer[(n + 1) & 1];
			shouldFill = false;
			
			for (short i = 64; i < 4032; i++) {
				if (source[i] != -1) {
					result[i] = source[i];
					continue;
				}
				
				x = i & 63;
				if (x == 0 || x == 63) continue;
				
				System.arraycopy(NEIGHBOURS[i & 1], 0, neighbours, 0, 6);
				for (byte j1 = 0; j1 < 6; j1++) {
					byte j2 = (byte) random.nextInt(6);
					short value = neighbours[j1];
					neighbours[j1] = neighbours[j2];
					neighbours[j2] = value;
				}
				
				for (byte j = 0; j < 3; j++) {
					short index = (short) (i + neighbours[j]);
					if (source[index] != -1) {
						result[i] = source[index];
						shouldFill = true;
						break;
					}
				}
			}
		}
		
		for (byte i = 0; i < 64; i++) {
			result[i] = result[i + 64];
			result[i + 4032] = result[i + 3968];
		}
		
		for (byte i = 1; i < 63; i++) {
			short index = (short) (i << 6);
			result[index] = result[index + 1];
			result[index + 63] = result[index + 62];
		}
		
		for (short i = 0; i < 4096; i++) {
			if (result[i] == -1) result[i] = 0;
		}
		
		return result;
	}
	
	private static final short[][] NEIGHBOURS;
	
	static {
		NEIGHBOURS = new short[2][6];
		byte side = 64;
		
		NEIGHBOURS[1][0] = 1;
		NEIGHBOURS[1][1] = -1;
		NEIGHBOURS[1][2] = side;
		NEIGHBOURS[1][3] = (short) -side;
		NEIGHBOURS[1][4] = (short) (side + 1);
		NEIGHBOURS[1][5] = (short) (side - 1);
		
		NEIGHBOURS[0][0] = 1;
		NEIGHBOURS[0][1] = -1;
		NEIGHBOURS[0][2] = side;
		NEIGHBOURS[0][3] = (short) -side;
		NEIGHBOURS[0][4] = (short) (-side + 1);
		NEIGHBOURS[0][5] = (short) (-side - 1);
	}
}
