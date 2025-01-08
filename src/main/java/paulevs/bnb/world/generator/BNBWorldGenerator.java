package paulevs.bnb.world.generator;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.level.Level;
import net.minecraft.level.LightType;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.dimension.DimensionData;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.world.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import paulevs.bnb.BNB;
import paulevs.bnb.BNBClient;
import paulevs.bnb.world.generator.terrain.ChunkTerrainMap;
import paulevs.bnb.world.generator.terrain.CrossInterpolationCell;
import paulevs.bnb.world.generator.terrain.TerrainMap;
import paulevs.bnb.world.generator.terrain.TerrainRegion;
import paulevs.bnb.world.generator.terrain.features.ArchesFeature;
import paulevs.bnb.world.generator.terrain.features.ArchipelagoFeature;
import paulevs.bnb.world.generator.terrain.features.BigPillarsFeature;
import paulevs.bnb.world.generator.terrain.features.CubesFeature;
import paulevs.bnb.world.generator.terrain.features.FlatCliffFeature;
import paulevs.bnb.world.generator.terrain.features.FlatHillsFeature;
import paulevs.bnb.world.generator.terrain.features.FlatMountainsFeature;
import paulevs.bnb.world.generator.terrain.features.FlatOceanFeature;
import paulevs.bnb.world.generator.terrain.features.LandPillarsFeature;
import paulevs.bnb.world.generator.terrain.features.OceanPillarsFeature;
import paulevs.bnb.world.generator.terrain.features.PlainsFeature;
import paulevs.bnb.world.generator.terrain.features.RiversFeature;
import paulevs.bnb.world.generator.terrain.features.ShoreFeature;
import paulevs.bnb.world.generator.terrain.features.StalactitesFeature;
import paulevs.bnb.world.generator.terrain.features.StraightThinPillarsFeature;
import paulevs.bnb.world.generator.terrain.features.TerrainFeature;
import paulevs.bnb.world.generator.terrain.features.ThinPillarsFeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

public class BNBWorldGenerator {
	private static final List<Queue<FlattenedChunk>> CHUNKS_TO_GENERATE;
	private static final List<ChunkTerrainMap> FEATURE_MAPS;
	
	private static final List<Pair<Identifier, TerrainRegion>> MAP_FEATURES = new ArrayList<>();
	private static final BlockState NETHERRACK = Block.NETHERRACK.getDefaultState();
	private static final BlockState BEDROCK = Block.BEDROCK.getDefaultState();
	private static final BlockState LAVA = Block.STILL_LAVA.getDefaultState();
	private static final Random RANDOM = new Random();
	
	private static ThreadLocal<TerrainMap> mapCopies;
	private static volatile boolean canRun = true;
	
	public static void updateData(DimensionData dimensionData, long seed) {
		RANDOM.setSeed(seed);
		final int mapSeed = RANDOM.nextInt();
		
		int terrainSeed = RANDOM.nextInt();
		for (ChunkTerrainMap map : FEATURE_MAPS) {
			map.setSeed(terrainSeed);
		}
		
		mapCopies = ThreadLocal.withInitial(() -> {
			TerrainMap map = new TerrainMap();
			map.setData(dimensionData, mapSeed);
			MAP_FEATURES.forEach(pair -> map.addTerrain(pair.getFirst(), pair.getSecond()));
			return map;
		});
	}
	
	public static Chunk makeChunk(Level level, int cx, int cz) {
		FlattenedChunk chunk = new FlattenedChunk(level, cx, cz);
		CHUNKS_TO_GENERATE.get((cx + cz) & 7).add(chunk);
		chunk.decorated = true;
		return chunk;
	}
	
