package paulevs.bnb.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockBase;
import paulevs.bnb.listeners.TextureListener;

@Mixin(BlockBase.class)
public abstract class BlockBaseMixin {
	@Inject(method = "getTextureForSide", at = @At("HEAD"), cancellable = true)
	private void bnb_getTextureForSide(int side, int meta, CallbackInfoReturnable<Integer> info) {
		BlockBase block = (BlockBase) (Object) this;
		if (block == BlockBase.NETHERRACK) {
			info.setReturnValue(TextureListener.getBlockTexture("netherrack"));
			info.cancel();
		}
		else if (block == BlockBase.GLOWSTONE) {
			info.setReturnValue(TextureListener.getBlockTexture("glowstone"));
			info.cancel();
		}
	}
}
