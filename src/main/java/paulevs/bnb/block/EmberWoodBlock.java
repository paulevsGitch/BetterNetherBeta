package paulevs.bnb.block;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.util.math.BlockPos;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Direction.Axis;
import paulevs.bnb.block.properties.NetherBlockProperties;

public class EmberWoodBlock extends NetherWoodBlock {
	public EmberWoodBlock(Identifier id) {
		super(id);
	}
	
	@Override
	public void appendProperties(Builder<BlockBase, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(NetherBlockProperties.NEAR_LAVA);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		BlockState state = super.getPlacementState(context);
		if (state.get(NetherBlockProperties.AXIS) != Axis.Y) {
			return state.with(NetherBlockProperties.NEAR_LAVA, false);
		}
		Level level = context.getWorld();
		BlockPos pos = context.getBlockPos();
		boolean nearLava = isNearLava(level, pos.getX(), pos.getY(), pos.getZ());
		if (!nearLava) {
			BlockState below = level.getBlockState(pos.getX(), pos.getY() - 1, pos.getZ());
			nearLava = below.isOf(this) && below.get(NetherBlockProperties.AXIS) == Axis.Y && below.get(NetherBlockProperties.NEAR_LAVA);
			nearLava &= isNearLava(level, pos.getX(), pos.getY() - 1, pos.getZ());
		}
		return state.with(NetherBlockProperties.NEAR_LAVA, nearLava);
	}
	
	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int l) {
		BlockState state = level.getBlockState(x, y, z);
		if (!state.isOf(this)) return;
		if (state.get(NetherBlockProperties.AXIS) != Axis.Y) return;
		boolean nearLava = state.get(NetherBlockProperties.NEAR_LAVA);
		boolean lavaInWorld = isNearLava(level, x, y, z);
		if (!lavaInWorld) {
			BlockState below = level.getBlockState(x, y - 1, z);
			lavaInWorld = below.isOf(this) && below.get(NetherBlockProperties.AXIS) == Axis.Y && below.get(NetherBlockProperties.NEAR_LAVA);
			lavaInWorld &= isNearLava(level, x, y - 1, z);
		}
		if (nearLava != lavaInWorld) {
			level.setBlockState(x, y, z, state.with(NetherBlockProperties.NEAR_LAVA, lavaInWorld));
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
