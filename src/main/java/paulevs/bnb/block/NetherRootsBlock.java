package paulevs.bnb.block;

import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.vbe.utils.CreativeUtil;

import java.util.Random;

public class NetherRootsBlock extends NetherFloorPlantBlock {
	public NetherRootsBlock(Identifier id) {
		super(id);
	}
	
	@Override
	public void afterBreak(Level level, PlayerEntity player, int x, int y, int z, int meta) {
		if (level.isRemote) super.afterBreak(level, player, x, y, z, meta);
		ItemStack heldItem = player.getHeldItem();
		if (heldItem == null || heldItem.getType() != Item.shears) {
			int count = level.random.nextInt(3);
			if (count == 0) return;
			drop(level, x, y, z, new ItemStack(Item.stick, count));
			return;
		}
		drop(level, x, y, z, new ItemStack(this));
		if (!CreativeUtil.isCreative(player)) heldItem.applyDamage(1, player);
	}
}
