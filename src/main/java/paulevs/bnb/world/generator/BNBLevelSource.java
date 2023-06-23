package paulevs.bnb.world.generator;

import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.api.util.math.BlockPos;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.StationFlatteningChunk;
import paulevs.bnb.BNB;
import paulevs.bnb.listeners.BiomeListener;
import paulevs.bnb.world.biome.NetherBiome;
import paulevs.bnb.world.structures.BNBStructures;

import java.util.stream.IntStream;

public class BNBLevelSource {
	public static Chunk makeChunk(Level level, int cx, int cz) {
		StationFlatteningChunk chunk = new StationFlatteningChunk(level, cx, cz);
		ChunkSection[] sections = chunk.sections;
		sections[0] = new ChunkSection(0);
		sections[1] = new ChunkSection(1);
		
		NetherBiome biome = BiomeListener.BIOMES.get(BNB.id("crimson_forest"));
		BlockPos.Mutable pos = new BlockPos.Mutable();
		
		for (byte bx = 0; bx < 16; bx++) {
			pos.setX(bx);
			for (byte bz = 0; bz < 16; bz++) {
				pos.setZ(bz);
				for (byte by = 0; by < 16; by++) {
					pos.setY(by);
					sections[0].setBlockState(bx, by, bz, biome.getFillBlock(chunk, pos));
					pos.setY(by + 16);
					sections[1].setBlockState(bx, by, bz, biome.getFillBlock(chunk, pos));
				}
			}
		}
		
		pos.setY(31);
		for (byte bx = 0; bx < 16; bx++) {
			pos.setX(bx);
			for (byte bz = 0; bz < 16; bz++) {
				pos.setZ(bz);
				biome.buildSurfaceColumn(chunk, pos);
			}
		}
		
		return chunk;
	}
	
	public static void decorateChunk(Level level, int cx, int cz) {
		Chunk chunk = level.getChunkFromCache(cx, cz);
		int count = ((StationFlatteningChunk) chunk).sections.length;
		IntStream.range(0, count).parallel().forEach(cy -> {
			int wx = cx << 4;
			int wy = cy << 4;
			int wz = cz << 4;
			BNBStructures.FLAME_BULBS_PLACER.place(level, chunk, level.rand, wx, wy, wz);
			BNBStructures.CRIMSON_ROOTS_PLACER.place(level, chunk, level.rand, wx, wy, wz);
			BNBStructures.NETHER_DAISY_PLACER.place(level, chunk, level.rand, wx, wy, wz);
			BNBStructures.FIREWEED_STRUCTURE_PLACER.place(level, chunk, level.rand, wx, wy, wz);
		});
	}
}
