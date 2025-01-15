package paulevs.bnb.mixin.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.level.Level;
import net.minecraft.level.dimension.NetherPortalManager;
import net.modificationstation.stationapi.impl.worldgen.FogRendererImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.BNBClient;
import paulevs.bnb.sound.BNBSoundManager;
import paulevs.bnb.sound.BNBWeatherSounds;
import paulevs.bnb.util.BNBPortalManager;

@Mixin(NetherPortalManager.class)
public class NetherPortalManagerMixin {
	@ModifyConstant(method = "setPositionToPortal", constant = @Constant(intValue = 128))
	private int bnb_changeSearchRadius(int constant) {
		return 2;
	}
	
	@Inject(method = "makePortal", at = @At("HEAD"), cancellable = true)
	private void bnb_waitForChunk(Level level, Entity entity, CallbackInfoReturnable<Boolean> info) {
		if (BNBPortalManager.makePortal(level, entity)) {
			info.setReturnValue(true);
		}
	}
	
	@Environment(EnvType.CLIENT)
	@Inject(method = "setPositionToPortal", at = @At("RETURN"))
	private void bnb_updateClient(Level level, Entity entity, CallbackInfoReturnable<Boolean> info) {
		if (!info.getReturnValue()) return;
		BNBClient.getMinecraft().level = level;
		FogRendererImpl.setupFog(BNBClient.getMinecraft(), 0.0F);
		boolean isNether = level.dimension.id == -1;
		BNBSoundManager.setInTheNether(isNether);
		BNBWeatherSounds.setInTheNether(isNether);
	}
}
