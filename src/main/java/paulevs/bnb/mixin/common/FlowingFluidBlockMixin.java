package paulevs.bnb.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import paulevs.bnb.block.fluid.SulphuricAcidFlowingBlock;
import paulevs.bnb.block.property.BNBBlockMaterials;

@Mixin(FlowingFluidBlock.class)
public abstract class FlowingFluidBlockMixin extends Block {
	public FlowingFluidBlockMixin(int id, Material material) {
		super(id, material);
	}
	
	@WrapOperation(method = "setBlockWithUpdate", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/Level;setBlockInChunk(IIIII)Z"
	))
	private boolean bnb_changeFluidCall(Level level, int x, int y, int z, int id, int meta, Operation<Boolean> original) {
		if (FlowingFluidBlock.class.cast(this) instanceof SulphuricAcidFlowingBlock block) {
			level.getChunk(x, z).setBlockStateWithMetadata(x & 15, y, z & 15, block.stillFluid.getDefaultState(), meta);
			return true;
		}
		return original.call(level, x, y, z, id, meta);
	}
	
	@ModifyExpressionValue(method = "onScheduledTick", at = @At(
		value = "FIELD",
		target = "Lnet/minecraft/block/material/Material;WATER:Lnet/minecraft/block/material/Material;"
	))
	private Material bnb_materialCheck(Material original) {
		return material == BNBBlockMaterials.SULPHURIC_ACID ? material : original;
	}
}
