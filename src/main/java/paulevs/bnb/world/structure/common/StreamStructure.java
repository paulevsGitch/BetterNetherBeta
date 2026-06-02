package paulevs.bnb.world.structure.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.level.LightType;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;
import paulevs.bnb.block.BNBBlockTags;

import java.util.Random;

public class StreamStructure extends Structure {
	private static final BlockState LAVA = Block.STILL_LAVA.getDefaultState();
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		Chunk chunk = level.getChunkFromCache(x >> 4, z >> 4);
		byte cx = (byte) (x & 15);
		byte cz = (byte) (z & 15);
		int maxY = y + 2;
		
		if (maxY < 100 || !chunk.getBlockState(cx, maxY, cz).isIn(BNBBlockTags.NETHERRACK_TERRAIN)) return false;
		
		int searchStart = maxY;
		while (chunk.getBlockState(cx, searchStart, cz).isIn(BNBBlockTags.NETHERRACK_TERRAIN)) {
			searchStart--;
		}
		
		int minY;
		for (minY = searchStart; minY > 95; minY--) {
			BlockState state = chunk.getBlockState(cx, minY, cz);
			if (state.getMaterial() == Material.LAVA) {
				minY++;
				break;
			}
			if (!state.getMaterial().isReplaceable()) return false;
		}
		
		chunk.setBlockState(cx, maxY, cz, LAVA);
		for (int py = minY; py < maxY; py++) {
			chunk.setBlockState(cx, py, cz, LAVA, 1);
		}
		
		level.updateLight(LightType.BLOCK, x, minY, z, x, maxY, z);
		
		return false;
	}
}
