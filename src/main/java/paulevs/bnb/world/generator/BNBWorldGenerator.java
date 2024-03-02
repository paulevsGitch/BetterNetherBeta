package paulevs.bnb.world.generator;

import net.minecraft.block.Block;
import net.minecraft.level.Level;
import net.minecraft.level.LightType;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.dimension.DimensionData;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.impl.world.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import net.modificationstation.stationapi.impl.worldgen.WorldDecoratorImpl;
import paulevs.bnb.world.generator.biome.BNBBiomeSource;
import paulevs.bnb.world.generator.terrain.ChunkTerrainMap;
import paulevs.bnb.world.generator.terrain.CrossInterpolationCell;
import paulevs.bnb.world.generator.terrain.features.ArchesFeature;
import paulevs.bnb.world.generator.terrain.features.ArchipelagoFeature;
import paulevs.bnb.world.generator.terrain.features.BridgesFeature;
import paulevs.bnb.world.generator.terrain.features.ContinentsFeature;
import paulevs.bnb.world.generator.terrain.features.CubesFeature;
import paulevs.bnb.world.generator.terrain.features.DoubleBridgesFeature;
import paulevs.bnb.world.generator.terrain.features.LavaOceanFeature;
import paulevs.bnb.world.generator.terrain.features.PancakesFeature;
import paulevs.bnb.world.generator.terrain.features.PillarsFeature;
import paulevs.bnb.world.generator.terrain.features.SmallPillarsFeature;
import paulevs.bnb.world.generator.terrain.features.SpikesFeature;
import paulevs.bnb.world.generator.terrain.features.TheHiveFeature;
import paulevs.bnb.world.generator.terrain.features.TheWallFeature;
import paulevs.bnb.world.generator.terrain.features.VolumetricNoiseFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class BNBWorldGenerator {
	private static final BlockState BEDROCK = Block.BEDROCK.getDefaultState();
	private static final BlockState LAVA = Block.STILL_LAVA.getDefaultState();
	private static final BlockState NETHERRACK = Block.NETHERRACK.getDefaultState();
	private static final List<ChunkTerrainMap> FEATURE_MAPS = new ArrayList<>();
	private static final Biome[] BIOMES = new Biome[256];
	private static final Random RANDOM = new Random();
	private static CrossInterpolationCell[] cells;
	private static int sectionCount;
	private static int seed;
	
	public static void updateData(DimensionData dimensionData, long seed) {
		BNBWorldGenerator.seed = new Random(seed).nextInt();
		ChunkTerrainMap.setData(dimensionData, BNBWorldGenerator.seed);
	}
	
	public static Chunk makeChunk(Level level, int cx, int cz) {
		FlattenedChunk chunk = new FlattenedChunk(level, cx, cz);
		final ChunkSection[] sections = chunk.sections;
		sectionCount = sections.length;
		
		if (cells == null || cells.length != sectionCount) {
			cells = new CrossInterpolationCell[sectionCount];
			for (int i = 0; i < sectionCount; i++) {
				cells[i] = new CrossInterpolationCell(4);
				if (i >= FEATURE_MAPS.size()) FEATURE_MAPS.add(new ChunkTerrainMap(i));
			}
		}
		
		ChunkTerrainMap.prepare(cx, cz);
		IntStream.range(0, sectionCount).parallel().forEach(i -> {
			cells[i].fill(cx << 4, i << 4, cz << 4, FEATURE_MAPS.get(i));
			if (forceSection(i) || !cells[i].isEmpty()) sections[i] = new ChunkSection(i);
		});
		
		BNBBiomeSource biomeSource = (BNBBiomeSource) level.dimension.biomeSource;
		biomeSource.fillBiomes(BIOMES, cx << 4, cz << 4, RANDOM);
		
		IntStream.range(0, sectionCount).parallel().forEach(i -> {
			CrossInterpolationCell cell = cells[i];
			if (!forceSection(i) && cell.isEmpty()) return;
			for (byte bx = 0; bx < 16; bx++) {
				cell.setX(bx);
				for (byte bz = 0; bz < 16; bz++) {
					cell.setZ(bz);
					Biome biome = BIOMES[bx << 4 | bz];
					for (byte by = 0; by < 16; by++) {
						cell.setY(by);
						if (cell.get() < 0.5F) {
							if (i > 1) continue;
							sections[i].setBlockState(bx, by, bz, LAVA);
							sections[i].setLight(LightType.BLOCK, bx, by, bz, 15);
						}
						else {
							sections[i].setBlockState(bx, by, bz, NETHERRACK);
							/*if ((by | i << 4) < 31) sections[i].setBlockState(bx, by, bz, biome.getFillBlock());
							else {
								cell.setY(by + 1);
								BlockState block = cell.get() < 0.5F ? biome.getSurfaceBlock() : biome.getFillBlock();
								sections[i].setBlockState(bx, by, bz, block);
							}*/
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
		WorldDecoratorImpl.decorate(level, cx, cz);
		/*FlattenedChunk chunk = (FlattenedChunk) level.getChunkFromCache(cx, cz);
		final ChunkSection[] sections = chunk.sections;
		
		NetherBiome biome = (NetherBiome) level.dimension.biomeSource.getBiome((cx + 1) << 4, (cz + 1) << 4);
		
		int wx = cx << 4 | 8;
		int wz = cz << 4 | 8;
		for (short cy = 0; cy < sectionCount; cy++) {
			if (sections[cy] == null) continue;
			int wy = cy << 4;
			biome.getStructures().forEach(placer -> placer.place(level, RANDOM, wx, wy, wz));
		}*/
	}
	
	private static boolean forceSection(int index) {
		return index == 15 || index < 2;
	}
	
	static {
		ChunkTerrainMap.addFeature(ArchipelagoFeature::new);
		ChunkTerrainMap.addFeature(PillarsFeature::new);
		ChunkTerrainMap.addFeature(SpikesFeature::new);
		ChunkTerrainMap.addFeature(ContinentsFeature::new);
		ChunkTerrainMap.addFeature(TheHiveFeature::new);
		ChunkTerrainMap.addFeature(CubesFeature::new);
		ChunkTerrainMap.addFeature(ArchesFeature::new);
		ChunkTerrainMap.addFeature(VolumetricNoiseFeature::new);
		ChunkTerrainMap.addFeature(LavaOceanFeature::new);
		ChunkTerrainMap.addFeature(PancakesFeature::new);
		ChunkTerrainMap.addFeature(TheWallFeature::new);
		ChunkTerrainMap.addFeature(SmallPillarsFeature::new);
		ChunkTerrainMap.addFeature(BridgesFeature::new);
		ChunkTerrainMap.addFeature(DoubleBridgesFeature::new);
	}
}
