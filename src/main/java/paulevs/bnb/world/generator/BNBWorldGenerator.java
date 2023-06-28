package paulevs.bnb.world.generator;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.util.math.BlockPos;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.StationFlatteningChunk;
import paulevs.bnb.BNB;
import paulevs.bnb.listeners.BiomeListener;
import paulevs.bnb.world.biome.NetherBiome;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

public class BNBWorldGenerator {
	private static final Random RANDOM = new Random();
	private static final Function<BlockPos, Float> DENSITY = pos -> {
		float dx = (float) Math.sin(pos.getX() * 0.1) * 5;
		float dz = (float) Math.sin(pos.getZ() * 0.1) * 5;
		return 30F - pos.getY() + dx + dz;
	};
	
	private static InterpolationCell[] cells;
	private static int sectionCount;
	
	public static Chunk makeChunk(Level level, int cx, int cz) {
		StationFlatteningChunk chunk = new StationFlatteningChunk(level, cx, cz);
		final ChunkSection[] sections = chunk.sections;
		sectionCount = sections.length;
		
		if (cells == null || cells.length != sectionCount) {
			cells = new InterpolationCell[sectionCount];
			for (int i = 0; i < sectionCount; i++) {
				cells[i] = new InterpolationCell(4);
			}
		}
		
		NetherBiome biome = BiomeListener.BIOMES.get(BNB.id("crimson_forest"));
		
		IntStream.range(0, sectionCount).parallel().forEach(i -> {
			InterpolationCell cell = cells[i];
			cell.fill(cx << 4, i << 4, cz << 4, DENSITY);
			if (cell.isEmpty()) return;
			sections[i] = new ChunkSection(i);
			BlockPos.Mutable pos = new BlockPos.Mutable();
			for (byte bx = 0; bx < 16; bx++) {
				pos.setX(bx | cx << 4);
				cell.setX(bx);
				for (byte bz = 0; bz < 16; bz++) {
					pos.setZ(bz | cz << 4);
					cell.setZ(bz);
					for (byte by = 0; by < 16; by++) {
						cell.setY(by);
						if (cell.get() < 0.5F) continue;
						pos.setY(by | i << 4);
						sections[i].setBlockState(bx, by, bz, biome.getFillBlock());
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
		
		short maxY = (short) ((sectionCount << 4) - 1);
		for (short i = 0; i < 256; i++) {
			int x = i & 15;
			int z = i >> 4;
			BlockState lastState = BlockBase.BEDROCK.getDefaultState();
			for (short y = maxY; y > 0; y--) {
				BlockState state = get(sections, x, y, z);
				if (!state.getMaterial().isReplaceable() && lastState.getMaterial().isReplaceable()) {
					state = biome.getSurfaceBlock();
					set(sections, x, y, z, state);
				}
				lastState = state;
			}
		}
		
		int wx = cx << 4;
		int wz = cz << 4;
		for (short cy = 0; cy < sectionCount; cy++) {
			if (sections[cy] == null) continue;
			int wy = cy << 4;
			biome.getStructures().forEach(placer -> placer.place(level, RANDOM, wx, wy, wz));
		}
	}
	
	private static BlockState get(ChunkSection[] sections, int x, int y, int z) {
		ChunkSection section = sections[y >> 4];
		return section == null ? States.AIR.get() : section.getBlockState(x, y & 15, z);
	}
	
	private static void set(ChunkSection[] sections, int x, int y, int z, BlockState state) {
		int index = y >> 4;
		ChunkSection section = sections[index];
		if (section == null) {
			section = new ChunkSection(index);
			sections[index] = section;
		}
		section.setBlockState(x, y & 15, z, state);
	}
}
