package paulevs.bnb.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.impl.world.FlattenedWorldManager;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.world.decorator.BNBChunkStatus;
import paulevs.bnb.world.decorator.BNBWorldChunk;

@Mixin(FlattenedWorldManager.class)
public class FlattenedWorldManagerMixin {
	@Inject(method = "saveChunk", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/Level;checkSessionLock()V",
		shift = Shift.AFTER
	))
	private static void bnb_saveChunk(FlattenedChunk chunk, Level level, CompoundTag chunkTag, CallbackInfo info) {
		if (level.dimension.id != -1) return;
		BNBChunkStatus status = BNBWorldChunk.cast(chunk).bnb_getStatus();
		if (status == null) status = BNBChunkStatus.EMPTY;
		chunkTag.put("bnb:chunk_status", status.id);
	}
	
	@Inject(method = "loadChunk", at = @At(
		value = "INVOKE",
		target = "Lnet/modificationstation/stationapi/impl/world/chunk/FlattenedChunk;loadStoredHeightmap([B)V",
		shift = Shift.AFTER
	))
	private static void bnb_saveChunk(Level level, CompoundTag chunkTag, CallbackInfoReturnable<FlattenedChunk> info, @Local FlattenedChunk chunk) {
		if (level.dimension.id != -1) return;
		BNBChunkStatus status;
		if (chunkTag.containsKey("bnb:chunk_status")) {
			byte statusID = chunkTag.getByte("bnb:chunk_status");
			status = BNBChunkStatus.fromID(statusID);
		}
		else {
			status = chunk.decorated ? BNBChunkStatus.FINISHED : BNBChunkStatus.TERRAIN;
			chunk.decorated = true;
		}
		BNBWorldChunk.cast(chunk).bnb_setStatus(status);
	}
}
