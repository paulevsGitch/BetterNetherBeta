package paulevs.bnb.mixin.client;

import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.rendering.LavaRenderer;

@Mixin(BlockRenderer.class)
public class BlockRendererMixin {
	@Inject(method = "renderFluid", at = @At("HEAD"))
	private void bnb_renderFluidStart(Block block, int x, int y, int z, CallbackInfoReturnable<Boolean> info) {
		LavaRenderer.POS.set(x, y, z);
	}
}
