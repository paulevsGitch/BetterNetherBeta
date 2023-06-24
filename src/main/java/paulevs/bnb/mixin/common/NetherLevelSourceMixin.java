package paulevs.bnb.mixin.common;

import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.source.LevelSource;
import net.minecraft.level.source.NetherLevelSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.world.generator.BNBLevelSource;

@Mixin(NetherLevelSource.class)
public class NetherLevelSourceMixin {
	@Shadow private Level level;
	
	@Inject(method = "getChunk", at = @At("HEAD"), cancellable = true)
	private void bnb_getChunk(int cx, int cz, CallbackInfoReturnable<Chunk> info) {
		info.setReturnValue(BNBLevelSource.makeChunk(this.level, cx, cz));
	}
	
	@Inject(method = "decorate", at = @At("HEAD"), cancellable = true)
	public void decorate(LevelSource source, int cx, int cz, CallbackInfo info) {
		BNBLevelSource.decorateChunk(this.level, cx, cz);
		info.cancel();
	}
}
