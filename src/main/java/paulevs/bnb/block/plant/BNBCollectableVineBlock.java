package paulevs.bnb.block.plant;

import net.minecraft.block.Block;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tool.ShearsItem;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.block.property.BNBBlockProperties;

import java.util.Random;

public class BNBCollectableVineBlock extends BNBVineBlock {
	private Item collectableItem;
	
	public BNBCollectableVineBlock(Identifier id) {
		super(id);
		setDefaultState(getDefaultState().with(BNBBlockProperties.BERRIES, false));
		setTicksRandomly(true);
	}
	
	public void setCollectableItem(Item item) {
		this.collectableItem = item;
	}
	
	@Override
	public void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.BERRIES);
	}
	
	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random rand) {
		super.onScheduledTick(level, x, y, z, rand);
		grow(level, x, y, z);
	}
	
	@Override
	public boolean canUse(Level level, int x, int y, int z, PlayerEntity player) {
		ItemStack stack = player.getHeldItem();
		boolean canUse = stack != null && stack.getType() instanceof ShearsItem;
		if (level.isRemote) return canUse;
		if (!canUse) return false;
		
		BlockState state = level.getBlockState(x, y, z);
		if (!state.get(BNBBlockProperties.BERRIES)) return false;
		level.setBlockState(x, y, z, state.with(BNBBlockProperties.BERRIES, false));
		
		stack = new ItemStack(collectableItem, 1 + level.random.nextInt(3));
		if (!player.inventory.addStack(stack)) player.dropItem(stack);
		
		return true;
	}
	
	private void grow(Level level, int x, int y, int z) {
		if (level.random.nextInt(32) > 0) return;
		
		BlockState state = level.getBlockState(x, y, z);
		if (!state.isOf(this)) return;
		
		int count = 0;
		for (int i = -3; i <= 3; i++) {
			BlockState sideState = level.getBlockState(x, y + i, z);
			if (sideState.isOf(this) && sideState.get(BNBBlockProperties.BERRIES)) count++;
		}
		
		if (count > 2) return;
		
		level.setBlockState(x, y, z, state.with(BNBBlockProperties.BERRIES, true));
	}
}
