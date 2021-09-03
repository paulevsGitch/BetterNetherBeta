package paulevs.bnb.mixin.client;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.ParticleBase;
import net.minecraft.level.Level;
import net.minecraft.util.maths.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.particles.NetherParticle;
import paulevs.bnb.util.ClientUtil;

import java.util.ArrayList;
import java.util.List;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
	private static List<ParticleBase> customParticles = new ArrayList<ParticleBase>(128);
	private static final int BNB_PARTICLES_TEXTURE = ClientUtil.getTextureID("particles");
	
	@Shadow
	private TextureManager textureManager;
	
	@Inject(method = "method_323", at = @At("TAIL"))
	private void bnb_clearParticles(Level arg, CallbackInfo info) {
		customParticles.clear();
	}
	
	@Inject(method = "addParticle", at = @At("HEAD"), cancellable = true)
	private void bnb_addParticle(ParticleBase particle, CallbackInfo info) {
		if (particle instanceof NetherParticle) {
			if (customParticles.size() > 127) {
				customParticles.remove(0);
			}
			customParticles.add(particle);
			info.cancel();
		}
	}
	
	@Inject(method = "method_320", at = @At("TAIL"))
	private void bnb_particleTick(CallbackInfo info) {
		for (int i = 0; i < customParticles.size(); i++) {
			ParticleBase particle = customParticles.get(i);
			particle.tick();
			if (particle.removed) {
				customParticles.remove(i);
				i--;
			}
		}
	}
	
	@Inject(method = "method_324", at = @At("TAIL"))
	private void bnb_renderParticles(EntityBase entity, float f, CallbackInfo info) {
		if (!customParticles.isEmpty()) {
			float yaw = entity.yaw * (float) Math.PI / 180.0F;
			float pitch = entity.pitch * (float) Math.PI / 180.0F;
			float yawCos = MathHelper.cos(yaw);
			float yawSin = MathHelper.sin(yaw);
			float pitchSin = MathHelper.sin(pitch);
			float pitchCos = MathHelper.cos(pitch);
			float var5 = -yawSin * pitchSin;
			float var6 = yawCos * pitchSin;
			
			textureManager.bindTexture(BNB_PARTICLES_TEXTURE);
			
			Tessellator tessellator = Tessellator.INSTANCE;
			tessellator.start();
			for (int i = 0; i < customParticles.size(); i++) {
				ParticleBase particle = customParticles.get(i);
				particle.method_2002(tessellator, f, yawCos, pitchCos, yawSin, var5, var6);
			}
			tessellator.draw();
		}
	}
}
