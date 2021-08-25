package paulevs.bnb.listeners;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.dimension.Nether;
import net.minecraft.level.source.LevelSource;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationloader.api.common.event.level.gen.ChunkPopulator;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.MHelper;
import paulevs.bnb.world.biome.NetherBiome;
import paulevs.bnb.world.structures.NetherStructures;

import java.util.Random;

public class ChunkListener implements ChunkPopulator {
	@Override
	public void populate(Level level, LevelSource levelSource, Biome biome, int startX, int startZ, Random random) {
		if (level.dimension instanceof Nether) {
			Chunk chunk = level.getChunk(startX, startZ);
			Biome[] biomes = level.getBiomeSource().getBiomes(startX, startZ, 16, 16);
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					Biome bio = biomes[x << 4 | z];
					if (bio instanceof NetherBiome) {
						BlockState top = ((NetherBiome) bio).getTopBlock(x, z);
						int depth = ((NetherBiome) bio).getTopDepth();
						boolean fire = ((NetherBiome) bio).hasFire();
						for (int y = 31; y < 127; y++) {
							int tile = chunk.getTileId(x, y, z);
							if (tile == BlockBase.NETHERRACK.id || tile == BlockBase.SOUL_SAND.id || tile == BlockBase.GRAVEL.id) {
								tile = chunk.getTileId(x, y + depth, z);
								if (tile == 0 || BlockBase.BY_ID[tile] == null || !BlockBase.BY_ID[tile].isFullOpaque()) {
									top.setBlock(chunk, x, y, z);
								}
								else if (tile != BlockBase.NETHERRACK.id) {
									chunk.setTileWithMetadata(x, y, z, BlockBase.NETHERRACK.id, 0);
								}
							}
							else if (!fire && tile == BlockBase.FIRE.id) {
								chunk.setTileWithMetadata(x, y, z, 0, 0);
							}
						}
					}
				}
			}
			
			if (biome instanceof NetherBiome) {
				NetherBiome bio = (NetherBiome) biome;
				int countTrees = bio.getMaxTreeCount();
				int countPlants = bio.getMaxPlantCount();
				int countCeilPlants = bio.getMaxCeilPlantCount();
				
				Structure structure;
				
				for (int sect = 0; sect < 8; sect++) {
					int sy = sect << 4;
					int minY = sect < 2 ? 16 : 0;
					int maxY = sect == 7 ? 15 : 16;
					int count;
					
					count = random.nextInt(4);
					for (int i = 0; i < count; i++) {
						int px = random.nextInt(16);
						int pz = random.nextInt(16);
						int py = random.nextInt(16) | sy;
						int tile = chunk.getTileId(px, py, pz);
						if (tile == BlockBase.NETHERRACK.id) {
							NetherStructures.ORICHALCUM_ORE.generate(level, random, px | startX, py, pz | startZ);
						}
					}
					
					if (countTrees > 0) {
						count = random.nextInt(countTrees);
						for (int i = 0; i < count; i++) {
							int px = random.nextInt(16);
							int pz = random.nextInt(16);
							for (int y = minY; y < maxY; y++) {
								int py = sy | y;
								int tile = chunk.getTileId(px, py, pz);
								if (BlockUtil.isTerrain(tile) || BlockUtil.isSoulTerrain(tile)) {
									tile = chunk.getTileId(px, py + 1, pz);
									if (BlockUtil.isNonSolidNoLava(tile)) {
										structure = bio.getTree(random);
										structure.generate(level, random, px | startX, py + 1, pz | startZ);
										break;
									}
								}
							}
						}
					}
					
					if (countPlants > 0) {
						count = MHelper.randRange(countPlants >> 1, countPlants, random);
						for (int i = 0; i < count; i++) {
							int px = random.nextInt(16);
							int pz = random.nextInt(16);
							for (int y = minY; y < maxY; y++) {
								int py = sy | y;
								int tile = chunk.getTileId(px, py, pz);
								if (BlockUtil.isTerrain(tile) || BlockUtil.isSoulTerrain(tile)) {
									tile = chunk.getTileId(px, py + 1, pz);
									if (BlockUtil.isNonSolidNoLava(tile)) {
										structure = bio.getPlant(random);
										structure.generate(level, random, px | startX, py + 1, pz | startZ);
										break;
									}
								}
							}
						}
					}
					
					if (countCeilPlants > 0) {
						count = random.nextInt(countCeilPlants);
						for (int i = 0; i < count; i++) {
							int px = random.nextInt(16);
							int pz = random.nextInt(16);
							for (int y = minY; y < maxY; y++) {
								int py = sy | y;
								int tile = chunk.getTileId(px, py, pz);
								if (BlockBase.BY_ID[tile] != null && BlockBase.BY_ID[tile].isFullCube()) {
									tile = chunk.getTileId(px, py - 1, pz);
									if (BlockUtil.isNonSolidNoLava(tile)) {
										structure = bio.getCeilPlant(random);
										structure.generate(level, random, px | startX, py - 1, pz | startZ);
										break;
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
