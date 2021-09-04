package paulevs.bnb.world.structures;

import net.minecraft.level.Level;
import paulevs.bnb.block.types.FlameBambooType;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.MHelper;

public class DistortedBambooStructure extends BlockScatter {
	public DistortedBambooStructure() {
		super(new BlockState(BlockListener.getBlock("flame_bamboo"), FlameBambooType.SAPLING), 5);
	}
	
	@Override
	protected void placeBlock(Level level, int x, int y, int z) {
		if (level.getTileId(x, y + 1, z) != 0 || level.getTileId(x, y + 2, z) != 0) {
			return;
		}
		int h = MHelper.randRange(7, 12, level.rand);
		for (int i = 0; i < h; i++) {
			int py = y + i;
			if (level.getTileId(x, py, z) != 0) {
				BlockUtil.fastTilePlace(level, x, py - 1, z, block.getBlockID(), FlameBambooType.TOP_INACTIVE.getMeta());
				BlockUtil.fastTilePlace(level, x, py - 2, z, block.getBlockID(), FlameBambooType.MIDDLE.getMeta());
			}
			else {
				BlockUtil.fastTilePlace(level, x, py, z, block.getBlockID(), FlameBambooType.STEM.getMeta());
			}
		}
		h += y;
		BlockUtil.fastTilePlace(level, x, h, z, block.getBlockID(), FlameBambooType.TOP_INACTIVE.getMeta());
		BlockUtil.fastTilePlace(level, x, h - 1, z, block.getBlockID(), FlameBambooType.MIDDLE.getMeta());
	}
}
