package paulevs.bnb.mixin.common;

import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.util.io.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.achievement.BNBAchievements;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
	@Shadow public int dimensionId;
	@Shadow public abstract void incrementStat(Stat arg);
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void bnb_netherAchievement(CompoundTag tag, CallbackInfo info) {
		if (dimensionId == -1 && BNBAchievements.readStat(BNBAchievements.WARM_WELCOME) == 0) {
			incrementStat(BNBAchievements.WARM_WELCOME);
		}
	}
}
