package paulevs.bnb.world.structure.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bnb.block.BNBBlockTags;

import java.util.Random;

public class StreamStructure extends Structure {
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		Direction offset = Direction.UP;
		if (random.nextBoolean()) {
			offset = Direction.fromHorizontal(random.nextInt(4));
		}
		
		int sx = x + offset.getOffsetX();
		int sy = y + offset.getOffsetY();
		int sz = z + offset.getOffsetZ();
		
		if (!level.getBlockState(sx, sy, sz).isIn(BNBBlockTags.NETHERRACK_TERRAIN)) return false;
		
		int ey;
		
		for (ey = y - 1; ey > level.getBottomY(); ey--) {
			BlockState state = level.getBlockState(x, ey, z);
			if (state.getMaterial() == Material.LAVA) {
				ey++;
				break;
			}
			if (!state.getMaterial().isReplaceable()) return false;
		}
		
		BlockState state = Block.STILL_LAVA.getDefaultState();
		level.setBlockState(sx, sy, sz, state);
		
		for (int py = ey; py <= y; py++) {
			level.setBlockState(x, py, z, state);
		}
		
		return false;
	}
}
