package paulevs.bnb.listeners;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.dimension.Nether;
import net.minecraft.level.source.LevelSource;
import net.minecraft.level.structure.Structure;
import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationloader.api.common.event.level.gen.ChunkPopulator;
import paulevs.bnb.mixin.common.MinecraftServerAccessor;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.ClientUtil;
import paulevs.bnb.world.biome.NetherBiome;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Function;
import java.util.stream.IntStream;

public class ChunkListener implements ChunkPopulator {
	private FeatureGenerationThread featureGenerator;
	
	@Override
	public void populate(Level level, LevelSource levelSource, Biome biome, int startX, int startZ, Random random) {
		if (level.dimension instanceof Nether) {
			final int sx = startX + 8;
			final int sz = startZ + 8;
			Biome[] biomes = level.getBiomeSource().getBiomes(sx, sz, 16, 16);
			IntStream.range(0, 256).parallel().forEach(index -> {
				int x = index & 15;
				int z = index >> 4;
				Biome bio = biomes[x << 4 | z];
				x += sx;
				z += sz;
				if (bio instanceof NetherBiome) {
					BlockState top = ((NetherBiome) bio).getTopBlock(x, z);
					int depth = ((NetherBiome) bio).getTopDepth();
					boolean fire = ((NetherBiome) bio).hasFire();
					for (int y = 31; y < 127; y++) {
						int tile = level.getTileId(x, y, z);
						if (tile == BlockBase.NETHERRACK.id || tile == BlockBase.SOUL_SAND.id || tile == BlockBase.GRAVEL.id) {
							tile = level.getTileId(x, y + depth, z);
							if (tile == 0 || BlockBase.BY_ID[tile] == null || !BlockBase.BY_ID[tile].isFullOpaque()) {
								top.setBlockFast(level, x, y, z);
							}
							else if (tile != BlockBase.NETHERRACK.id) {
								BlockUtil.fastTilePlace(level, x, y, z, BlockBase.NETHERRACK.id, 0);
							}
						}
						else if (!fire && tile == BlockBase.FIRE.id) {
							BlockUtil.fastTilePlace(level, x, y, z, 0, 0);
						}
					}
				}
			});
			
			if (biome instanceof NetherBiome) {
				NetherBiome bio = (NetherBiome) biome;
				
				if (featureGenerator == null || !featureGenerator.isAlive()) {
					featureGenerator = new FeatureGenerationThread(random);
					featureGenerator.start();
				}
				
				int count = bio.getMaxTreeCount();
				if (count > 0) {
					featureGenerator.queue.add(new FeatureData(bio::getTree, level, count, sx, sz));
				}
				count = bio.getMaxPlantCount();
				if (count > 0) {
					featureGenerator.queue.add(new FeatureData(bio::getPlant, level, count, sx, sz));
				}
				count = bio.getMaxCeilPlantCount();
				if (count > 0) {
					featureGenerator.queue.add(new FeatureData(bio::getCeilPlant, level, count, sx, sz));
				}
				
				/*int countTrees = bio.getMaxTreeCount();
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
				}*/
			}
		}
	}
	
	private final class FeatureData {
		final Function<Random, Structure> featureGetter;
		final Level level;
		final int count;
		final int sx;
		final int sz;
		
		private FeatureData(Function<Random, Structure> featureGetter,  Level level, int count, int sx, int sz) {
			this.featureGetter = featureGetter;
			this.level = level;
			this.count = count;
			this.sx = sx;
			this.sz = sz;
		}
		
		public Structure getStructure(Random random) {
			return featureGetter.apply(random);
		}
	}
	
	private class FeatureGenerationThread extends Thread {
		private final Queue<FeatureData> queue = new ArrayBlockingQueue<>(16384);
		private final Random random;
		
		FeatureGenerationThread(Random random) {
			this.random = random;
		}
		
		public void run() {
			while (isActive()) {
				FeatureData data = queue.poll();
				if (data != null) {
					for (int sect = 2; sect < 8; sect++) {
						int sy = sect << 4;
						int maxY = sect == 7 ? 15 : 16;
						int count = random.nextInt(data.count);
						for (int i = 0; i < count; i++) {
							int px = random.nextInt(16) + data.sx;
							int pz = random.nextInt(16) + data.sz;
							for (int y = 0; y < maxY; y++) {
								int py = sy | y;
								int tile = data.level.getTileId(px, py, pz);
								if (BlockUtil.isTerrain(tile) || BlockUtil.isSoulTerrain(tile)) {
									tile = data.level.getTileId(px, py + 1, pz);
									if (BlockUtil.isNonSolidNoLava(tile)) {
										Structure structure = data.getStructure(random);
										structure.generate(data.level, random, px, py + 1, pz);
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
	
	@SuppressWarnings("deprecation")
	private boolean isActive() {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
			return ((MinecraftServerAccessor) server).bnb_isRunning();
		}
		return ClientUtil.getMinecraft().running;
	}
}
