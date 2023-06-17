package paulevs.bnb.mixin.client;

import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.aocalc.LightingCalculatorImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.rendering.EmissiveQuad;

@Mixin(value = LightingCalculatorImpl.class, remap = false)
public abstract class LightingCalculatorImplMixin {
	@Shadow @Final public float[] light;
	
	@Inject(
		method = "calculateForQuad(Lnet/modificationstation/stationapi/api/client/render/model/BakedQuad;)V",
		at = @At("HEAD"), cancellable = true
	)
	private void thelimit_processEmissive(BakedQuad quad, CallbackInfo info) {
		EmissiveQuad emissiveQuad = EmissiveQuad.cast(quad);
		if (!emissiveQuad.isEmissive()) return;
		info.cancel();
		for (int i = 0; i < 4; i++) this.light[i] = 1;
	}
}
