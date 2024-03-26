package paulevs.bnb.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Direction.Axis;
import paulevs.bnb.block.property.BNBBlockProperties;

public class EmberLogBlock extends NetherLogBlock {
	public EmberLogBlock(Identifier id) {
		super(id);
	}
	
	@Override
	public void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.NEAR_LAVA);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		BlockState state = super.getPlacementState(context);
		if (state.get(BNBBlockProperties.AXIS) != Axis.Y) {
			return state.with(BNBBlockProperties.NEAR_LAVA, false);
		}
		Level level = context.getWorld();
		BlockPos pos = context.getBlockPos();
		boolean nearLava = isNearLava(level, pos.getX(), pos.getY(), pos.getZ());
		if (!nearLava) {
			BlockState below = level.getBlockState(pos.getX(), pos.getY() - 1, pos.getZ());
			nearLava = below.isOf(this) && below.get(BNBBlockProperties.AXIS) == Axis.Y && below.get(BNBBlockProperties.NEAR_LAVA);
			nearLava &= isNearLava(level, pos.getX(), pos.getY() - 1, pos.getZ());
		}
		return state.with(BNBBlockProperties.NEAR_LAVA, nearLava);
	}
	
	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int l) {
		BlockState state = level.getBlockState(x, y, z);
		if (!state.isOf(this)) return;
		if (state.get(BNBBlockProperties.AXIS) != Axis.Y) return;
		boolean nearLava = state.get(BNBBlockProperties.NEAR_LAVA);
		boolean lavaInWorld = isNearLava(level, x, y, z);
		if (!lavaInWorld) {
			BlockState below = level.getBlockState(x, y - 1, z);
			lavaInWorld = below.isOf(this) && below.get(BNBBlockProperties.AXIS) == Axis.Y && below.get(
				BNBBlockProperties.NEAR_LAVA);
			lavaInWorld &= isNearLava(level, x, y - 1, z);
		}
		if (nearLava != lavaInWorld) {
			level.setBlockState(x, y, z, state.with(BNBBlockProperties.NEAR_LAVA, lavaInWorld));
		}
	}
	
	private boolean isNearLava(Level level, int x, int y, int z) {
		boolean lavaInWorld = false;
		for (byte d = 0; d < 6 && !lavaInWorld; d++) {
			Direction dir = Direction.byId(d);
			BlockState side = level.getBlockState(
				x + dir.getOffsetX(),
				y + dir.getOffsetY(),
				z + dir.getOffsetZ()
			);
			lavaInWorld = side.getMaterial() == Material.LAVA;
		}
		return lavaInWorld;
	}
}
