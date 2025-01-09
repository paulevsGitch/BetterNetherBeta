package paulevs.bnb.mixin.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.living.FlyingEntity;
import net.minecraft.entity.living.monster.GhastEntity;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.Vec3D;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.sound.BNBSoundManager;

@Mixin(GhastEntity.class)
public abstract class GhastEntityMixin extends FlyingEntity {
	public GhastEntityMixin(Level arg) {
		super(arg);
	}
	
	@Inject(method = "canSpawn", at = @At("HEAD"), cancellable = true)
	private void bnb_canSpawn(CallbackInfoReturnable<Boolean> info) {
		Box bounds = Box.createAndCache(x - 256, y - 256, z - 256, x + 256, y + 256, z + 256);
		if (level.getEntities(GhastEntity.class, bounds).size() > 1) {
			info.setReturnValue(false);
		}
	}
	
	@ModifyConstant(method = "getSoundVolume", constant = @Constant(floatValue = 10.0F))
	private float bnb_changeVolume(float original) {
		return 1.0F;
	}
	
	@Override
	public void playAmbientSound() {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			bnb_playAmbientSound();
		}
	}
	
	@Unique
	@Environment(EnvType.CLIENT)
	private void bnb_playAmbientSound() {
		float volume = random.nextFloat() * 0.2F + 0.4F;
		float pitch = random.nextFloat() * 0.2F + 0.9F;
		BNBSoundManager.playSound(getAmbientSound(), x, y, z, volume, pitch, 128.0F);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public boolean canRenderFrom(Vec3D pos) {
		return true;
	}
}
