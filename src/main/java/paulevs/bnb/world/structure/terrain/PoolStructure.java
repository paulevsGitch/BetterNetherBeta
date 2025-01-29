package paulevs.bnb.world.structure.terrain;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;
import paulevs.bnb.block.BNBBlockTags;

import java.util.Random;

public class PoolStructure extends Structure {
	private final BlockState wallsBlock;
	private final BlockState fluidBlock;
	private final float minRadius;
	private final float deltaRadius;
	
	public PoolStructure(BlockState wallsBlock, BlockState fluidBlock, float minRadius, float maxRadius) {
		this.wallsBlock = wallsBlock;
		this.fluidBlock = fluidBlock;
		this.minRadius = minRadius;
		deltaRadius = maxRadius - minRadius;
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		if (!level.getBlockState(x, y - 1, z).isIn(BNBBlockTags.NETHERRACK_TERRAIN)) return false;
		float radius = minRadius + random.nextFloat() * deltaRadius;
		float r1 = (radius - 1) * (radius - 1);
		float r2 = radius * radius;
		int ir = (int) Math.ceil(radius);
		for (int dx = -ir; dx <= ir; dx++) {
			int dx2 = dx * dx;
			int wx = x + dx;
			for (int dz = -ir; dz <= ir; dz++) {
				int dist = dx2 + dz * dz;
				if (dist > r2) continue;
				int wz = z + dz;
				if (!level.getBlockState(wx, y, wz).isAir()) continue;
				BlockState state = dist > r1 ? wallsBlock : fluidBlock;
				level.setBlockState(wx, y, wz, state);
				level.setBlockState(wx, y - 1, wz, wallsBlock);
				if (dist <= r1) level.setBlockState(wx, y - 2, wz, wallsBlock);
			}
		}
		return true;
	}
}
