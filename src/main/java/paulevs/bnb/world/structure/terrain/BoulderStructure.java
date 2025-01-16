package paulevs.bnb.world.structure.terrain;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.block.BNBBlockTags;
import paulevs.bnb.noise.PerlinNoise;

import java.util.Random;

public class BoulderStructure extends Structure {
	private static final PerlinNoise NOISE = new PerlinNoise();
	private final BlockState state;
	private final float minRadius;
	private final float deltaRadius;
	
	public BoulderStructure(BlockState state, float minRadius, float maxRadius) {
		this.state = state;
		this.minRadius = minRadius;
		deltaRadius = maxRadius - minRadius + 1;
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		BlockState below = level.getBlockState(x, y - 1, z);
		if (below == state) return false;
		if (!below.isIn(BNBBlockTags.NETHERRACK_TERRAIN) && !below.isIn(BNBBlockTags.SOUL_TERRAIN) && !below.isIn(BNBBlockTags.GRAVEL)) {
			return false;
		}
		
		float r = random.nextFloat() * deltaRadius + minRadius;
		int radius = (int) Math.ceil(r + 5.0F);
		random.setSeed(MathHelper.hashCode(x, y, z));
		y -= (int) (r * 0.25F);
		
		for (int dx = -radius; dx <= radius; dx++) {
			int wx = x + dx;
			int x2 = dx * dx;
			for (int dy = -radius; dy <= radius; dy++) {
				int wy = y + dy;
				int y2 = dy * dy;
				for (int dz = -radius; dz <= radius; dz++) {
					int wz = z + dz;
					int z2 = dz * dz;
					float rad = r + (NOISE.get(wx * 0.1, wy * 0.1, wz * 0.1) - 0.5F) * r * 1.5F;
					if (x2 + y2 + z2 > rad * rad) continue;
					if (!canReplace(level.getBlockState(wx, wy, wz))) continue;
					level.setBlockState(wx, wy, wz, state);
				}
			}
		}
		
		return true;
	}
	
	protected boolean canReplace(BlockState state) {
		return true;
	}
	
	static {
		NOISE.setSeed(1);
	}
}
