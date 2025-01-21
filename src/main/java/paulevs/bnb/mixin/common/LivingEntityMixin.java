package paulevs.bnb.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import paulevs.bnb.block.plant.BNBVineBlock;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(Level level) {
		super(level);
	}
	
	@WrapOperation(method = "isOnLadder", at = @At(value = "INVOKE", target = "Lnet/minecraft/level/Level;getBlockID(III)I"))
	private int bnb_isOnLadder(Level level, int x, int y, int z, Operation<Integer> original) {
		BlockState state = level.getBlockState(x, y, z);
		if (state.getBlock() instanceof BNBVineBlock) return Block.LADDER.id;
		return state.getBlock().id;
	}
}
