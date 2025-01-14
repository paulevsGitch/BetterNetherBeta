package paulevs.bnb.mixin.common;

import net.minecraft.entity.Entity;
import net.minecraft.level.Level;
import net.minecraft.level.dimension.NetherPortalManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.world.generator.BNBChunkStatus;
import paulevs.bnb.world.generator.BNBWorldChunk;

@Mixin(NetherPortalManager.class)
public class NetherPortalManagerMixin {
	@ModifyConstant(method = "setPositionToPortal", constant = @Constant(intValue = 128))
	private int bnb_changeSearchRadius(int constant) {
		return 16;
	}
	
	@Inject(method = "makePortal", at = @At("HEAD"))
	private void bnb_waitForChunk(Level level, Entity entity, CallbackInfoReturnable<Boolean> infoReturnable) {
		if (level.dimension.id != -1) return;
		for (int i = 0; i < 9; i++) {
			BNBWorldChunk chunk = BNBWorldChunk.cast(level.getChunk(
				entity.chunkX + (i % 3) - 1,
				entity.chunkZ + i / 3 - 1
			));
			while (chunk.bnb_getStatus() == BNBChunkStatus.EMPTY) {
				try {
					//noinspection BusyWait
					Thread.sleep(10);
				}
				catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
