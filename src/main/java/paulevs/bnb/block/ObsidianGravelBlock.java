package paulevs.bnb.block;

import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.material.ToolMaterial;
import net.minecraft.item.tool.ShovelItem;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.template.block.TemplateSandBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.item.BNBItems;

public class ObsidianGravelBlock extends TemplateSandBlock {
	public ObsidianGravelBlock(Identifier id) {
		super(id, 0);
		setHardness(2.0F);
		setBlastResistance(1000.0f);
		setSounds(GRAVEL_SOUNDS);
	}
	
	@Override
	public void afterBreak(Level level, PlayerEntity player, int x, int y, int z, int meta) {
		ItemStack item = player.getHeldItem();
		if (item == null || !(item.getType() instanceof ShovelItem shovel)) return;
		if (shovel.getMaterial(item).getMiningLevel() < ToolMaterial.IRON.getMiningLevel()) {
			drop(level, x, y, z, new ItemStack(level.random.nextBoolean() ? BNBItems.OBSIDIAN_SHARD : Item.flint));
			return;
		}
		drop(level, x, y, z, new ItemStack(this));
	}
}
