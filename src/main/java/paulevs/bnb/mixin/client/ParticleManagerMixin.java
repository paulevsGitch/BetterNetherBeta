package paulevs.bnb.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.technical.ParticleEntity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.particle.BNBParticle;

import java.util.ArrayList;
import java.util.List;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
	@Shadow private TextureManager textureManager;
	@Unique List<ParticleEntity> bnb_particles = new ArrayList<>(128);
	
	@Inject(method = "addParticle", at = @At("HEAD"), cancellable = true)
	private void bnb_addParticle(ParticleEntity particle, CallbackInfo info) {
		if (!(particle instanceof BNBParticle)) return;
		if (bnb_particles.size() == 128) bnb_particles.remove(0);
		bnb_particles.add(particle);
		info.cancel();
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	private void bnb_tickParticles(CallbackInfo info) {
		for (short i = 0; i < bnb_particles.size(); i++) {
			ParticleEntity particle = bnb_particles.get(i);
			particle.tick();
			if (particle.removed) {
				bnb_particles.remove(i--);
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
		for (ParticleEntity particle : bnb_particles) {
			particle.render(tessellator, delta, x, y, z, width, height);
		}
		tessellator.render();
	}
}
