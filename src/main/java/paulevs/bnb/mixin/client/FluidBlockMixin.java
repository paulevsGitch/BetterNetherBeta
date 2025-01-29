package paulevs.bnb.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.BNBClient;
import paulevs.bnb.block.property.BNBBlockMaterials;
import paulevs.bnb.rendering.LavaRenderer;

@Mixin(FluidBlock.class)
public abstract class FluidBlockMixin extends Block {
	public FluidBlockMixin(int i, Material arg) {
		super(i, arg);
	}
	
	@Environment(EnvType.CLIENT)
	@Inject(method = "getTexture", at = @At("HEAD"), cancellable = true)
	private void bnb_changeLavaTexture(int side, CallbackInfoReturnable<Integer> info) {
		if (this != STILL_LAVA && this != FLOWING_LAVA) return;
		Minecraft minecraft = BNBClient.getMinecraft();
		if (minecraft == null) return;
		if (minecraft.level == null || minecraft.level.dimension.id != -1) return;
		int texture = switch (side) {
			case 0, 1 -> LavaRenderer.STILL_TEXTURES[((LavaRenderer.POS.z & 3) << 2) | (LavaRenderer.POS.x & 3)];
			default -> LavaRenderer.flowTexture;
		};
		info.setReturnValue(texture);
	}
	
	@Environment(EnvType.CLIENT)
	@ModifyReturnValue(method = "getBrightness", at = @At("RETURN"))
	private float bnb_changeLavaBrightness(float original) {
		Minecraft minecraft = BNBClient.getMinecraft();
		if (minecraft.level == null || minecraft.level.dimension.id != -1) return original;
		return this == STILL_LAVA || this == FLOWING_LAVA ? original * 2.0F : original;
	}
	
	/*@Inject(method = "getFluidAngle", at = @At("HEAD"), cancellable = true)
	private static void bnb_getFluidAngle(BlockView view, int x, int y, int z, Material material, CallbackInfoReturnable<Double> info) {
		if (material != BNBBlockMaterials.SULPHURIC_ACID) return;
		Vec3D vec3D = ((FluidBlock) Block.FLOWING_WATER).getMovementVector(view, x, y, z);
		if (vec3D.x == 0.0 && vec3D.z == 0.0) {
			return -1000.0;
		}
	}*/
	
	@ModifyExpressionValue(method = "getFluidAngle", at = @At(
		value = "FIELD",
		target = "Lnet/minecraft/block/material/Material;WATER:Lnet/minecraft/block/material/Material;"
	))
	private static Material bnb_getLiquidMaterial(Material originalMaterial, @Local(argsOnly = true) Material blockMaterial) {
		return blockMaterial == BNBBlockMaterials.SULPHURIC_ACID ? blockMaterial : originalMaterial;
	}
}
