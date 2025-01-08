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
		x &= 15;
		z &= 15;
		
		if (!chunk.getBlockState(x, y + 1, z).isIn(BNBBlockTags.NETHERRACK_TERRAIN)) return false;
		
		int minY;
		for (minY = y - 1; minY > 95; minY--) {
			BlockState state = chunk.getBlockState(x, minY, z);
			if (state.getMaterial() == Material.LAVA) {
				minY++;
				break;
			}
			if (!state.getMaterial().isReplaceable()) return false;
		}
		
		chunk.setBlockState(x, y, z, LAVA);
		
		for (int py = minY; py <= y; py++) {
			chunk.setBlockStateWithMetadata(x, py, z, LAVA, 1);
		}
		
		level.updateLight(LightType.BLOCK, x, minY, z, x, y, z);
		
		return false;
	}
}
