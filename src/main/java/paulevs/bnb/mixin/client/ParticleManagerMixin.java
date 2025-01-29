package paulevs.bnb.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.technical.ParticleEntity;
import net.minecraft.level.Level;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.BNBClient;
import paulevs.bnb.particle.BNBParticle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
	@Shadow private TextureManager textureManager;
	@Unique private final List<ParticleEntity> bnb_particlesCutout = new ArrayList<>(256);
	@Unique private final List<ParticleEntity> bnb_particlesTranslucent = new ArrayList<>(256);
	@Unique private final Comparator<ParticleEntity> bnb_sorter = (p1, p2) -> {
		Entity viewEntity = BNBClient.getMinecraft().viewEntity;
		float d1 = (float) p1.distanceToSqr(viewEntity);
		float d2 = (float) p2.distanceToSqr(viewEntity);
		return Float.compare(d2, d1);
	};
	
	@Inject(method = "addParticle", at = @At("HEAD"), cancellable = true)
	private void bnb_addParticle(ParticleEntity particle, CallbackInfo info) {
		if (!(particle instanceof BNBParticle bnbParticle)) return;
		List<ParticleEntity> list = bnbParticle.isTranslucent() ? bnb_particlesTranslucent : bnb_particlesCutout;
		if (list.size() == 256) list.remove(0);
		list.add(particle);
		info.cancel();
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	private void bnb_tickParticles(CallbackInfo info) {
		for (short i = 0; i < bnb_particlesCutout.size(); i++) {
			ParticleEntity particle = bnb_particlesCutout.get(i);
			particle.tick();
			if (particle.removed) {
				bnb_particlesCutout.remove(i--);
			}
		}
		for (short i = 0; i < bnb_particlesTranslucent.size(); i++) {
			ParticleEntity particle = bnb_particlesTranslucent.get(i);
			particle.tick();
			if (particle.removed) {
				bnb_particlesTranslucent.remove(i--);
			}
		}
	}
	
	@Inject(method = "renderAll", at = @At("TAIL"))
	private void bnb_renderAllParticles(
		Entity entity, float delta, CallbackInfo info,
		@Local(ordinal = 1) float x,
		@Local(ordinal = 2) float z,
		@Local(ordinal = 3) float width,
		@Local(ordinal = 4) float height,
		@Local(ordinal = 5) float y
	) {
		int texture = textureManager.getTextureId("/assets/bnb/stationapi/textures/environment/particles.png");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		Tessellator tessellator = Tessellator.INSTANCE;
		
		tessellator.start();
		for (ParticleEntity particle : bnb_particlesCutout) {
			particle.render(tessellator, delta, x, y, z, width, height);
		}
		tessellator.render();
		
		tessellator.start();
		GL11.glEnable(GL11.GL_BLEND);
		bnb_particlesTranslucent.sort(bnb_sorter);
		for (ParticleEntity particle : bnb_particlesTranslucent) {
			particle.render(tessellator, delta, x, y, z, width, height);
		}
		GL11.glDisable(GL11.GL_BLEND);
		tessellator.render();
	}
	
	@Inject(method = "setLevel", at = @At("HEAD"))
	private void bnb_setLevel(Level level, CallbackInfo info) {
		bnb_particlesCutout.clear();
	}
}
