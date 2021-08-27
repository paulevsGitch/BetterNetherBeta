package paulevs.bnb.mixin.common;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Portal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.interfaces.BlockWithLight;

@Mixin(Portal.class)
public class PortalMixin implements BlockWithLight {
	@Inject(method = "<init>*", at = @At("TAIL"))
	private void bnb_initPortalBlock(int id, int texture, CallbackInfo info) {
		BlockBase.EMITTANCE[id] = 15;
	}
	
	@Override
	public float getEmissionIntensity() {
		return 10;
	}
}
