package paulevs.bnb.world.structures.common;

import net.minecraft.block.Block;
import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.MathHelper;

import java.util.Random;

public class NetherOreStructure extends Structure {
	private final BlockState state;
	private final int randomRange;
	private final int randomMin;
	private final int radiusSqr;
	private final int radius;
	
	public NetherOreStructure(Block block, int radius) {
		this(block.getDefaultState(), radius);
	}
	
	public NetherOreStructure(BlockState state, int radius) {
		this.state = state;
		this.radius = radius;
		this.randomMin = radius >> 1;
		this.randomRange = randomMin << 1 | 1;
		this.radiusSqr = radius * radius;
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		random.setSeed(MathHelper.hashCode(x, y, z));
		for (int dx = -radius; dx <= radius; dx++) {
			int wx = x + dx;
			for (int dy = -radius; dy <= radius; dy++) {
				int wy = y + dy;
				for (int dz = -radius; dz <= radius; dz++) {
					int wz = z + dz;
					int xsq = dx + random.nextInt(randomRange) - randomMin;
					int ysq = dy + random.nextInt(randomRange) - randomMin;
					int zsq = dz + random.nextInt(randomRange) - randomMin;
					if (xsq * xsq + ysq * ysq + zsq * zsq > radiusSqr) continue;
					if (!level.getBlockState(wx, wy, wz).isOf(Block.NETHERRACK)) continue;
					level.setBlockState(wx, wy, wz, state);
				}
			}
		}
		return true;
	}
}
