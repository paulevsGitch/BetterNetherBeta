package paulevs.bnb.world.structures;

import net.minecraft.level.Level;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.BlockUtil;

public class DoublePlantStructure extends BlockScatter {
	public DoublePlantStructure(BlockState state, int radius) {
		super(state, radius);
	}
	
	@Override
	protected void placeBlock(Level level, int x, int y, int z) {
		if (!BlockUtil.isTerrain(level.getTileId(x, y - 1, z)) || level.getTileId(x, y, z) != 0 || level.getTileId(x, y + 1, z) != 0) {
			return;
		}
		block.setBlockFast(level, x, y, z);
		block.setBlockFast(level, x, y + 1, z);
	}
}
