package paulevs.bnb.mixin.common;

import net.minecraft.level.LevelProperties;
import net.minecraft.util.io.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.weather.BNBWeatherManager;
import paulevs.bnb.weather.WeatherType;

@Mixin(LevelProperties.class)
public class LevelPropertiesMixin {
	@Inject(method = "<init>(Lnet/minecraft/util/io/CompoundTag;)V", at = @At("TAIL"))
	private void bnb_readTag(CompoundTag tag, CallbackInfo info) {
		WeatherType weather = WeatherType.getByID(tag.getByte("bnb_weather_type"));
		int length = tag.getInt("bnb_weather_length");
		BNBWeatherManager.setWeather(weather, length);
	}
	
	@Inject(method = "updateProperties", at = @At("TAIL"))
	private void bnb_updateTag(CompoundTag to, CompoundTag from, CallbackInfo ci) {
		to.put("bnb_weather_type", (byte) BNBWeatherManager.getCurrentWeather().ordinal());
		to.put("bnb_weather_length", BNBWeatherManager.getCurrentWeatherLength());
	}
}
