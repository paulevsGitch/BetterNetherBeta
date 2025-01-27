package paulevs.bnb.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.technical.FallingBlockEntity;
import net.minecraft.entity.technical.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.property.BNBBlockProperties;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {
	@Shadow public int block;
	
	public FallingBlockEntityMixin(Level object) {
		super(object);
	}
	
	@Inject(method = "tick", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/Level;getBlockID(III)I",
		shift = Shift.BEFORE
	))
	private void bnb_ashFall(CallbackInfo info, @Local(index = 1) int x, @Local(index = 2) int y, @Local(index = 3) int z) {
		if (!onGround) return;
		// TODO remove that after StAPI fix
		if (block == BNBBlocks.ASH_BLOCK.id) {
			BlockState state = level.getBlockState(x, y, z);
			if (!state.isOf(BNBBlocks.ASH_LAYER)) return;
			level.setBlockState(x, y, z, BNBBlocks.ASH_BLOCK.getDefaultState());
			level.setBlockState(x, y + 1, z, state);
			remove();
		}
		else if (block == BNBBlocks.ASH_LAYER.id) {
			BlockState state = level.getBlockState(x, y, z);
			if (!state.isOf(BNBBlocks.ASH_LAYER) || !level.isAir(x, y + 1, z)) return;
			int layer = state.get(BNBBlockProperties.LAYER) + 1;
			state = layer == 3 ? BNBBlocks.ASH_BLOCK.getDefaultState() : state.with(BNBBlockProperties.LAYER, layer);
			level.setBlockState(x, y, z, state);
			remove();
		}
	}
	
	@WrapOperation(method = "tick", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/entity/technical/FallingBlockEntity;dropItem(II)Lnet/minecraft/entity/technical/ItemEntity;"
	))
	private ItemEntity bnb_fixDrop(FallingBlockEntity entity, int id, int count, Operation<ItemEntity> original) {
		level.spawnEntity(new ItemEntity(level, x, y, z, new ItemStack(Block.BY_ID[block])));
		return null;
	}
}
