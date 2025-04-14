package paulevs.bnb.world.structure.terrain;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.block.BNBBlockTags;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.noise.FloatNoise;
import paulevs.bnb.noise.PerlinNoise;

import java.util.Random;

public class AcidFountain extends Structure {
	private static final BlockState WALL = BNBBlocks.SULPHURIC_NETHERRACK.getDefaultState();
	private static final BlockState FLUID = BNBBlocks.SULPHURIC_ACID_STILL.getDefaultState();
	private static final FloatNoise NOISE_X = new PerlinNoise();
	private static final FloatNoise NOISE_Z = new PerlinNoise();
	private static final Random RANDOM = new Random(0);
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		float radius = random.nextFloat() * 5.0F + 3.0F;
		int height = (int) radius >> 1;
		int offset = (int) (radius * 2 / 3);
		
		for (byte i = 0; i < 4; i++) {
			Direction dir = Direction.fromHorizontal(i);
			int px = x + dir.getOffsetX() * offset;
			int pz = z + dir.getOffsetZ() * offset;
			if (!level.getBlockState(px, y - 1, pz).isIn(BNBBlockTags.NETHERRACK_TERRAIN)) return false;
		}
		
		RANDOM.setSeed(MathHelper.hashCode(x, y, z));
		NOISE_X.setSeed(RANDOM.nextInt());
		NOISE_Z.setSeed(RANDOM.nextInt());
		
		offset = (int) radius >> 4;
		
		for (int h = 0; h < height; h++) {
			int py = y + h - offset;
			
			float r2 = radius - h * 2;
			if (r2 < 2) break;
			
			int r = (int) r2 + 4;
			float r3 = r2 - 1;
			r2 *= r2;
			r3 *= r3;
			
			for (int dx = -r; dx <= r; dx++) {
				float fdx = dx * 0.3F;
				for (int dz = -r; dz <= r; dz++) {
					float fdz = dz * 0.3F;
					float dx2 = dx + NOISE_X.get(fdx, fdz) * 3.0F;
					float dz2 = dz + NOISE_Z.get(fdx, fdz) * 3.0F;
					float dist = dx2 * dx2 + dz2 * dz2;
					if (dist > r2) continue;
					int px = x + dx;
					int pz = z + dz;
					level.setBlockState(px, py, pz, dist > r3 ? WALL : FLUID);
					level.setBlockState(px, py - 1, pz, WALL);
				}
			}
		}
		
		return true;
	}
}
