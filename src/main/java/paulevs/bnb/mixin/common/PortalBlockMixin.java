package paulevs.bnb.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.PortalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.world.BlockStateView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.util.BNBPortalManager;

@Mixin(PortalBlock.class)
public abstract class PortalBlockMixin extends Block {
	public PortalBlockMixin(int id, Material material) {
		super(id, material);
	}
	
	@Inject(method = "onEntityCollision", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/entity/Entity;handleTeleporting()V",
		shift = Shift.BEFORE
	))
	private void bnb_onEntityCollision(Level level, int x, int y, int z, Entity entity, CallbackInfo info) {
		entity.bnb_setPortalOrigin(level, x, y, z);
	}
	
	@Override
	public float getBrightness(BlockView blockView, int x, int y, int z) {
		return 2.0F;
	}
	
	@Inject(method = "tryCreatePortal", at = @At("HEAD"), cancellable = true)
	private void bnb_tryCreatePortal(Level level, int x, int y, int z, CallbackInfoReturnable<Boolean> info) {
		info.setReturnValue(BNBPortalManager.tryCreatePortal(level, x, y, z));
	}
	
	@Inject(method = "onAdjacentBlockUpdate", at = @At("HEAD"), cancellable = true)
	private void bnb_onNeighbourBlockUpdate(Level level, int x, int y, int z, int id, CallbackInfo info) {
		info.cancel();
		if (BNBPortalManager.portalCanExist(level, x, y, z)) return;
		level.setBlockStateWithNotify(x, y, z, States.AIR.get());
	}
	
	@WrapOperation(method = "updateBoundingBox", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/BlockView;getBlockID(III)I"
	))
	private int bnb_changeID(BlockView view, int x, int y, int z, Operation<Integer> original) {
		BlockState state = ((BlockStateView) view).getBlockState(x, y, z);
		return state.isOf(PORTAL) || state.isOf(OBSIDIAN) ? PORTAL.id : 0;
	}
	
	@Environment(EnvType.CLIENT)
	@ModifyConstant(method = "onRandomClientTick", constant = @Constant(floatValue = 1.0F, ordinal = 0))
	private float bob_changeVolume(float original, @Local(argsOnly = true) Level level) {
		return 0.1F + level.random.nextFloat() * 0.1F;
	}
}
