package paulevs.bnb.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.client.render.entity.model.SpiderModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.entity.renderer.NetherSpiderRenderer;

@Mixin(SpiderModel.class)
public class SpiderModelMixin {
	@Inject(method = "<init>", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/model/Cuboid;addCuboid(FFFIIIF)V",
		shift = Shift.BEFORE
	))
	private void bnb_scaleModel(CallbackInfo info, @Local(ordinal = 0) LocalFloatRef scale) {
		if (NetherSpiderRenderer.upscaleModel) scale.set(0.01F);
	}
}
