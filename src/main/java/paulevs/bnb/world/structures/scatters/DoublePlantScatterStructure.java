package paulevs.bnb.world.structures.scatters;

import net.minecraft.block.Block;
import net.minecraft.level.Level;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.block.BlockState;
import paulevs.bnb.block.properties.BNBBlockProperties;
import paulevs.bnb.block.properties.BNBBlockProperties.DoubleShape;

import java.util.Random;

public class DoublePlantScatterStructure extends ScatterStructure {
	protected final BlockState bottom;
	protected final BlockState top;
	
	public DoublePlantScatterStructure(int radius, int count, Block block) {
		super(radius, count);
		this.bottom = block.getDefaultState().with(BNBBlockProperties.DOUBLE_SHAPE, DoubleShape.BOTTOM);
		this.top = block.getDefaultState().with(BNBBlockProperties.DOUBLE_SHAPE, DoubleShape.TOP);
	}
	
	@Override
	protected void place(Level level, Random random, BlockPos pos, BlockPos center) {
		level.setBlockState(pos.getX(), pos.getY(), pos.getZ(), bottom);
		level.setBlockState(pos.getX(), pos.getY() + 1, pos.getZ(), top);
	}
	
	@Override
	protected boolean canPlaceAt(Level level, BlockPos pos) {
		return bottom.getBlock().canPlaceAt(level, pos.getX(), pos.getY(), pos.getZ());
	}
}
