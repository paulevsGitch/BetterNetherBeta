package paulevs.bnb.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.level.Level;
import net.minecraft.level.LevelPopulationRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import paulevs.bnb.BNBClient;

@Mixin(LevelPopulationRegion.class)
public class LevelPopulationRegionMixin {
	@Shadow private Level level;
	
	@ModifyReturnValue(method = "getLight(IIII)F", at = @At("RETURN"))
	private float bnb_getLight(float light, @Local(argsOnly = true, ordinal = 1) int y) {
		if (level.dimension.id != -1) return light;
		return BNBClient.getLight(light, y);
	}
	
	@ModifyReturnValue(method = "getBrightness(III)F", at = @At("RETURN"))
	private float bnb_getBrightness(float light, @Local(argsOnly = true, ordinal = 1) int y) {
		if (level.dimension.id != -1) return light;
		return BNBClient.getLight(light, y);
	}
}
