package paulevs.bnb.mixin.client;

import net.minecraft.client.render.AreaRenderer;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.world.decorator.BNBChunkStatus;
import paulevs.bnb.world.decorator.BNBWorldChunk;

@Mixin(AreaRenderer.class)
public class AreaRendererMixin {
	@Shadow public boolean canUpdate;
	@Shadow public boolean isVisible;
	@Shadow public Level level;
	@Shadow public int startX;
	@Shadow public int startZ;
	
	@Inject(method = "update", at = @At("HEAD"), cancellable = true)
	private void bnb_skipUpdate(CallbackInfo info) {
		if (!isVisible || !canUpdate || level == null || level.isRemote || level.dimension.id != -1) return;
		BNBWorldChunk bnbWorldChunk = BNBWorldChunk.cast(level.getChunk(startX, startZ));
		if (bnbWorldChunk != null && bnbWorldChunk.bnb_getStatus() != BNBChunkStatus.FINISHED) {
			info.cancel();
		}
	}
}
