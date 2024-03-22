package paulevs.bnb.block;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.block.entity.CocoonSpawnerBlockEntity;
import paulevs.bnb.sound.BNBSounds;
import paulevs.vbe.utils.CreativeUtil;

public class SpiderCocoonBlock extends TemplateBlockWithEntity {
	public SpiderCocoonBlock(Identifier identifier) {
		super(identifier, Material.STONE);
		setHardness(0.75F);
		setSounds(BNBSounds.MOSS_BLOCK);
		setBoundingBox(0.125F, 0F, 0.125F, 0.875F, 0.75F, 0.875F);
		setLightOpacity(2);
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
	public Box getCollisionShape(Level level, int x, int y, int z) {
		return null;
	}
	
	@Override
	protected BlockEntity createBlockEntity() {
		return new CocoonSpawnerBlockEntity();
	}
	
	@Override
	public boolean canPlaceAt(Level level, int x, int y, int z) {
		Block block = level.getBlockState(x, y - 1, z).getBlock();
		return block.isFullCube() && block.isFullOpaque() && super.canPlaceAt(level, x, y, z);
	}
	
	@Override
	public void afterBreak(Level level, PlayerEntity player, int x, int y, int z, int meta) {
		if (level.isRemote) super.afterBreak(level, player, x, y, z, meta);
		ItemStack heldItem = player.getHeldItem();
		if (heldItem == null || heldItem.getType() != Item.shears) {
			int count = level.random.nextInt(5) + 4;
			drop(level, x, y, z, new ItemStack(Item.string, count));
			return;
		}
		drop(level, x, y, z, new ItemStack(this));
		if (!CreativeUtil.isCreative(player)) heldItem.applyDamage(1, player);
	}
}
