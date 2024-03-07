package paulevs.bnb.mixin.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.Level;
import net.minecraft.level.dimension.NetherPortalManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.achievement.BNBAchievements;

@Mixin(NetherPortalManager.class)
public class NetherPortalManagerMixin {
	@Inject(method = "teleport", at = @At("RETURN"))
	private void bnb_travelToNether(Level level, Entity entity, CallbackInfo info) {
		if (!(entity instanceof PlayerEntity player)) return;
		if (player.dimensionId != -1) return;
		player.incrementStat(BNBAchievements.WARM_WELCOME);
	}
}
