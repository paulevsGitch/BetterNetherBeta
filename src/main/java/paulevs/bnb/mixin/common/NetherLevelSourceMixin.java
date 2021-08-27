package paulevs.bnb.mixin.common;

import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.source.NetherLevelSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.world.NetherGenerator;

@Mixin(NetherLevelSource.class)
public class NetherLevelSourceMixin {
	@Shadow
	private Level level;
	
	@Inject(method = "<init>*", at = @At("TAIL"))
	private void bnb_onInit(Level level, long seed, CallbackInfo info) {
		NetherGenerator.init(seed);
	}
	
	@Inject(method = "getChunk", at = @At("HEAD"), cancellable = true)
	private void bnb_customNetherGen(int x, int z, CallbackInfoReturnable<Chunk> info) {
		Chunk chunk = new Chunk(this.level, new byte[32768], x, z);
		NetherGenerator.generateChunk(chunk);
		info.setReturnValue(chunk);
	}
}
