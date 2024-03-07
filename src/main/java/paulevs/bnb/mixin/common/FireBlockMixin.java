package paulevs.bnb.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.FireBlock;
import net.minecraft.block.PortalBlock;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import paulevs.bnb.achievement.BNBAchievements;

@Mixin(FireBlock.class)
public class FireBlockMixin {
	@SuppressWarnings("unchecked")
	@WrapOperation(method = "onBlockPlaced", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/block/PortalBlock;tryCreatePortal(Lnet/minecraft/level/Level;III)Z"
	))
	private boolean bnb_onPortalCreation(PortalBlock portalBlock, Level level, int x, int y, int z, Operation<Boolean> original) {
		boolean result = original.call(portalBlock, level, x, y, z);
		if (result) {
			level.getEntities(PlayerEntity.class, Box.createAndCache(x - 32, y - 32, z - 32, x + 32, y + 32, z + 32)).forEach(entity -> {
				PlayerEntity player = (PlayerEntity) entity;
				player.incrementStat(BNBAchievements.THE_WAYS);
			});
		}
		return result;
	}
}
