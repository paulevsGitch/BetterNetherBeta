package paulevs.bnb.mixin.common;

import net.minecraft.block.Block;
import net.minecraft.container.slot.CraftingResult;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.achievement.BNBAchievements;
import paulevs.bnb.block.BNBBlocks;

@Mixin(CraftingResult.class)
public class CraftingResultMixin {
	@Shadow private PlayerEntity player;
	
	@Inject(method = "onCrafted", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/item/ItemStack;onCrafted(Lnet/minecraft/level/Level;Lnet/minecraft/entity/living/player/PlayerEntity;)V",
		shift = Shift.AFTER
	))
	private void bnb_craftAchievements(ItemStack stack, CallbackInfo info) {
		if (!(stack.getType() instanceof BlockItem item)) return;
		Block block = item.getBlock();
		if (block == BNBBlocks.NETHERRACK_FURNACE) {
			player.incrementStat(BNBAchievements.ALMOST_THE_SAME);
		}
	}
}
