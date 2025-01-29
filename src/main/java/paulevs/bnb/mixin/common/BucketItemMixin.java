package paulevs.bnb.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitResult;
import net.modificationstation.stationapi.api.block.States;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.block.property.BNBBlockMaterials;
import paulevs.bnb.item.BNBItems;

@Mixin(BucketItem.class)
public class BucketItemMixin {
	@Inject(method = "use", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/Level;getMaterial(III)Lnet/minecraft/block/material/Material;",
		shift = Shift.BEFORE,
		ordinal = 0
	), cancellable = true)
	private void bnb_checkAcid(ItemStack stack, Level level, PlayerEntity player, CallbackInfoReturnable<ItemStack> info, @Local HitResult hit) {
		if (level.getMaterial(hit.x, hit.y, hit.z) == BNBBlockMaterials.SULPHURIC_ACID) {
			level.setBlockStateWithNotify(hit.x, hit.y, hit.z, States.AIR.get());
			info.setReturnValue(new ItemStack(BNBItems.ACID_BUCKET));
		}
	}
}
