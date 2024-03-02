package paulevs.bnb.block;

import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.block.properties.BNBBlockProperties;
import paulevs.bnb.block.properties.BNBBlockProperties.VineShape;

public class CollectableNetherVineBlock extends NetherVineBlock {
	private NetherVineBlock basic;
	private Item collectableItem;
	
	public CollectableNetherVineBlock(Identifier id) {
		super(id);
	}
	
	public void setBasic(NetherVineBlock basic) {
		this.basic = basic;
	}
	
	public void setCollectableItem(Item item) {
		this.collectableItem = item;
	}
	
	@Override
	protected boolean isCeil(BlockState state) {
		return state.getBlock() instanceof GrowingNetherVineBlock ||
			state.getBlock() instanceof CollectableNetherVineBlock ||
			super.isCeil(state);
	}
	
	@Override
	public boolean canUse(Level level, int x, int y, int z, PlayerEntity player) {
		ItemStack stack = player.getHeldItem();
		boolean canUse = stack != null && stack.itemId == Item.shears.id;
		if (level.isRemote) return canUse;
		if (!canUse) return false;
		
		VineShape shape = level.getBlockState(x, y, z).get(BNBBlockProperties.VINE_SHAPE);
		level.setBlockState(x, y, z, basic.getDefaultState().with(BNBBlockProperties.VINE_SHAPE, shape));
		
		stack = new ItemStack(collectableItem, 1 + level.random.nextInt(3));
		if (!player.inventory.addStack(stack)) player.dropItem(stack);
		
		return true;
	}
}
