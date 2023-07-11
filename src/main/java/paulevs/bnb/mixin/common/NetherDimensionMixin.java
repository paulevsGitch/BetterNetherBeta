package paulevs.bnb.mixin.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.dimension.BaseDimension;
import net.minecraft.level.dimension.NetherDimension;
import net.minecraft.util.maths.Vec3f;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.level.StationDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.rendering.FogInfo;

@Mixin(NetherDimension.class)
public class NetherDimensionMixin extends BaseDimension implements StationDimension {
	@Override
	public short getDefaultLevelHeight() {
		return 256;
	}
	
	@Override
	public short getSectionCount() {
		return (short) (this.getActualLevelHeight() >> 4);
	}
	
	@Inject(method = "pregenLight", at = @At("HEAD"), cancellable = true)
	private void bnb_pregenLight(CallbackInfo info) {
		info.cancel();
		for (byte i = 0; i < 16; i++) {
			float delta = i / 15F;
			this.lightCurve[i] = MathHelper.lerp(delta, 0.3F, 1.0F);
		}
	}
	
	@Environment(value= EnvType.CLIENT)
	@Inject(method = "getSkyColor", at = @At("HEAD"), cancellable = true)
	protected void bnb_getSkyColor(CallbackInfoReturnable<Vec3f> info) {
		info.setReturnValue(FogInfo.getVector());
	}
}
