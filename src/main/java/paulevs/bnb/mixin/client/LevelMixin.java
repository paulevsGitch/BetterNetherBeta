package paulevs.bnb.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.level.Level;
import net.minecraft.level.dimension.Dimension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.BNBClient;

@Mixin(Level.class)
public class LevelMixin {
	@Shadow @Final public Dimension dimension;
	@Shadow private int caveSoundTicks;
	
	@Inject(method = "processLoadedChunks", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/Level;getChunkFromCache(II)Lnet/minecraft/level/chunk/Chunk;",
		ordinal = 0,
		shift = Shift.AFTER
	))
	private void bnb_cancelCaveSound(CallbackInfo info) {
		if (dimension.id == -1) caveSoundTicks = 1000;
	}
	
	@ModifyReturnValue(method = "getLight(IIII)F", at = @At("RETURN"))
	private float bnb_getLight(float light, @Local(argsOnly = true, ordinal = 1) int y) {
		if (dimension.id != -1) return light;
		return BNBClient.getLight(light, y);
	}
	
	@ModifyReturnValue(method = "getBrightness(III)F", at = @At("RETURN"))
	private float bnb_getBrightness(float light, @Local(argsOnly = true, ordinal = 1) int y) {
		if (dimension.id != -1) return light;
		return BNBClient.getLight(light, y);
	}
}
