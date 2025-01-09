package paulevs.bnb.block;

import net.minecraft.block.Block;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tool.ShearsItem;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bnb.block.property.BNBBlockMaterials;
import paulevs.bnb.block.property.BNBBlockProperties;
import paulevs.vbe.utils.CreativeUtil;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BNBLeavesBlock extends TemplateBlock {
	private Block sapling;
	
	public BNBLeavesBlock(Identifier id) {
		super(id, BNBBlockMaterials.NETHER_LEAVES);
		setHardness(LEAVES.getHardness());
		setSounds(GRASS_SOUNDS);
		setDefaultState(getDefaultState().with(BNBBlockProperties.LEAVES_DIRECTION, 6));
		setTicksRandomly(true);
		disableNotifyOnMetaDataChange();
	}
	
	@Override
	public List<ItemStack> getDropList(Level level, int x, int y, int z, BlockState state, int meta) {
		if (sapling == null || level.random.nextInt(31) > 0) return Collections.emptyList();
		return Collections.singletonList(new ItemStack(sapling));
	}
	
	@Override
	public void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(BNBBlockProperties.LEAVES_DIRECTION);
	}
	
	@Override
	public void afterBreak(Level level, PlayerEntity player, int x, int y, int z, int meta) {
		if (level.isRemote) return;
		ItemStack heldItem = player.getHeldItem();
		if (heldItem == null || !(heldItem.getType() instanceof ShearsItem)) {
			super.afterBreak(level, player, x, y, z, meta);
			return;
		}
		drop(level, x, y, z, new ItemStack(this));
		if (!CreativeUtil.isCreative(player)) heldItem.applyDamage(1, player);
	}
	
	public BlockState getState(Direction direction) {
		BlockState state = getDefaultState();
		if (direction == null) return state;
		return state.with(BNBBlockProperties.LEAVES_DIRECTION, direction.getId());
	}
	
	public Direction getDirection(BlockState state) {
		int dirID = state.get(BNBBlockProperties.LEAVES_DIRECTION);
		if (dirID == 6) return null;
		return Direction.byId(dirID);
	}
	
	private boolean canStay(Level level, int x, int y, int z, BlockState state) {
		Direction dir = getDirection(state);
		if (dir == null) return true;
		BlockState side = level.getBlockState(
			x + dir.getOffsetX(),
			y + dir.getOffsetY(),
			z + dir.getOffsetZ()
		);
		return side.isIn(BNBBlockTags.LEAVES_SUPPORT);
	}
	
	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int blockID) {
		if (level.isRemote) return;
		level.scheduleTick(x, y, z, id, level.random.nextInt(40) + 20);
	}
	
	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random random) {
		if (level.isRemote) return;
		BlockState state = level.getBlockState(x, y, z);
		if (!state.isOf(this)) return;
		if (canStay(level, x, y, z, state)) return;
		drop(level, x, y, z, 0);
		level.setBlockStateWithNotify(x, y, z, States.AIR.get());
	}
	
	public void setSapling(Block sapling) {
		this.sapling = sapling;
	}
}
