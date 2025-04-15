package paulevs.bnb.item;

import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.material.ToolMaterial;
import net.minecraft.level.BlockView;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.template.item.TemplatePickaxeItem;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.block.BNBBlockTags;
import paulevs.bnb.util.MaterialExcavatorUtil;

public class ObsidianPickaxeItem extends TemplatePickaxeItem {
	public ObsidianPickaxeItem(Identifier identifier, ToolMaterial material) {
		super(identifier, material);
	}
	
	@Override
	public boolean isSuitableFor(PlayerEntity player, ItemStack itemStack, BlockView blockView, BlockPos blockPos, BlockState state) {
		if (state.isIn(BNBBlockTags.OBSIDIAN)) return true;
		return super.isSuitableFor(player, itemStack, blockView, blockPos, state);
	}
	
	@Override
	public float getMiningSpeedMultiplier(PlayerEntity player, ItemStack itemStack, BlockView blockView, BlockPos blockPos, BlockState state) {
		if (state.isIn(BNBBlockTags.OBSIDIAN)) return MaterialExcavatorUtil.scaleMiningSpeed(10.0F);
		return super.getMiningSpeedMultiplier(player, itemStack, blockView, blockPos, state);
	}
}
