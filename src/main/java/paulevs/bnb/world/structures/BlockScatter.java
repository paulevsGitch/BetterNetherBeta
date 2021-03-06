package paulevs.bnb.world.structures;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.MHelper;

import java.util.Random;

public class BlockScatter extends Structure {
	protected final BlockState block;
	private final float radius;
	private final int count;
	
	public BlockScatter(BlockState block, float radius) {
		this(block, radius, MathHelper.floor(radius * 10));
	}
	
	public BlockScatter(BlockState block, float radius, int count) {
		this.block = block;
		this.radius = radius;
		this.count = count;
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		int count = MHelper.randRange(this.count >> 1, this.count, random);
		for (int i = 0; i < count; i++) {
			int px = x + MHelper.clamp(MathHelper.floor(random.nextGaussian() * radius + 0.5), -8, 8);
			int pz = z + MHelper.clamp(MathHelper.floor(random.nextGaussian() * radius + 0.5), -8, 8);
			for (int py = y + 5; py > y - 5; py --) {
				if (level.getTileId(px, py, pz) == 0 && block.getBlock().canPlaceAt(level, px, py, pz)) {
					placeBlock(level, px, py, pz);
					break;
				}
			}
		}
		
		return true;
	}
	
	protected void placeBlock(Level level, int x, int y, int z) {
		block.setBlockFast(level, x, y, z);
	}
}
