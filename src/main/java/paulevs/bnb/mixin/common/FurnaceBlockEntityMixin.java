package paulevs.bnb.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import paulevs.bnb.block.NetherrackFurnaceBlock;
import paulevs.bnb.block.entity.NetherrackFurnaceBlockEntity;

@Mixin(FurnaceBlockEntity.class)
public class FurnaceBlockEntityMixin {
	@ModifyConstant(method = "tick", constant = @Constant(intValue = 200))
	private int bnb_changeTickTime(int original) {
		return FurnaceBlockEntity.class.cast(this) instanceof NetherrackFurnaceBlockEntity ? 800 : 200;
	}
	
	@Environment(EnvType.CLIENT)
	@ModifyConstant(method = {"getCookTimeDelta", "getFuelTimeDelta"}, constant = @Constant(intValue = 200))
	private int bnb_changeClientTickTime(int original) {
		return FurnaceBlockEntity.class.cast(this) instanceof NetherrackFurnaceBlockEntity ? 800 : 200;
	}
	
	@WrapOperation(method = "tick", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/block/FurnaceBlock;updateFurnaceState(ZLnet/minecraft/level/Level;III)V")
	)
	private void bnb_changeFurnaceState(boolean lit, Level level, int x, int y, int z, Operation<Void> operation) {
		if (FurnaceBlockEntity.class.cast(this) instanceof NetherrackFurnaceBlockEntity) {
			NetherrackFurnaceBlock.updateState(lit, level, x, y, z);
		}
		else operation.call(lit, level, x, y, z);
	}
}