	private static void fillBlocksData(int startX, int startZ, int index, byte[] section, CrossInterpolationCell cell, ChunkTerrainMap featureMap) {
		Arrays.fill(section, (byte) 0);
		
		cell.fill(startX, index << 4, startZ, featureMap);
		if (cell.isEmpty() && index > 5) return;
		
		for (byte bx = 0; bx < 16; bx++) {
			cell.setX(bx);
			for (byte bz = 0; bz < 16; bz++) {
				cell.setZ(bz);
				for (byte by = 0; by < 16; by++) {
					cell.setY(by);
					int pos = getIndex(bx, by, bz);
					if (cell.get() < 0.5F) {
						if (index > 5) continue;
						section[pos] = 3;
					}
					else {
						section[pos] = 1;
					}
				}
			}
		}
		
		Random random = new Random(MathHelper.hashCode(startX >> 4, index, startZ >> 4));
		if (index == 0) {
			for (byte bx = 0; bx < 16; bx++) {
				for (byte bz = 0; bz < 16; bz++) {
					section[getIndex(bx, 0, bz)] = 4;
					if (random.nextInt(2) == 0) {
						section[getIndex(bx, 1, bz)] = 4;
					}
				}
			}
		}
		else if (index == 15) {
			for (byte bx = 0; bx < 16; bx++) {
				for (byte bz = 0; bz < 16; bz++) {
					section[getIndex(bx, 15, bz)] = 4;
					if (random.nextInt(2) == 0) {
						section[getIndex(bx, 14, bz)] = 4;
					}
				}
			}
		}
	}
	
	private static void fixGenerationErrors(byte[][] blockSections) {
		for (byte i = 0; i < 16; i++) {
			byte[] blocks = blockSections[i];
			for (short n = 0; n < 4096; n++) {
				if (blocks[n] != 1) continue;
				byte x = (byte) (n & 15);
				byte z = (byte) ((n >> 4) & 15);
				boolean	hasSupport = n >= 256 ? blocks[n - 256] > 1 : i == 0 || blockSections[i - 1][n + 3840] > 1;
				hasSupport = hasSupport || x == 0 || blocks[n - 1] > 1;
				hasSupport = hasSupport || x == 15 || blocks[n + 1] > 1;
				hasSupport = hasSupport || z == 0 || blocks[n - 16] > 1;
				hasSupport = hasSupport || z == 15 || blocks[n + 16] > 1;
				if (!hasSupport) continue;
				blocks[n] = 2;
			}
		}
		
		for (byte i = 15; i >= 0; i--) {
			byte[] blocks = blockSections[i];
			for (short n = 4095; n >= 0; n--) {
				if (blocks[n] != 1) continue;
				byte x = (byte) (n & 15);
				byte z = (byte) ((n >> 4) & 15);
				boolean	hasSupport = n < 3840 ? blocks[n + 256] > 1 : i == 15 || blockSections[i + 1][n & 255] > 1;
				hasSupport = hasSupport || x == 0 || blocks[n - 1] > 1;
				hasSupport = hasSupport || x == 15 || blocks[n + 1] > 1;
				hasSupport = hasSupport || z == 0 || blocks[n - 16] > 1;
				hasSupport = hasSupport || z == 15 || blocks[n + 16] > 1;
				if (!hasSupport) continue;
				blocks[n] = 2;
			}
		}
	}
	
	private static ChunkSection fillSection(int index, byte[] blocks) {
		ChunkSection section = new ChunkSection(index);
		
		for (short i = 0; i < 4096; i++) {
			if (blocks[i] < 2) continue;
			byte x = (byte) (i & 15);
			byte z = (byte) ((i >> 4) & 15);
			byte y = (byte) (i >> 8);
			BlockState state = switch (blocks[i]) {
				case 3 -> LAVA;
				case 4 -> BEDROCK;
				default -> NETHERRACK;
			};
			section.setBlockState(x, y, z, state);
			if (blocks[i] == 3) {
				section.setLight(LightType.BLOCK, x, y, z, 15);
			}
		}
		
		return section;
	}
	
	public static TerrainMap getMapCopy() {
		return mapCopies.get();
	}
	
	private static int getIndex(int x, int y, int z) {
		return y << 8 | z << 4 | x;
	}
	
	private static void addFeature(Identifier id, Supplier<TerrainFeature> constructor, TerrainRegion... regions) {
		ChunkTerrainMap.addFeature(id, constructor);
		for (TerrainRegion region : regions) {
			MAP_FEATURES.add(new Pair<>(id, region));
		}
	}
	
	private static final BNBWorldDecoratorThread DECORATOR_THREAD = new BNBWorldDecoratorThread();
	
	public static void tick() {
		DECORATOR_THREAD.updateMain(BNBClient.getMinecraft());
	}
	
