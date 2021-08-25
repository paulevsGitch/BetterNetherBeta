package paulevs.bnb.mixin.client;

import net.minecraft.block.BlockBase;
import net.minecraft.level.TileView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.BlockUtil;

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
		else if (block == BlockBase.SOUL_SAND) {
			info.setReturnValue(TextureListener.getBlockTexture("soul_sand"));
			info.cancel();
		}
		/*else if (block == BlockBase.STILL_LAVA) {
			info.setReturnValue(TextureListener.getBlockTexture("lava_still"));
			info.cancel();
		}*/
	}
	
	@Inject(method = "method_1604", at = @At("HEAD"), cancellable = true)
	private void bnb_getLight(TileView world, int x, int y, int z, CallbackInfoReturnable<Float> info) {
		if (this instanceof BlockWithLight && BlockUtil.isLightPass()) {
			info.setReturnValue(((BlockWithLight) (Object) this).getEmissionIntensity());
			info.cancel();
		}
	}
}
