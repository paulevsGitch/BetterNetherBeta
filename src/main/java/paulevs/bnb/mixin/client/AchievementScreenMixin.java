package paulevs.bnb.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.achievement.Achievement;
import net.minecraft.client.gui.screen.menu.AchievementsScreen;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import paulevs.bnb.achievement.BNBAchievements;

@Mixin(AchievementsScreen.class)
public class AchievementScreenMixin {
	/*@WrapOperation(method = "method_1998", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/render/entity/ItemRenderer;renderStackInGUI(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/item/ItemStack;II)V"
	))
	private void bnb_swapIcon(
		ItemRenderer itemRenderer, TextRenderer textRenderer, TextureManager textureManager,
		ItemStack stack, int x, int y, Operation<Void> original, @Local (ordinal = 1) Achievement achievement
	) {
		if (achievement == BNBAchievements.RGB) stack = BNBAchievements.getRGBIcon();
		original.call(itemRenderer, textRenderer, textureManager, stack, x, y);
	}*/
	
	@ModifyArg(method = "method_1998", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/render/entity/ItemRenderer;renderStackInGUI(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/item/ItemStack;II)V"
	))
	private ItemStack bnb_swapIcon(ItemStack stack, @Local Achievement achievement) {
		return achievement == BNBAchievements.RGB ? BNBAchievements.getRGBIcon() : stack;
	}
}