	public static void stop() {
		DECORATOR_THREAD.stopThread();
		canRun = false;
	}
	
	static {
		addFeature(BNB.id("plains"), PlainsFeature::new, TerrainRegion.PLAINS);
		addFeature(BNB.id("arches"), ArchesFeature::new, TerrainRegion.PLAINS);
		addFeature(BNB.id("flat_hills"), FlatHillsFeature::new, TerrainRegion.HILLS);
		addFeature(BNB.id("flat_mountains"), FlatMountainsFeature::new, TerrainRegion.MOUNTAINS);
		addFeature(BNB.id("shore"), ShoreFeature::new, TerrainRegion.SHORE_NORMAL);
		addFeature(BNB.id("flat_ocean"), FlatOceanFeature::new, TerrainRegion.OCEAN_NORMAL, TerrainRegion.OCEAN_MOUNTAINS, TerrainRegion.BRIDGES);
		addFeature(BNB.id("archipelago"), ArchipelagoFeature::new, TerrainRegion.OCEAN_MOUNTAINS);
		addFeature(BNB.id("flat_cliff"), FlatCliffFeature::new, TerrainRegion.SHORE_MOUNTAINS);
		addFeature(BNB.id("cubes"), CubesFeature::new, TerrainRegion.HILLS, TerrainRegion.MOUNTAINS);
		addFeature(BNB.id("ocean_pillars"), OceanPillarsFeature::new, TerrainRegion.OCEAN_MOUNTAINS);
		addFeature(BNB.id("land_pillars"), LandPillarsFeature::new, TerrainRegion.MOUNTAINS);
		
		ChunkTerrainMap.addCommonFeature(BigPillarsFeature::new);
		ChunkTerrainMap.addCommonFeature(ThinPillarsFeature::new);
		ChunkTerrainMap.addCommonFeature(StalactitesFeature::new);
		ChunkTerrainMap.addCommonFeature(StraightThinPillarsFeature::new);
		ChunkTerrainMap.addCommonFeature(RiversFeature::new);
		
		mapCopies = ThreadLocal.withInitial(() -> {
			TerrainMap map = new TerrainMap();
			MAP_FEATURES.forEach(pair -> map.addTerrain(pair.getFirst(), pair.getSecond()));
			return map;
		});
		
		CHUNKS_TO_GENERATE = new ArrayList<>(8);
		FEATURE_MAPS = new ArrayList<>(8);
		for (byte n = 0; n < 8; n++) {
			CHUNKS_TO_GENERATE.add(new ConcurrentLinkedQueue<>());
			FEATURE_MAPS.add(new ChunkTerrainMap());
		}
		
		for (byte n = 0; n < 8; n++) {
			final Queue<FlattenedChunk> generateQueue = CHUNKS_TO_GENERATE.get(n);
			final byte[][] blockSections = new byte[16][4096];
			final CrossInterpolationCell[] cells = new CrossInterpolationCell[16];
			final ChunkTerrainMap featureMap = FEATURE_MAPS.get(n);
			
			for (byte i = 0; i < 16; i++) {
				cells[i] = new CrossInterpolationCell(8);
			}
			
			Thread thread = new Thread(() -> {
				while (canRun) {
					FlattenedChunk chunk = generateQueue.poll();
					if (chunk == null) continue;
					int startX = chunk.x << 4;
					int startZ = chunk.z << 4;
					
					featureMap.prepare(startX, startZ);
					
					for (byte index = 0; index < 16; index++) {
						fillBlocksData(
							startX,
							startZ,
							index,
							blockSections[index],
							cells[index],
							featureMap
						);
					}
					
					fixGenerationErrors(blockSections);
					
					for (byte index = 0; index < 16; index++) {
						if (cells[index].isEmpty()) chunk.sections[index] = new ChunkSection(index);
						else chunk.sections[index] = fillSection(index, blockSections[index]);
					}
					
					BNBWorldChunk.cast(chunk).bnb_setStatus(BNBChunkStatus.TERRAIN);
				}
			});
			thread.setName("BNB Chunk Generator " + n);
			thread.start();
		}
		
		DECORATOR_THREAD.start();
	}
}
