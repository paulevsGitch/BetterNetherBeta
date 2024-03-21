package paulevs.bnb.mixin.common;

import net.minecraft.level.Level;
import net.minecraft.level.dimension.Dimension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.weather.BNBWeatherManager;

@Mixin(Level.class)
public class LevelMixin {
	@Shadow public boolean isRemote;
	
	@Shadow @Final public Dimension dimension;
	
	@Inject(method = "processEntities", at = @At("HEAD"))
	private void bnb_tickWeather(CallbackInfo info) {
		if (isRemote || dimension.id != -1) return;
		BNBWeatherManager.tick(Level.class.cast(this));
	}
}
