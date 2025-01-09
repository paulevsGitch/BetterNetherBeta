package paulevs.bnb.mixin.common;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(value = FlowingFluidBlock.class, priority = 100)
public abstract class FlowingFluidBlockMixin extends FluidBlock {
	public FlowingFluidBlockMixin(int i, Material material) {
		super(i, material);
	}
	
	@Inject(method = "<init>", at = @At("TAIL"))
	private void bnb_onInit(int id, Material material, CallbackInfo info) {
		setTicksRandomly(false);
	}
	
	// TO remove after VBE removal
	@Inject(method = "onScheduledTick", at = @At("HEAD"), cancellable = true)
	private void bnb_update(Level level, int x, int y, int z, Random random, CallbackInfo info) {
		info.cancel();
	}
}
