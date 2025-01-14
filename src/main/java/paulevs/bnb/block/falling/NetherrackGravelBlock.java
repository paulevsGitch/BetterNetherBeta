package paulevs.bnb.block.falling;

import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tool.ShovelItem;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.template.block.TemplateSandBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class NetherrackGravelBlock extends TemplateSandBlock {
	public NetherrackGravelBlock(Identifier id) {
		super(id, 0);
		setHardness(1.0F);
		setSounds(GRAVEL_SOUNDS);
	}
	
	@Override
	public void afterBreak(Level level, PlayerEntity player, int x, int y, int z, int meta) {
		ItemStack item = player.getHeldItem();
		float chance = item != null && item.getType() instanceof ShovelItem ? 1.0F : level.random.nextFloat();
		drop(level, x, y, z, chance > 0.5F ? new ItemStack(this) : new ItemStack(Item.flint));
	}
}
