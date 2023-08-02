package paulevs.bnb.block;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.bnb.block.properties.BNBBlockProperties;

import java.util.Random;

public class GrowingNetherVineBlock extends NetherVineBlock {
	private NetherVineBlock grown;
	
	public GrowingNetherVineBlock(Identifier id) {
		super(id);
		this.setTicksRandomly(true);
	}
	
	public void setGrown(NetherVineBlock grown) {
		this.grown = grown;
	}
	
	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random rand) {
		super.onScheduledTick(level, x, y, z, rand);
		grow(level, x, y, z);
	}
	
	@Override
	protected boolean isCeil(BlockState state) {
		return state.getBlock() instanceof GrowingNetherVineBlock ||
			state.getBlock() instanceof CollectableNetherVineBlock ||
			super.isCeil(state);
	}
	
	private void grow(Level level, int x, int y, int z) {
		BlockState state = level.getBlockState(x, y, z);
		if (!state.isOf(this)) return;
		
		int count = 0;
		for (int i = -3; i <= 3; i++) {
			BlockState sideState = level.getBlockState(x, y + i, z);
			if (sideState.isOf(grown)) count++;
		}
		
		if (count > 2) return;
		
		state = grown.getDefaultState().with(BNBBlockProperties.VINE_SHAPE, state.get(BNBBlockProperties.VINE_SHAPE));
		level.setBlockState(x, y, z, state);
	}
}
