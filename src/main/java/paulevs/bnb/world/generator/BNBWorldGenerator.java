package paulevs.bnb.world.generator;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.LightType;
import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.StationFlatteningChunk;
import paulevs.bnb.BNB;
import paulevs.bnb.listeners.BiomeListener;
import paulevs.bnb.world.biome.NetherBiome;
import paulevs.bnb.world.generator.terrain.ArchipelagoFeature;
import paulevs.bnb.world.generator.terrain.PillarsFeature;

import java.util.Random;
import java.util.stream.IntStream;

public class BNBWorldGenerator {
	private static final BlockState LAVA = BlockBase.STILL_LAVA.getDefaultState();
	private static final TerrainMap TERRAIN_MAP = new TerrainMap();
	private static final Random RANDOM = new Random();
	private static InterpolationCell[] cells1;
	private static InterpolationCell[] cells2;
	private static int sectionCount;
	
	
	public static void setSeed(long seed) {
		Random random = new Random(seed);
		TERRAIN_MAP.setSeed(random.nextInt());
	}
	
	public static Chunk makeChunk(Level level, int cx, int cz) {
		StationFlatteningChunk chunk = new StationFlatteningChunk(level, cx, cz);
		final ChunkSection[] sections = chunk.sections;
		sectionCount = sections.length;
		
		if (cells1 == null || cells1.length != sectionCount) {
			cells1 = new InterpolationCell[sectionCount];
			cells2 = new InterpolationCell[sectionCount];
			for (int i = 0; i < sectionCount; i++) {
				cells1[i] = new InterpolationCell(8);
				cells2[i] = new InterpolationCell(8, 4);
			}
		}
		
		for (short i = 0; i < sectionCount; i++) {
			cells1[i].fill(cx << 4, i << 4, cz << 4, TERRAIN_MAP);
			cells2[i].fill((cx << 4) - 4, (i << 4) - 4, (cz << 4) - 4, TERRAIN_MAP);
			if (i < 2 || !(cells1[i].isEmpty() && cells2[i].isEmpty())) sections[i] = new ChunkSection(i);
		}
		
		NetherBiome biome = BiomeListener.BIOMES.get(BNB.id("crimson_forest"));
		
		IntStream.range(0, sectionCount).parallel().forEach(i -> {
			InterpolationCell cell1 = cells1[i];
			InterpolationCell cell2 = cells2[i];
			if (i > 1 && cell1.isEmpty() && cell2.isEmpty()) return;
			for (byte bx = 0; bx < 16; bx++) {
				cell1.setX(bx);
				cell2.setX(bx + 4);
				for (byte bz = 0; bz < 16; bz++) {
					cell1.setZ(bz);
					cell2.setZ(bz + 4);
					for (byte by = 0; by < 16; by++) {
						cell1.setY(by);
						cell2.setY(by + 4);
						float density = MathHelper.lerp(0.5F, cell1.get(), cell2.get());
						if (density < 0.5F) {
							if (i > 1) continue;
							sections[i].setBlockState(bx, by, bz, LAVA);
							sections[i].setLight(LightType.field_2758, bx, by, bz, 15);
						}
						else {
							if ((by | i << 4) < 31) sections[i].setBlockState(bx, by, bz, biome.getFillBlock());
							else {
								cell1.setY(by + 1);
								cell2.setY(by + 5);
								density = MathHelper.lerp(0.5F, cell1.get(), cell2.get());
								BlockState block = density < 0.5F ? biome.getSurfaceBlock() : biome.getFillBlock();
								sections[i].setBlockState(bx, by, bz, block);
							}
						}
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
	
	/*private static TerrainSDF makeF1F3() {
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
	
	private static float gradient(float y, float minY, float maxY, float minValue, float maxValue) {
		if (y <= minY) return minValue;
		if (y >= maxY) return maxValue;
		return MathHelper.lerp((y - minY) / (maxY - minY), minValue, maxValue);
	}
	
	private static TerrainSDF makeArchipelagos() {
		return (x, y, z) -> {
			float bottom = gradient(y, 0, 100, 1, -1) + ARCHIPELAGO_NOISE.get(x * 0.01, z * 0.01);
			return bottom;
		};
	}*/
	
	static {
		//TERRAIN_MAP.addSDF(makeF1F3());
		//TERRAIN_MAP.addSDF(makeTopBottom());
		//TERRAIN_MAP.addSDF((x, y, z) -> 1F);
		
		/*TERRAIN_MAP.addSDF((x, y, z) -> {
			float gradientBottom = gradient(y, 0, 20, 1, -1);
			float gradientTop = gradient(y, 235, 255, -1, 1);
			return Math.max(gradientBottom, gradientTop);
		});*/
		//TERRAIN_MAP.addFeature(makeArchipelagos());
		
		TERRAIN_MAP.addFeature(new ArchipelagoFeature());
		TERRAIN_MAP.addFeature(new PillarsFeature());
	}
}
