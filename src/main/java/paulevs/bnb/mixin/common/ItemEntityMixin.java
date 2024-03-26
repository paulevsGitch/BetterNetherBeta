package paulevs.bnb.mixin.common;

import net.minecraft.block.Block;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.entity.technical.ItemEntity;
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

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
	@Shadow public ItemStack stack;
	
	@Inject(method = "onPlayerCollision", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/Level;playSound(Lnet/minecraft/entity/Entity;Ljava/lang/String;FF)V",
		shift = Shift.BEFORE
	))
	private void bnb_onCollision(PlayerEntity player, CallbackInfo info) {
		if (!(stack.getType() instanceof BlockItem item)) return;
		Block block = item.getBlock();
		
		if (block == BNBBlocks.FALUN_LOG) player.incrementStat(BNBAchievements.COLLECT_FALUN_LOG);
		if (block == BNBBlocks.PIROZEN_LOG) player.incrementStat(BNBAchievements.COLLECT_PIROZEN_LOG);
		if (block == BNBBlocks.POISON_LOG) player.incrementStat(BNBAchievements.COLLECT_POISON_LOG);
		
		int summ = BNBAchievements.readStat(BNBAchievements.COLLECT_FALUN_LOG) > 0 ? 1 : 0;
		summ += BNBAchievements.readStat(BNBAchievements.COLLECT_PIROZEN_LOG) > 0 ? 1 : 0;
		summ += BNBAchievements.readStat(BNBAchievements.COLLECT_POISON_LOG) > 0 ? 1 : 0;
		
		if (summ == 3) {
			player.incrementStat(BNBAchievements.RGB);
		}
	}
}
