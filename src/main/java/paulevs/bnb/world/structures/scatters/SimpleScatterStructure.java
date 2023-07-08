package paulevs.bnb.world.structures.scatters;

import net.minecraft.block.BaseBlock;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.BlockPos;

import java.util.Random;

public class SimpleScatterStructure extends ScatterStructure {
	protected final BlockState state;
	
	public SimpleScatterStructure(int radius, int count, BaseBlock block) {
		this(radius, count, block.getDefaultState());
	}
	
	public SimpleScatterStructure(int radius, int count, BlockState state) {
		super(radius, count);
		this.state = state;
	}
	
	@Override
	protected void place(Level level, Random random, BlockPos pos, BlockPos center) {
		level.setBlockState(pos.getX(), pos.getY(), pos.getZ(), state);
	}
	
	@Override
	protected boolean canPlaceAt(Level level, BlockPos pos) {
		return state.getBlock().canPlaceAt(level, pos.getX(), pos.getY(), pos.getZ());
	}
}
