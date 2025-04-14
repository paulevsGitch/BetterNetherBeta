package paulevs.bnb.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityEntry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.living.monster.GhastEntity;
import net.minecraft.level.Level;
import net.minecraft.level.LevelMonsterSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.world.decorator.BNBChunkStatus;
import paulevs.bnb.world.decorator.BNBWorldChunk;

@Mixin(LevelMonsterSpawner.class)
public class LevelMonsterSpawnerMixin {
	@Unique private static Class<?> bnb_spawnEntity;
	
	@WrapOperation(method = "spawnEntities", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/Level;canSuffocate(III)Z"
	))
	private static boolean bnb_testChunk(Level level, int x, int y, int z, Operation<Boolean> original) {
		if (level.dimension.id == -1) {
			BNBChunkStatus status = BNBWorldChunk.cast(level.getChunk(x, z)).bnb_getStatus();
			if (status != BNBChunkStatus.FINISHED) return false;
		}
		return original.call(level, x, y, z);
	}
	
	@WrapOperation(method = "spawnEntities", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/entity/EntityType;getSpawnMaterial()Lnet/minecraft/block/material/Material;"
	))
	private static Material bnb_testMaterial(
		EntityType type, Operation<Material> original,
		@Local(ordinal = 0) EntityEntry entry
	) {
		bnb_spawnEntity = entry.entryClass;
		if (bnb_spawnEntity == GhastEntity.class) {
			return Material.AIR;
		}
		return original.call(type);
	}
	
	@Inject(method = "canSpawnEntity", at = @At("HEAD"), cancellable = true)
	private static void bnb_canSpawnEntity(EntityType type, Level level, int x, int y, int z, CallbackInfoReturnable<Boolean> info) {
		if (bnb_spawnEntity == GhastEntity.class) {
			info.setReturnValue(level.isAir(x, y, z));
		}
	}
}
