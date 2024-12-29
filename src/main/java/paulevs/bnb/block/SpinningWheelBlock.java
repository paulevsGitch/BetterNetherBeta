package paulevs.bnb.block;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Direction.Axis;
import net.modificationstation.stationapi.api.world.BlockStateView;
import paulevs.bnb.BNB;
import paulevs.bnb.block.entity.SpinningWheelBlockEntity;
import paulevs.bnb.block.property.BNBBlockProperties;
import paulevs.bnb.gui.container.SpinningWheelContainer;
import paulevs.bnb.item.BNBItems;

public class SpinningWheelBlock extends TemplateBlockWithEntity {
	public static final Identifier GUI_ID = BNB.id("spinning_wheel");
	
	public SpinningWheelBlock(Identifier identifier) {
		super(identifier, Material.STONE);
		setHardness(1.0F);
		setLightOpacity(1);
	}
	
	@Override
	public void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.DIRECTION);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		Direction dir = context.getHorizontalPlayerFacing();
		return getDefaultState().with(BNBBlockProperties.DIRECTION, dir);
	}
	
	@Override
	protected BlockEntity createBlockEntity() {
		return new SpinningWheelBlockEntity();
	}
	
	@Override
	public boolean canUse(Level level, int x, int y, int z, PlayerEntity player) {
		if (level.isRemote) return true;
		SpinningWheelBlockEntity entity = (SpinningWheelBlockEntity) level.getBlockEntity(x, y, z);
		if (entity == null) return false;
		GuiHelper.openGUI(player, GUI_ID, entity, new SpinningWheelContainer(player.inventory, entity));
		return true;
	}
	
	@Override
	public boolean isFullCube() {
		return false;
	}
	
	@Override
	public boolean isFullOpaque() {
		return false;
	}
	
	@Override
	public void updateBoundingBox(BlockView view, int x, int y, int z) {
		if (!(view instanceof BlockStateView stateView)) return;
		BlockState state = stateView.getBlockState(x, y, z);
		if (state.get(BNBBlockProperties.DIRECTION).getAxis() == Axis.X) {
			setBoundingBox(0.0625F, 0.0F, 0.3125F, 0.9375F, 0.9375F, 0.6875F);
		}
		else {
			setBoundingBox(0.3125F, 0.0F, 0.0625F, 0.6875F, 0.9375F, 0.9375F);
		}
	}
	
	@Override
	public void onBlockRemoved(Level level, int x, int y, int z) {
		SpinningWheelBlockEntity entity = (SpinningWheelBlockEntity) level.getBlockEntity(x, y, z);
		if (entity != null) {
			if (entity.getProcess() > 0) drop(level, x, y, z, new ItemStack(BNBItems.NETHER_FIBER));
			for (byte i = 0; i < entity.getInventorySize(); i++) {
				ItemStack stack = entity.getItem(i);
				if (stack == null) continue;
				drop(level, x, y, z, stack);
			}
		}
	}
	
	
}
