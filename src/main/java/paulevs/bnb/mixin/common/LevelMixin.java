package paulevs.bnb.mixin.common;

import net.minecraft.level.Level;
import net.minecraft.level.dimension.Dimension;
import net.modificationstation.stationapi.api.world.BlockStateView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.weather.BNBWeatherManager;
import paulevs.bnb.world.generator.BNBDecoratorLevel;

@Mixin(Level.class)
public abstract class LevelMixin implements BlockStateView {
	@Shadow public boolean isRemote;
	@Shadow @Final public Dimension dimension;
	
	@Shadow public abstract boolean isBlockLoaded(int x, int y, int z);
	
	@Inject(method = "processLevel", at = @At("HEAD"))
	private void bnb_tickWeather(CallbackInfo info) {
		if (isRemote || dimension.id != -1) return;
		BNBWeatherManager.tick(Level.class.cast(this));
	}
	
	@Inject(method = "isChunkLoaded", at = @At("HEAD"), cancellable = true)
	private void bnb_isChunkLoaded(int chunkX, int chunkZ, CallbackInfoReturnable<Boolean> info) {
		if ((Object) this instanceof BNBDecoratorLevel) {
			info.setReturnValue(isBlockLoaded(chunkX << 4, 0, chunkZ << 4));
		}
	}
}
