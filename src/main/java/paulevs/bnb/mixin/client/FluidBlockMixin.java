package paulevs.bnb.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.rendering.LavaRenderer;

@Mixin(FluidBlock.class)
public abstract class FluidBlockMixin extends Block {
	public FluidBlockMixin(int i, Material arg) {
		super(i, arg);
	}
	
	@Inject(method = "getTexture", at = @At("HEAD"), cancellable = true)
	private void bnb_changeLavaTexture(int side, CallbackInfoReturnable<Integer> info) {
		if (this != STILL_LAVA && this != FLOWING_LAVA) return;
		int texture = switch (side) {
			case 0, 1 -> LavaRenderer.STILL_TEXTURES[((LavaRenderer.POS.z & 3) << 2) | (LavaRenderer.POS.x & 3)];
			default -> LavaRenderer.flowTexture;
		};
		info.setReturnValue(texture);
	}
	
	@Environment(EnvType.CLIENT)
	@ModifyReturnValue(method = "getBrightness", at = @At("RETURN"))
	private float bnb_changeLavaBrightness(float original) {
		return this == STILL_LAVA || this == FLOWING_LAVA ? original * 2.0F : original;
	}
}
