package paulevs.bnb.block;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import paulevs.bnb.block.properties.BNBBlockProperties;
import paulevs.bnb.block.properties.BNBBlockProperties.DoubleShape;

import java.util.Collections;
import java.util.List;

public class DoubleFloorPlantBlock extends NetherFloorPlantBlock {
	public DoubleFloorPlantBlock(Identifier id) {
		super(id);
		setDefaultState(getDefaultState().with(BNBBlockProperties.DOUBLE_SHAPE, DoubleShape.BOTTOM));
	}
	
	@Override
	public void appendProperties(Builder<BlockBase, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.DOUBLE_SHAPE);
	}
	
	@Override
	protected boolean isGround(BlockState state) {
		if (state.isOf(this) && state.get(BNBBlockProperties.DOUBLE_SHAPE) == DoubleShape.BOTTOM) return true;
		return super.isGround(state);
	}
	
	@Override
	public boolean canPlaceAt(Level level, int x, int y, int z) {
		return super.canPlaceAt(level, x, y, z) && level.getBlockState(x, y + 1, z).getMaterial().isReplaceable();
	}
	
	@Override
	public void drop(Level level, int x, int y, int z, BlockState state, int meta) {
		if (state.get(BNBBlockProperties.DOUBLE_SHAPE) == DoubleShape.TOP) return;
		super.drop(level, x, y, z, state, meta);
	}
	
	@Override
	public List<ItemInstance> getDropList(Level level, int x, int y, int z, BlockState state, int meta) {
		if (state.get(BNBBlockProperties.DOUBLE_SHAPE) == DoubleShape.TOP) return Collections.emptyList();
		return Collections.singletonList(new ItemInstance(this));
	}
	
	@Override
	public void afterPlaced(Level level, int x, int y, int z, Living entity) {
		super.afterPlaced(level, x, y, z, entity);
		level.setBlockState(x, y + 1, z, getDefaultState().with(BNBBlockProperties.DOUBLE_SHAPE, DoubleShape.TOP));
	}
	
	@Override
	public void onBlockRemoved(Level level, int x, int y, int z) {
		BlockState state = level.getBlockState(x, y - 1, z);
		if (state.isOf(this)) {
			level.setBlockState(x, y - 1, z, States.AIR.get());
		}
		super.onBlockRemoved(level, x, y, z);
	}
}
