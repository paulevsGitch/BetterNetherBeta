package paulevs.bnb.block;

import net.minecraft.block.BaseBlock;
import net.minecraft.block.material.Material;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.util.math.BlockPos;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bnb.block.properties.BNBBlockProperties;
import paulevs.bnb.sound.BNBSounds;

public class MossBlock extends NetherPlantBlock {
	public MossBlock(Identifier id) {
		super(id, Material.PLANT);
		setSounds(BNBSounds.MOSS_BLOCK);
		BlockState state = getDefaultState();
		for (byte i = 0; i < 6; i++) {
			state = state.with(BNBBlockProperties.FACES[i], false);
		}
		setDefaultState(state);
	}
	
	@Override
	public void appendProperties(Builder<BaseBlock, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.FACES);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		Level level = context.getWorld();
		BlockPos pos = context.getBlockPos();
		return getFacingState(level, pos.getX(), pos.getY(), pos.getZ());
	}
	
	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int l) {
		BlockState state = level.getBlockState(x, y, z);
		BlockState facing = getFacingState(level, x, y, z);
		
		if (isEmpty(facing)) {
			drop(level, x, y, z, l);
			level.setBlockState(x, y, z, States.AIR.get());
			return;
		}
		
		if (facing != state) {
			level.setBlockState(x, y, z, facing);
		}
	}
	
	@Override
	protected boolean canStay(Level level, int x, int y, int z) {
		BlockPos.Mutable mpos = new BlockPos.Mutable();
		for (byte i = 0; i < 6; i++) {
			Direction dir = Direction.byId(i);
			mpos.set(x, y, z).move(dir);
			BlockState state = level.getBlockState(mpos);
			if (isSupport(state)) return true;
		}
		return false;
	}
	
	@Override
	public void updateBoundingBox(BlockView view, int x, int y, int z) {
		if (view instanceof Level) {
			BlockState state = ((Level) view).getBlockState(x, y, z);
			float min = 2F / 16F;
			float max = 1 - min;
			float x1 = 0;
			float y1 = 0;
			float z1 = 0;
			float x2 = 1;
			float y2 = 1;
			float z2 = 1;
			
			Direction dir = null;
			for (byte i = 0; i < 6; i++) {
				if (!state.get(BNBBlockProperties.FACES[i])) continue;
				if (dir != null) {
					dir = null;
					break;
				}
				dir = Direction.byId(i);
			}
			
			if (dir != null) {
				if (dir.getOffsetX() > 0) x1 = max;
				else if (dir.getOffsetX() < 0) x2 = min;
				else if (dir.getOffsetY() > 0) y1 = max;
				else if (dir.getOffsetY() < 0) y2 = min;
				else if (dir.getOffsetZ() > 0) z1 = max;
				else if (dir.getOffsetZ() < 0) z2 = min;
			}
			
			this.setBoundingBox(x1, y1, z1, x2, y2, z2);
			return;
		}
		this.setBoundingBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
	}
	
	private boolean isSupport(BlockState state) {
		return (state.getMaterial() == Material.LEAVES || state.isOpaque()) && state.getBlock().isFullCube();
	}
	
	private BlockState getFacingState(Level level, int x, int y, int z) {
		BlockPos.Mutable pos = new BlockPos.Mutable();
		BlockState self = getDefaultState();
		for (byte i = 0; i < 6; i++) {
			Direction dir = Direction.byId(i);
			pos.set(x, y, z).move(dir);
			BlockState state = level.getBlockState(pos);
			self = self.with(BNBBlockProperties.FACES[i], isSupport(state));
		}
		return self;
	}
	
	private boolean isEmpty(BlockState state) {
		for (byte i = 0; i < 6; i++) {
			if (state.get(BNBBlockProperties.FACES[i])) return false;
		}
		return true;
	}
	
	public BlockState getStructureState(Level level, BlockPos pos) {
		BlockState state = getFacingState(level, pos.getX(), pos.getY(), pos.getZ());
		return isEmpty(state) ? null : state;
	}
}
