package paulevs.bnb.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.TileRenderer;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.util.BlockUtil;

@Mixin(value = TileRenderer.class, priority = 100)
public class TileRendererMixin {
	private static boolean active = false;
	
	/**
	 * Main method to render block in the world
	 */
	@Inject(method = "method_57", at = @At("HEAD"))
	private void renderBlock(BlockBase block, int x, int y, int z, CallbackInfoReturnable<Boolean> info) {
		if (block instanceof BlockWithLight) {
			if (active) {
				active = false;
				method_57(block, x, y, z);
				BlockUtil.setLightPass(false);
			}
			else {
				active = true;
				BlockUtil.setLightPass(true);
			}
		}
		BlockUtil.setItemRender(false);
	}
	
	@Shadow
	public boolean method_57(BlockBase block, int x, int y, int z) {
		return true;
	}
}