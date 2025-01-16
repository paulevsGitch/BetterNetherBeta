package paulevs.bnb.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.BNBClient;
import paulevs.bnb.block.crafting.BNBFurnaceBlock;

@Mixin(FurnaceBlockEntity.class)
public abstract class FurnaceBlockEntityMixin extends BlockEntity {
	@ModifyConstant(method = "tick", constant = @Constant(intValue = 200))
	private int bnb_changeTickTime(int original) {
		if (bnb_getBlock() instanceof BNBFurnaceBlock furnace) {
			return furnace.cookingTime;
		}
		return original;
	}
	
	@Environment(EnvType.CLIENT)
	@ModifyConstant(method = {"getCookTimeDelta", "getFuelTimeDelta"}, constant = @Constant(intValue = 200))
	private int bnb_changeClientTickTime(int original) {
		if (bnb_getBlock() instanceof BNBFurnaceBlock furnace) {
			return furnace.cookingTime;
		}
		return original;
	}
	
	@WrapOperation(method = "tick", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/block/FurnaceBlock;updateFurnaceState(ZLnet/minecraft/level/Level;III)V")
	)
	private void bnb_changeFurnaceState(boolean lit, Level level, int x, int y, int z, Operation<Void> operation) {
		if (bnb_getBlock() instanceof BNBFurnaceBlock) {
			BNBFurnaceBlock.updateState(lit, level, x, y, z);
		}
		else operation.call(lit, level, x, y, z);
	}
	
	@Environment(EnvType.CLIENT)
	@Inject(method = "getInventoryName", at = @At("HEAD"), cancellable = true)
	private void bnb_getFurnaceName(CallbackInfoReturnable<String> info) {
		if (bnb_getBlock() instanceof BNBFurnaceBlock furnace) {
			info.setReturnValue(I18n.translate(furnace.guiTranslationKey));
		}
	}
	
	@Unique
	private Block bnb_getBlock() {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			return level.getBlockState(x, y, z).getBlock();
		}
		return bnb_getBlockClient();
	}
	
	@Unique
	@Environment(EnvType.CLIENT)
	private Block bnb_getBlockClient() {
		Minecraft minecraft = BNBClient.getMinecraft();
		if (minecraft.level.isRemote) {
			HitResult hit = minecraft.hitResult;
			return minecraft.level.getBlockState(hit.x, hit.y, hit.z).getBlock();
		}
		return getBlock();
	}
}
