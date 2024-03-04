package paulevs.bnb.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.item.armor.ArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import paulevs.bnb.item.NetherArmorItem;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
	@WrapOperation(method = "method_344", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/render/entity/PlayerRenderer;bindTexture(Ljava/lang/String;)V"
	))
	private void bnb_setArmorTexture(PlayerRenderer renderer, String texture, Operation<Void> operation, @Local(ordinal = 0) int type, @Local ArmorItem item) {
		if (item instanceof NetherArmorItem netherArmor) {
			operation.call(renderer, netherArmor.getArmourTexture(type));
		}
		else operation.call(renderer, texture);
	}
}
