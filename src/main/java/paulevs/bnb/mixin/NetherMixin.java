package paulevs.bnb.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.dimension.Nether;
import paulevs.bnb.world.NetherBiomeSource;

@Mixin(Nether.class)
public abstract class NetherMixin extends Dimension {
	@Inject(method = "initBiomeSource", at = @At("TAIL"))
	private void bnb_initBiomeSource(CallbackInfo info) {
		this.biomeSource = new NetherBiomeSource(level.getSeed());
	}
}
