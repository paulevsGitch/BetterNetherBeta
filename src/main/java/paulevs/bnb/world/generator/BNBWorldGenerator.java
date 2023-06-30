package paulevs.bnb.world.generator;

import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.StationFlatteningChunk;
import paulevs.bnb.BNB;
import paulevs.bnb.listeners.BiomeListener;
import paulevs.bnb.noise.VoronoiNoise;
import paulevs.bnb.world.biome.NetherBiome;

import java.util.Random;
import java.util.stream.IntStream;

public class BNBWorldGenerator {
	private static final TerrainMap TERRAIN_MAP = new TerrainMap();
	private static final Random RANDOM = new Random();
	private static InterpolationCell[] cells;
	private static int sectionCount;
	//private static int chunkX;
	//private static int chunkZ;
	
	private static final VoronoiNoise NOISE = new VoronoiNoise();
	
	public static void setSeed(long seed) {
		Random random = new Random(seed);
		TERRAIN_MAP.setSeed(random.nextInt());
		NOISE.setSeed(random.nextInt());
	}
	
	public static Chunk makeChunk(Level level, int cx, int cz) {
		StationFlatteningChunk chunk = new StationFlatteningChunk(level, cx, cz);
		final ChunkSection[] sections = chunk.sections;
		sectionCount = sections.length;
		//chunkX = cx;
		//chunkZ = cz;
		
		if (cells == null || cells.length != sectionCount) {
			cells = new InterpolationCell[sectionCount];
			for (int i = 0; i < sectionCount; i++) {
				cells[i] = new InterpolationCell(4);
			}
		}
		
		for (short i = 0; i < sectionCount; i++) {
			cells[i].fill(cx << 4, i << 4, cz << 4, TERRAIN_MAP);
			if (!cells[i].isEmpty()) sections[i] = new ChunkSection(i);
		}
		
		NetherBiome biome = BiomeListener.BIOMES.get(BNB.id("crimson_forest"));
		
		IntStream.range(0, sectionCount).parallel().forEach(i -> {
			InterpolationCell cell = cells[i];
			if (cell.isEmpty()) return;
			for (byte bx = 0; bx < 16; bx++) {
				cell.setX(bx);
				for (byte bz = 0; bz < 16; bz++) {
					cell.setZ(bz);
					for (byte by = 0; by < 16; by++) {
						cell.setY(by);
						if (cell.get() < 0.5F) continue;
						cell.setY(by + 1);
						BlockState block = cell.get() < 0.5F ? biome.getSurfaceBlock() : biome.getFillBlock();
						sections[i].setBlockState(bx, by, bz, block);
					}
				}
			}
		});
		
		return chunk;
	}
	
	public static void decorateChunk(Level level, int cx, int cz) {
		StationFlatteningChunk chunk = (StationFlatteningChunk) level.getChunkFromCache(cx, cz);
		final ChunkSection[] sections = chunk.sections;
		
		NetherBiome biome = BiomeListener.BIOMES.get(BNB.id("crimson_forest"));
		
		int wx = cx << 4 | 8;
		int wz = cz << 4 | 8;
		for (short cy = 0; cy < sectionCount; cy++) {
			if (sections[cy] == null) continue;
			int wy = cy << 4;
			biome.getStructures().forEach(placer -> placer.place(level, RANDOM, wx, wy, wz));
		}
	}
	
	private static TerrainSDF makeF1F3() {
		return (x, y, z) -> 1.0F - (1.3F - NOISE.getF1F3(x * 0.02, y * 0.02, z * 0.02));
	}
	
	private static TerrainSDF makeTopBottom() {
		return (x, y, z) -> {
			float value = -1;
			if (y < 20F) value = MathHelper.lerp(y / 20F, 1, -1F);
			if (y > 236F) value = MathHelper.lerp((y - 236) / 20F, -1, 1F);
			return value;
		};
	}
	
	static {
		TERRAIN_MAP.addSDF(makeF1F3());
		TERRAIN_MAP.addSDF(makeTopBottom());
		TERRAIN_MAP.addSDF((x, y, z) -> 1F);
	}
}
