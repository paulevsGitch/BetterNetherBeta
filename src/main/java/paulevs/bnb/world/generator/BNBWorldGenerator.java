package paulevs.bnb.world.generator;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.LightType;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.dimension.DimensionData;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.StationFlatteningChunk;
import paulevs.bnb.BNB;
import paulevs.bnb.listeners.BiomeListener;
import paulevs.bnb.world.biome.NetherBiome;
import paulevs.bnb.world.generator.terrain.ArchesFeature;
import paulevs.bnb.world.generator.terrain.ArchipelagoFeature;
import paulevs.bnb.world.generator.terrain.ContinentsFeature;
import paulevs.bnb.world.generator.terrain.CubesFeature;
import paulevs.bnb.world.generator.terrain.LavaOceanFeature;
import paulevs.bnb.world.generator.terrain.PancakesFeature;
import paulevs.bnb.world.generator.terrain.PillarsFeature;
import paulevs.bnb.world.generator.terrain.SmallPillarsFeature;
import paulevs.bnb.world.generator.terrain.SpikesFeature;
import paulevs.bnb.world.generator.terrain.TheHiveFeature;
import paulevs.bnb.world.generator.terrain.TheWallFeature;
import paulevs.bnb.world.generator.terrain.VolumetricNoiseFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class BNBWorldGenerator {
	private static final BlockState BEDROCK = BlockBase.BEDROCK.getDefaultState();
	private static final BlockState LAVA = BlockBase.STILL_LAVA.getDefaultState();
	private static final List<ChunkFeatureMap> FEATURE_MAPS = new ArrayList<>();
	private static final Random RANDOM = new Random();
	private static CrossInterpolationCell[] cells;
	private static int sectionCount;
	private static int seed;
	
	public static void updateData(DimensionData dimensionData, long seed) {
		BNBWorldGenerator.seed = new Random(seed).nextInt();
		ChunkFeatureMap.setData(dimensionData, BNBWorldGenerator.seed);
	}
	
	public static Chunk makeChunk(Level level, int cx, int cz) {
		StationFlatteningChunk chunk = new StationFlatteningChunk(level, cx, cz);
		final ChunkSection[] sections = chunk.sections;
		sectionCount = sections.length;
		
		if (cells == null || cells.length != sectionCount) {
			cells = new CrossInterpolationCell[sectionCount];
			for (int i = 0; i < sectionCount; i++) {
				cells[i] = new CrossInterpolationCell(4);
				if (i >= FEATURE_MAPS.size()) FEATURE_MAPS.add(new ChunkFeatureMap(i));
			}
		}
		
		ChunkFeatureMap.prepare(cx, cz);
		IntStream.range(0, sectionCount).parallel().forEach(i -> {
			cells[i].fill(cx << 4, i << 4, cz << 4, FEATURE_MAPS.get(i));
			if (forceSection(i) || !cells[i].isEmpty()) sections[i] = new ChunkSection(i);
		});
		
		NetherBiome biome = BiomeListener.BIOMES.get(BNB.id("crimson_forest"));
		
		IntStream.range(0, sectionCount).parallel().forEach(i -> {
			CrossInterpolationCell cell = cells[i];
			if (!forceSection(i) && cell.isEmpty()) return;
			for (byte bx = 0; bx < 16; bx++) {
				cell.setX(bx);
				for (byte bz = 0; bz < 16; bz++) {
					cell.setZ(bz);
					for (byte by = 0; by < 16; by++) {
						cell.setY(by);
						if (cell.get() < 0.5F) {
							if (i > 1) continue;
							sections[i].setBlockState(bx, by, bz, LAVA);
							sections[i].setLight(LightType.field_2758, bx, by, bz, 15);
						}
						else {
							if ((by | i << 4) < 31) sections[i].setBlockState(bx, by, bz, biome.getFillBlock());
							else {
								cell.setY(by + 1);
								BlockState block = cell.get() < 0.5F ? biome.getSurfaceBlock() : biome.getFillBlock();
								sections[i].setBlockState(bx, by, bz, block);
							}
						}
					}
				}
			}
		});
		
		for (byte bx = 0; bx < 16; bx++) {
			for (byte bz = 0; bz < 16; bz++) {
				sections[0].setBlockState(bx, 0, bz, BEDROCK);
				sections[15].setBlockState(bx, 15, bz, BEDROCK);
				if (RANDOM.nextInt(2) == 0) sections[0].setBlockState(bx, 1, bz, BEDROCK);
				if (RANDOM.nextInt(2) == 0) sections[15].setBlockState(bx, 14, bz, BEDROCK);
			}
		}
		
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
	
	private static boolean forceSection(int index) {
		return index == 15 || index < 2;
	}
	
	static {
		ChunkFeatureMap.addFeature(ArchipelagoFeature::new);
		ChunkFeatureMap.addFeature(PillarsFeature::new);
		ChunkFeatureMap.addFeature(SpikesFeature::new);
		ChunkFeatureMap.addFeature(ContinentsFeature::new);
		ChunkFeatureMap.addFeature(TheHiveFeature::new);
		ChunkFeatureMap.addFeature(CubesFeature::new);
		ChunkFeatureMap.addFeature(ArchesFeature::new);
		ChunkFeatureMap.addFeature(VolumetricNoiseFeature::new);
		ChunkFeatureMap.addFeature(LavaOceanFeature::new);
		ChunkFeatureMap.addFeature(PancakesFeature::new);
		ChunkFeatureMap.addFeature(TheWallFeature::new);
		ChunkFeatureMap.addFeature(SmallPillarsFeature::new);
	}
}
