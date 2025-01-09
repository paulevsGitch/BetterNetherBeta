package paulevs.bnb.mixin.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.stat.Stat;
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
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void bnb_netherAchievement(CallbackInfo info) {
		if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) return;
		if (dimensionId == -1) {
			if (BNBAchievements.readStat(BNBAchievements.THE_WAYS) == 0) incrementStat(BNBAchievements.THE_WAYS);
			if (BNBAchievements.readStat(BNBAchievements.WARM_WELCOME) == 0) incrementStat(BNBAchievements.WARM_WELCOME);
		}
	}
}
