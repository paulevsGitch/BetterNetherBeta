package paulevs.bnb.block;

import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tool.ShearsItem;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.util.Identifier;

public class BNBDoubleGrassPlantBlock extends BNBDoubleFloorPlantBlock {
	public BNBDoubleGrassPlantBlock(Identifier id) {
		super(id);
	}
	
	@Override
	public void afterBreak(Level level, PlayerEntity player, int x, int y, int z, int meta) {
		if (level.isRemote) super.afterBreak(level, player, x, y, z, meta);
		ItemStack heldItem = player.getHeldItem();
		if (heldItem == null || !(heldItem.getType() instanceof ShearsItem)) return;
		drop(level, x, y, z, new ItemStack(this));
		heldItem.applyDamage(1, player);
	}
}
