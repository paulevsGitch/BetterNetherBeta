package paulevs.bnb.mixin.common;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.block.NetherSaplingBlock;

@Mixin(DyeItem.class)
public class DyeItemMixin {
	@Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
	private void bnb_useOnBlock(ItemStack stack, PlayerBase player, Level level, int x, int y, int z, int side, CallbackInfoReturnable<Boolean> info) {
		if (stack.getDamage() != 15) return;
		BlockState state = level.getBlockState(x, y, z);
		if (state.getBlock() instanceof NetherSaplingBlock sapling) {
			if (!level.isRemote) sapling.grow(level, x, y, z);
			info.setReturnValue(true);
		}
	}
}
