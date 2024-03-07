package paulevs.bnb.world.generator;

import net.minecraft.block.Block;
import net.minecraft.level.Level;
import net.minecraft.level.LightType;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.dimension.DimensionData;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.world.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import net.modificationstation.stationapi.impl.worldgen.WorldDecoratorImpl;
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
	private static final Random RANDOM = new Random();
	private static CrossInterpolationCell[] cells;
	private static ChunkSection[] sections;
	private static int startX;
	private static int startZ;
	private static int seed;
	
	public static void updateData(DimensionData dimensionData, long seed) {
		BNBWorldGenerator.seed = new Random(seed).nextInt();
		ChunkTerrainMap.setData(dimensionData, BNBWorldGenerator.seed);
	}
	
	public static Chunk makeChunk(Level level, int cx, int cz) {
		FlattenedChunk chunk = new FlattenedChunk(level, cx, cz);
		sections = chunk.sections;
		startX = cx << 4;
		startZ = cz << 4;
		
		if (cells == null || cells.length != sections.length) {
			cells = new CrossInterpolationCell[sections.length];
			for (int i = 0; i < sections.length; i++) {
				cells[i] = new CrossInterpolationCell(4);
				if (i >= FEATURE_MAPS.size()) FEATURE_MAPS.add(new ChunkTerrainMap(i));
			}
		}
		
		ChunkTerrainMap.prepare(cx, cz);
		IntStream.range(0, sections.length).parallel().forEach(BNBWorldGenerator::initSection);
		IntStream.range(0, sections.length).parallel().forEach(BNBWorldGenerator::fillSection);
		
		RANDOM.setSeed(MathHelper.hashCode(cx, 0, cz));
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
	
	private static void initSection(int index) {
		cells[index].fill(startX, index << 4, startZ, FEATURE_MAPS.get(index));
		if (forceSection(index) || !cells[index].isEmpty()) sections[index] = new ChunkSection(index);
	}
	
	private static void fillSection(int index) {
		CrossInterpolationCell cell = cells[index];
		if (!forceSection(index) && cell.isEmpty()) return;
		ChunkSection section = sections[index];
		for (byte bx = 0; bx < 16; bx++) {
			cell.setX(bx);
			for (byte bz = 0; bz < 16; bz++) {
				cell.setZ(bz);
				for (byte by = 0; by < 16; by++) {
					cell.setY(by);
					section.setLight(LightType.SKY, bx, by, bz, 0);
					if (cell.get() < 0.5F) {
						if (index > 1) continue;
						section.setBlockState(bx, by, bz, LAVA);
						section.setLight(LightType.BLOCK, bx, by, bz, 15);
					}
					else {
						section.setBlockState(bx, by, bz, NETHERRACK);
					}
				}
			}
		}
	}
	
	public static void decorateChunk(Level level, int cx, int cz) {
		WorldDecoratorImpl.decorate(level, cx, cz);
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
