package paulevs.bnb.block.stone;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.material.ToolMaterial;
import net.minecraft.item.tool.PickaxeItem;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.item.BNBItems;

import java.util.Collections;
import java.util.List;

public class ObsidianShardsBlock extends ShardsBlock {
	public ObsidianShardsBlock(Identifier id) {
		super(id, Material.STONE);
		setHardness(5.0F);
	}
	
	@Override
	public void onEntityCollision(Level level, int x, int y, int z, Entity entity) {
		if (entity instanceof LivingEntity) {
			entity.damage(null, 2);
		}
	}
	
	@Override
	public List<ItemStack> getDropList(Level level, int x, int y, int z, BlockState state, int meta) {
		return Collections.emptyList();
	}
	
	@Override
	public void afterBreak(Level level, PlayerEntity player, int x, int y, int z, int meta) {
		if (level.isRemote) return;
		ItemStack item = player.getHeldItem();
		if (item == null || !(item.getType() instanceof PickaxeItem pickaxe)) return;
		ToolMaterial material = pickaxe.getMaterial(item);
		if (material.getMiningLevel() < ToolMaterial.STONE.getMiningLevel()) return;
		int scale = (ToolMaterial.DIAMOND.getMiningLevel() - material.getMiningLevel()) << 2 | 1;
		if (level.random.nextInt(scale) == 0) drop(level, x, y, z, new ItemStack(BNBItems.OBSIDIAN_SHARD));
	}
}
