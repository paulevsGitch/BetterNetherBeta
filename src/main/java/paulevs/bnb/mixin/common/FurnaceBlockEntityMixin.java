package paulevs.bnb.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.block.BNBFurnaceBlock;

@Mixin(FurnaceBlockEntity.class)
public abstract class FurnaceBlockEntityMixin extends BlockEntity {
	@ModifyConstant(method = "tick", constant = @Constant(intValue = 200))
	private int bnb_changeTickTime(int original) {
		if (getBlock() instanceof BNBFurnaceBlock furnace) {
			return furnace.cookingTime;
		}
		return original;
	}
	
	@Environment(EnvType.CLIENT)
	@ModifyConstant(method = {"getCookTimeDelta", "getFuelTimeDelta"}, constant = @Constant(intValue = 200))
	private int bnb_changeClientTickTime(int original) {
		if (getBlock() instanceof BNBFurnaceBlock furnace) {
			return furnace.cookingTime;
		}
		return original;
	}
	
	@WrapOperation(method = "tick", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/block/FurnaceBlock;updateFurnaceState(ZLnet/minecraft/level/Level;III)V")
	)
	private void bnb_changeFurnaceState(boolean lit, Level level, int x, int y, int z, Operation<Void> operation) {
		if (getBlock() instanceof BNBFurnaceBlock) {
			BNBFurnaceBlock.updateState(lit, level, x, y, z);
		}
		else operation.call(lit, level, x, y, z);
	}
	
	@Inject(method = "getInventoryName", at = @At("HEAD"), cancellable = true)
	private void bnb_getFurnaceName(CallbackInfoReturnable<String> info) {
		if (getBlock() instanceof BNBFurnaceBlock furnace) {
			info.setReturnValue(I18n.translate(furnace.guiTranslationKey));
		}
	}
}
