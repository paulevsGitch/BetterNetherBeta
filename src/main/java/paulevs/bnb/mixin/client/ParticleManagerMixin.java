package paulevs.bnb.mixin.client;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.ParticleBase;
import net.minecraft.level.Level;
import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.particles.BiomeParticle;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
	private static List<ParticleBase> biomeParticles = new ArrayList<ParticleBase>(128);
	private static int bnb_particleTexID = -1;
	
	@Shadow
	private TextureManager textureManager;
	
	@Inject(method = "method_323", at = @At("TAIL"))
	private void bnb_clearParticles(Level arg, CallbackInfo info) {
		biomeParticles.clear();
	}
	
	@Inject(method = "addParticle", at = @At("HEAD"), cancellable = true)
	private void bnb_addParticle(ParticleBase particle, CallbackInfo info) {
		if (particle instanceof BiomeParticle) {
			if (biomeParticles.size() > 127) {
				biomeParticles.remove(0);
			}
			biomeParticles.add(particle);
			info.cancel();
		}
	}
	
	@Inject(method = "method_320", at = @At("TAIL"))
	private void bnb_particleTick(CallbackInfo info) {
		for (int i = 0; i < biomeParticles.size(); i++) {
			ParticleBase particle = biomeParticles.get(i);
			particle.tick();
			if (particle.removed) {
				biomeParticles.remove(i);
				i--;
			}
		}
	}
	
	@Inject(method = "method_324", at = @At("TAIL"))
	private void bnb_renderParticles(EntityBase entity, float f, CallbackInfo info) {
		if (!biomeParticles.isEmpty()) {
			float yaw = entity.yaw * (float) Math.PI / 180.0F;
			float pitch = entity.pitch * (float) Math.PI / 180.0F;
			float yawCos = MathHelper.cos(yaw);
			float yawSin = MathHelper.sin(yaw);
			float pitchSin = MathHelper.sin(pitch);
			float pitchCos = MathHelper.cos(pitch);
			float var5 = -yawSin * pitchSin;
			float var6 = yawCos * pitchSin;
			
			if (bnb_particleTexID < 0) {
				bnb_particleTexID = textureManager.getTextureId("/assets/" + BetterNetherBeta.MOD_ID + "/textures/particles.png");
			}
			textureManager.bindTexture(bnb_particleTexID);
			
			Tessellator tesselator = Tessellator.INSTANCE;
			tesselator.start();
			for (int i = 0; i < biomeParticles.size(); i++) {
				ParticleBase particle = biomeParticles.get(i);
				particle.method_2002(tesselator, f, yawCos, pitchCos, yawSin, var5, var6);
			}
			tesselator.draw();
		}
	}
}
