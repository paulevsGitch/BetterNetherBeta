package paulevs.bnb.mixin.client;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.rendering.CustomStackTexture;

@Mixin(Item.class)
public class ItemMixin {
	@Inject(method = "getTexturePosition(Lnet/minecraft/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
	private void bnb_setTexture(ItemStack stack, CallbackInfoReturnable<Integer> info) {
		if (stack == null || !(stack.getType() instanceof CustomStackTexture item)) return;
		info.setReturnValue(item.getTexture(stack));
	}
}
