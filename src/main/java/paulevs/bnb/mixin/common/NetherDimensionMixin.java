package paulevs.bnb.mixin.common;

import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.dimension.NetherDimension;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.world.StationDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetherDimension.class)
public class NetherDimensionMixin extends Dimension implements StationDimension {
	@Override
	public short getDefaultWorldHeight() {
		return 256;
	}
	
	// Unlock when new API
	/*@Override
	public int getHeight() {
		return 256;
	}*/
	
	@Inject(method = "pregenLight", at = @At("HEAD"), cancellable = true)
	private void bnb_pregenLight(CallbackInfo info) {
		info.cancel();
		for (byte i = 0; i < 16; i++) {
			float delta = i / 15F;
			this.lightCurve[i] = MathHelper.lerp(delta, 0.3F, 1.0F);
		}
	}
	
	/*@Environment(value= EnvType.CLIENT)
	@Inject(method = "getSkyColor", at = @At("HEAD"), cancellable = true)
	protected void bnb_getSkyColor(CallbackInfoReturnable<Vec3f> info) {
		info.setReturnValue(FogInfo.getVector());
	}*/
}
