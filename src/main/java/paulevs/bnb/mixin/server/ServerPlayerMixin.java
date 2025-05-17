package paulevs.bnb.mixin.server;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.entity.living.player.ServerPlayer;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.maths.Vec2I;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import paulevs.bnb.world.decorator.BNBChunkStatus;
import paulevs.bnb.world.decorator.BNBWorldChunk;

import java.util.List;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends PlayerEntity {
	@Shadow public MinecraftServer server;
	
	public ServerPlayerMixin(Level level) {
		super(level);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@WrapOperation(method = "tick(Z)V", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", ordinal = 0))
	private Object bnb_getChunkPos(List trackedChunkList, int index, Operation<Object> original) {
		Vec2I chunkPos = (Vec2I) original.call(trackedChunkList, index);
		if (dimensionId != -1) return chunkPos;
		Level level = server.getLevel(dimensionId);
		if (
			bnb_getStatus(level, chunkPos.x, chunkPos.z) == BNBChunkStatus.FINISHED
			&& bnb_getStatus(level, chunkPos.x + 1, chunkPos.z) == BNBChunkStatus.FINISHED
			&& bnb_getStatus(level, chunkPos.x - 1, chunkPos.z) == BNBChunkStatus.FINISHED
			&& bnb_getStatus(level, chunkPos.x, chunkPos.z + 1) == BNBChunkStatus.FINISHED
			&& bnb_getStatus(level, chunkPos.x, chunkPos.z - 1) == BNBChunkStatus.FINISHED
		) return chunkPos;
		trackedChunkList.remove(index);
		trackedChunkList.add(chunkPos);
		return null;
	}
	
	@Unique
	private BNBChunkStatus bnb_getStatus(Level level, int x, int z) {
		Chunk chunk = level.getChunkFromCache(x, z);
		if (chunk instanceof BNBWorldChunk worldChunk) {
			return worldChunk.bnb_getStatus();
		}
		return BNBChunkStatus.FINISHED;
	}
}
