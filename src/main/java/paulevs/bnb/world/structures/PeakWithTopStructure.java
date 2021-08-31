package paulevs.bnb.world.structures;

import net.minecraft.level.Level;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.MHelper;

import java.util.Random;
import java.util.function.Function;

public class PeakWithTopStructure extends PeakStructure {
	private final BlockState[] top;
	
	public PeakWithTopStructure(BlockState block, int height, int radius, Function<BlockState, Boolean> placeFunction, BlockState... top) {
		super(block, height, radius, placeFunction);
		this.top = top;
	}
	
	@Override
	protected void generatePillar(Level level, Random random, int x, int z, int minY, int maxY) {
		/*for (int i = 0; i < top.length; i++) {
			int id = level.getTileId(x, minY - i, z);
			for (int j = 0; j < top.length; j++) {
				if (top[j].getBlockID() == id) {
					block.setBlockFast(level, x, minY - i, z);
					break;
				}
			}
		}*/
		int topCount = ((maxY - minY > top.length) && (random.nextInt(4) == 0)) ? top.length > 1 ? MHelper.randRange(1, top.length, random) : top.length : 0;
		int maxTop = maxY - topCount;
		for (int y = minY; y < maxTop; y++) {
			block.setBlockFast(level, x, y, z);
		}
		if (level.getTileId(x, maxTop, z) == 0) {
			for (int i = 0; i < topCount; i++) {
				top[i].setBlockFast(level, x, maxTop++, z);
			}
		}
	}
}
