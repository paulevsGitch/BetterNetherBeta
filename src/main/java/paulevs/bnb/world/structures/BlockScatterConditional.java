package paulevs.bnb.world.structures;

import net.minecraft.level.Level;
import paulevs.bnb.util.BlockState;

import java.util.Random;
import java.util.function.Function;

public class BlockScatterConditional extends BlockScatter {
	private final Function<BlockState, Boolean> condition;
	
	public BlockScatterConditional(BlockState block, float radius, Function<BlockState, Boolean> condition) {
		super(block, radius);
		this.condition = condition;
	}
	
	public BlockScatterConditional(BlockState block, float radius, int count, Function<BlockState, Boolean> condition) {
		super(block, radius, count);
		this.condition = condition;
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		BlockState check = new BlockState(level.getTileId(x, y - 1, z), level.getTileMeta(x, y - 1, z));
		if (condition.apply(check)) {
			return super.generate(level, random, x, y, z);
		}
		return false;
	}
	
	@Override
	protected void placeBlock(Level level, int x, int y, int z) {
		BlockState check = new BlockState(level.getTileId(x, y - 1, z), level.getTileMeta(x, y - 1, z));
		if (condition.apply(check)) {
			super.placeBlock(level, x, y, z);
		}
	}
}
