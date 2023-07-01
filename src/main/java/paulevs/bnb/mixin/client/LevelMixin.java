package paulevs.bnb.mixin.client;

import net.minecraft.level.Level;
import net.minecraft.level.dimension.Dimension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public class LevelMixin {
	@Shadow @Final public Dimension dimension;
	@Shadow private int field_195;
	
	@Inject(method = "method_248", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/Level;getChunkFromCache(II)Lnet/minecraft/level/chunk/Chunk;",
		ordinal = 0,
		shift = Shift.AFTER
	))
	private void bnb_cancelCaveSound(CallbackInfo info) {
		if (dimension.id == -1) field_195 = 1000;
	}
}
