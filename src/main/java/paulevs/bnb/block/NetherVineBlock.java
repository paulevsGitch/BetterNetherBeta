package paulevs.bnb.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.block.properties.BNBBlockProperties;
import paulevs.bnb.block.properties.BNBBlockProperties.VineShape;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NetherVineBlock extends NetherCeilPlantBlock {
	public NetherVineBlock(Identifier id) {
		super(id);
		setDefaultState(getDefaultState().with(BNBBlockProperties.VINE_SHAPE, VineShape.BOTTOM));
		this.setBoundingBox(0.125F, 0.0F, 0.125F, 0.875F, 1.0F, 0.875F);
	}
	
	@Override
	public void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.VINE_SHAPE);
	}
	
	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int l) {
		tick(level, x, y, z);
		BlockState state = level.getBlockState(x, y, z);
		if (!state.isOf(this)) return;
		BlockState bottom = level.getBlockState(x, y - 1, z);
		boolean normal = state.get(BNBBlockProperties.VINE_SHAPE) == VineShape.NORMAL;
		boolean hasBottom = bottom.isOf(this);
		if (hasBottom != normal) {
			state = state.with(BNBBlockProperties.VINE_SHAPE, hasBottom ? VineShape.NORMAL : VineShape.BOTTOM);
			level.setBlockState(x, y, z, state);
		}
	}
	
	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random rand) {
		tick(level, x, y, z);
	}
	
	@Override
	protected boolean isCeil(BlockState state) {
		return state.isOf(this) || super.isCeil(state);
	}
	
	@Override
	protected void tick(Level level, int x, int y, int z) {
		if (!this.canStay(level, x, y, z)) {
			int y1 = y;
			while (level.getBlockState(x, y1, z).isOf(this)) {
				this.drop(level, x, y1, z, 0);
				level.setBlockState(x, y1, z, States.AIR.get());
				y1--;
			}
			level.updateArea(x, y1, z, x, y, z);
		}
	}
	
	@Override
	public List<ItemStack> getDropList(Level level, int x, int y, int z, BlockState state, int meta) {
		return Collections.emptyList();
	}
}
