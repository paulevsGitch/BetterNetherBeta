package paulevs.bnb.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.BlockPos;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MutableBlockPos;
import paulevs.bnb.block.properties.BNBBlockProperties;

import java.util.ArrayList;

public class BranchBlock extends TemplateBlock {
	public BranchBlock(Identifier identifier) {
		super(identifier, Material.WOOD);
		setSounds(WOOD_SOUNDS);
		setHardness(1F);
		
		BlockState state = getDefaultState();
		for (byte i = 0; i < 6; i++) {
			state = state.with(BNBBlockProperties.FACES[i], false);
		}
		setDefaultState(state);
	}
	
	@Override
	public void appendProperties(Builder<Block, BlockState> builder) {
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
		if (facing != state) {
			level.setBlockState(x, y, z, facing);
		}
	}
	
	@Override
	public boolean isFullOpaque() {
		return false;
	}
	
	@Override
	public boolean isFullCube() {
		return false;
	}
	
	@Override
	public void updateBoundingBox(BlockView view, int x, int y, int z) {
		if (view instanceof Level) {
			BlockState state = ((Level) view).getBlockState(x, y, z);
			if (state.isOf(this)) {
				setBoundingBox(state);
				return;
			}
		}
		this.setBoundingBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
	}
	
	@Override
	public void doesBoxCollide(Level level, int x, int y, int z, Box box, ArrayList list) {
		BlockState state = level.getBlockState(x, y, z);
		if (state.isOf(this)) {
			setBoundingBox(state);
		}
		super.doesBoxCollide(level, x, y, z, box, list);
		this.setBoundingBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
	}
	
	private void setBoundingBox(BlockState state) {
		float minX = 0.25F;
		float minY = 0.25F;
		float minZ = 0.25F;
		float maxX = 0.75F;
		float maxY = 0.75F;
		float maxZ = 0.75F;
		
		for (byte i = 0; i < 6; i++) {
			if (state.get(BNBBlockProperties.FACES[i])) {
				Direction dir = Direction.byId(i);
				switch (dir.getAxis()) {
					case X -> {
						if (dir.getOffsetX() < 0) minX = 0;
						else maxX = 1;
					}
					case Y -> {
						if (dir.getOffsetY() < 0) minY = 0;
						else maxY = 1;
					}
					case Z -> {
						if (dir.getOffsetZ() < 0) minZ = 0;
						else maxZ = 1;
					}
				}
			}
		}
		
		setBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	private BlockState getFacingState(Level level, int x, int y, int z) {
		MutableBlockPos pos = new MutableBlockPos();
		BlockState self = getDefaultState();
		for (byte i = 0; i < 6; i++) {
			Direction dir = Direction.byId(i);
			BlockState state = level.getBlockState(pos.set(x, y, z).move(dir));
			self = self.with(BNBBlockProperties.FACES[i], canConnect(state, dir));
		}
		return self;
	}
	
	private boolean canConnect(BlockState state, Direction dir) {
		Block block = state.getBlock();
		if (block instanceof StemBlock) return state.get(BNBBlockProperties.AXIS) == dir.getAxis();
		return block instanceof BranchBlock || (block.isFullOpaque() && block.isFullCube());
	}
}
