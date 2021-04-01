package paulevs.bnb.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.class_66;

@Mixin(class_66.class)
public abstract class Class66Mixin {
	@Inject(method = "method_296", at = @At("HEAD"), cancellable = true)
	private void bnb_renderWorldStart(CallbackInfo info) {
		//BlockUtil.setInWorld(true);
		//System.out.println("Start!");
	}
	
	@Inject(method = "method_296", at = @At("TAIL"))
	private void bnb_renderWorldEnd(CallbackInfo info) {
		//BlockUtil.setInWorld(false);
		//System.out.println("End!");
	}
}
