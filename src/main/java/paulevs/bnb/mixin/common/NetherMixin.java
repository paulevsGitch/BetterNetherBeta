package paulevs.bnb.mixin.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.dimension.Nether;
import net.minecraft.util.maths.Vec3f;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.level.StationDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.rendering.FogInfo;

@Mixin(Nether.class)
public class NetherMixin extends Dimension implements StationDimension {
	@Override
	public short getDefaultLevelHeight() {
		return 256;
	}
	
	@Override
	public short getSectionCount() {
		return (short) (this.getActualLevelHeight() >> 4);
	}
	
	@Inject(method = "pregenLight", at = @At("HEAD"), cancellable = true)
	protected void pregenLight(CallbackInfo info) {
		info.cancel();
		for (byte i = 0; i < 16; i++) {
			float delta = i / 15F * 1.2F;
			if (delta > 1.0F) delta = 1.0F;
			this.lightTable[i] = MathHelper.lerp(delta, 0.25F, 1.0F);
		}
	}
	
	@Environment(value= EnvType.CLIENT)
	@Inject(method = "getSkyColour", at = @At("HEAD"), cancellable = true)
	protected void bnb_getSkyColour(CallbackInfoReturnable<Vec3f> info) {
		info.setReturnValue(FogInfo.getVector());
	}
}
