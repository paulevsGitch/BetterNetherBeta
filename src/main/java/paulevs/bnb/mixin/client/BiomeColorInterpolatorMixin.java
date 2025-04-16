package paulevs.bnb.mixin.client;

import net.minecraft.level.biome.BiomeSource;
import net.modificationstation.stationapi.impl.worldgen.BiomeColorInterpolator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeColorInterpolator.class)
public class BiomeColorInterpolatorMixin {
	@Shadow(remap = false) private boolean initiated;
	@Unique private BiomeSource oldSource;
	
	@Inject(method = "getColor", at = @At(value = "HEAD", remap = false))
	private void bnb_checkSource(BiomeSource source, double x, double z, CallbackInfoReturnable<Integer> info) {
		if (oldSource != source) initiated = false;
		oldSource = source;
	}
}
