package paulevs.bnb.mixin.common;

import net.minecraft.level.biome.Biome;
import net.minecraft.level.biome.HellBiome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HellBiome.class)
public abstract class HellBiomeMixin extends Biome {
	@Inject(method = "<init>", at = @At("TAIL"))
	private void bnb_initHell(CallbackInfo info) {
		monsters.clear();
	}
}
