package paulevs.bnb.listeners;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.dimension.Nether;
import net.minecraft.level.source.LevelSource;
import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationloader.api.common.event.level.gen.ChunkPopulator;
import paulevs.bnb.mixin.common.MinecraftServerAccessor;
import paulevs.bnb.noise.OpenSimplexNoise;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.ClientUtil;
import paulevs.bnb.util.MHelper;
import paulevs.bnb.world.biome.NetherBiome;
import paulevs.bnb.world.structures.NetherStructures;
import paulevs.bnb.world.structures.StructureInstance;

import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.IntStream;

public class ChunkListener implements ChunkPopulator {
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(10);
	private static final BlockState GRAVEL = new BlockState();
	private StructureGenerationThread featureGenerator;
	private Thread main;
	
	@Override
	public void populate(Level level, LevelSource levelSource, Biome biome, int startX, int startZ, Random random) {
		if (level.dimension instanceof Nether) {
			if (GRAVEL.getBlockID() == 0) {
				GRAVEL.setBlock(BlockBase.GRAVEL);
			}
			if (main == null) {
				main = Thread.currentThread();
			}
			final int sx = startX + 8;
			final int sz = startZ + 8;
			level.getChunk(sx, sz);
			level.getChunk(sx + 16, sz);
			level.getChunk(sx, sz + 16);
			level.getChunk(sx + 16, sz + 16);
			Biome[] biomes = level.getBiomeSource().getBiomes(sx, sz, 16, 16);
			IntStream.range(0, 256).parallel().forEach(index -> {
				int x = index & 15;
				int z = index >> 4;
				Biome bio = biomes[x << 4 | z];
				x += sx;
				z += sz;
				int terrainY = (int) (35 + NOISE.eval(x * 0.1, z * 0.1) * 3);
				for (int y = 1; y < terrainY; y++) {
					int tile = level.getTileId(x, y, z);
					if (tile == BlockBase.NETHERRACK.id || tile == BlockBase.SOUL_SAND.id || tile == BlockBase.GRAVEL.id) {
						if (y < terrainY && BlockUtil.isNonSolidNoLava(level.getTileId(x, y + 3, z))) {
							GRAVEL.setBlockFast(level, x, y, z);
						}
					}
				}
				BlockState state;
				if (bio instanceof NetherBiome) {
					int depth = ((NetherBiome) bio).getTopDepth();
					boolean fire = ((NetherBiome) bio).hasFire();
					for (int y = MHelper.max(terrainY, 31); y < 127; y++) {
						int tile = level.getTileId(x, y, z);
						if (tile == BlockBase.FIRE.id && !fire) {
							BlockUtil.fastTilePlace(level, x, y, z, 0, 0);
						}
						else if (tile == BlockBase.NETHERRACK.id || tile == BlockBase.SOUL_SAND.id || tile == BlockBase.GRAVEL.id) {
							tile = level.getTileId(x, y + depth, z);
							if (BlockUtil.isNonSolidNoLava(tile)) {
								int offset = 0;
								if (BlockUtil.isNonSolidNoLava(level.getTileId(x, y + 1, z))) {
									state = ((NetherBiome) bio).getTopBlock(level, x, y, z);
									offset = 4;
								}
								else {
									state = ((NetherBiome) bio).getBottomBlock(level, x, y, z);
								}
								state.setBlockFast(level, x, y, z);
								if (offset > 0) {
									y += offset;
								}
							}
							else {
								state = ((NetherBiome) bio).getFillBlock(level, x, y, z);
								state.setBlockFast(level, x, y, z);
							}
						}
					}
				}
			});
			
			if (biome instanceof NetherBiome) {
				NetherBiome bio = (NetherBiome) biome;
				
				for (int sect = 0; sect < 8; sect++) {
					int sy = sect << 4;
					int count = random.nextInt(4);
					for (int i = 0; i < count; i++) {
						int px = random.nextInt(16) + sx;
						int pz = random.nextInt(16) + sz;
						int py = random.nextInt(16) | sy;
						int tile = level.getTileId(px, py, pz);
						if (tile == BlockBase.NETHERRACK.id) {
							NetherStructures.ORICHALCUM_ORE.generate(level, random, px, py, pz);
						}
					}
				}
				
				if (featureGenerator == null || !featureGenerator.isAlive()) {
					featureGenerator = new StructureGenerationThread();
					featureGenerator.start();
				}
				
				List<StructureInstance> structures = bio.getStructures();
				for (StructureInstance instance: structures) {
					featureGenerator.queue.add(new StructureData(instance, level, sx, sz));
				}
			}
		}
	}
	
	private final class StructureData {
		final StructureInstance instance;
		final Level level;
		final int sx;
		final int sz;
		
		private StructureData(StructureInstance instance, Level level, int sx, int sz) {
			this.instance = instance;
			this.level = level;
			this.sx = sx;
			this.sz = sz;
		}
	}
	
	private final class StructureGenerationThread extends Thread {
		private final Queue<StructureData> queue = new ArrayBlockingQueue<>(16384);
		
		public void run() {
			while (isActive()) {
				StructureData data = queue.poll();
				if (data != null) {
					for (int sect = 2; sect < 8; sect++) {
						int sy = sect << 4;
						int count = data.level.rand.nextInt(data.instance.getCount());
						for (int i = 0; i < count; i++) {
							if (data.level.rand.nextFloat() < data.instance.getChance()) {
								int px = data.level.rand.nextInt(16) + data.sx;
								int pz = data.level.rand.nextInt(16) + data.sz;
								for (int y = 0; y < 16; y++) {
									int py = sy | y;
									int tile = data.level.getTileId(px, py, pz);
									if (BlockUtil.isNonSolidNoLava(tile)) {
										if (data.instance.getStructure().generate(data.level, data.level.rand, px, py, pz)) {
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
	
	@SuppressWarnings("deprecation")
	private boolean isActive() {
		if (main != null && !main.isAlive()) {
			return false;
		}
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
			return ((MinecraftServerAccessor) server).bnb_isRunning();
		}
		return ClientUtil.getMinecraft().running;
	}
}
