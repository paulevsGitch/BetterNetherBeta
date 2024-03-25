package paulevs.bnb.mixin.common;

import net.minecraft.container.slot.FurnaceOutput;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.achievement.BNBAchievements;
import paulevs.bnb.item.BNBItems;

@Mixin(FurnaceOutput.class)
public class FurnaceOutputMixin {
	@Shadow private PlayerEntity player;
	
	@Inject(method = "onCrafted", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/item/ItemStack;onCrafted(Lnet/minecraft/level/Level;Lnet/minecraft/entity/living/player/PlayerEntity;)V",
		shift = Shift.AFTER
	))
	private void bnb_craftAchievements(ItemStack stack, CallbackInfo info) {
		Item item = stack.getType();
		if (item == BNBItems.ORICHALCUM_INGOT) {
			player.incrementStat(BNBAchievements.ORICHALCUM);
		}
	}
}
