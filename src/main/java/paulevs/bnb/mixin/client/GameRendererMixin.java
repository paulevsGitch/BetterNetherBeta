package paulevs.bnb.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.dimension.Nether;
import net.minecraft.sortme.GameRenderer;
import net.minecraft.util.maths.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.particles.BiomeParticle;
import paulevs.bnb.util.ClientUtil;
import paulevs.bnb.util.MHelper;
import paulevs.bnb.world.biome.NetherBiome;

@Mixin(value = GameRenderer.class, priority = 100)
public class GameRendererMixin {
	@Shadow
	private Minecraft minecraft;
	
	@Shadow
	private float field_2350 = 0.0F;
	
	private float bnb_fogDensity = 1F;
	
	@Inject(method = "method_1846", at = @At("HEAD"))
	private void bnb_renderParticles(CallbackInfo info) {
		if (minecraft.level.dimension instanceof Nether) {
			double x = minecraft.player.x + ClientUtil.getRandom().nextDouble() * 16 - 8;
			double y = minecraft.player.y + ClientUtil.getRandom().nextDouble() * 16 - 8;
			double z = minecraft.player.z + ClientUtil.getRandom().nextDouble() * 16 - 8;
			Biome biome = minecraft.level.getBiomeSource().getBiome(MathHelper.floor(x), MathHelper.floor(z));
			if (biome instanceof NetherBiome) {
				NetherBiome netherBiome = (NetherBiome) biome;
				if (ClientUtil.getRandom().nextFloat() <= netherBiome.getParticleChance()) {
					minecraft.particleManager.addParticle(new BiomeParticle(
						minecraft.level,
						x, y, z,
						netherBiome.getParticleID(ClientUtil.getRandom()),
						netherBiome.isParticlesEmissive()
					));
				}
			}
			
			double dx = minecraft.player.x / 8.0;
			double dz = minecraft.player.z / 8.0;
			int x1 = MathHelper.floor(dx) << 3;
			int z1 = MathHelper.floor(dz) << 3;
			int x2 = x1 + 8;
			int z2 = z1 + 8;
			dx -= x1 >> 3;
			dz -= z1 >> 3;
			float a = bnb_getDensity(x1, z1);
			float b = bnb_getDensity(x2, z1);
			float c = bnb_getDensity(x1, z2);
			float d = bnb_getDensity(x2, z2);
			
			a = MHelper.lerp(a, b, (float) dx);
			b = MHelper.lerp(c, d, (float) dx);
			bnb_fogDensity = MHelper.lerp(a, b, (float) dz);
		}
	}
	
	@Inject(method = "method_1842", at = @At("TAIL"))
	private void bnb_renderFog(int i, float f, CallbackInfo info) {
		if (minecraft.level != null && minecraft.level.dimension instanceof Nether) {
			GL11.glFogf(GL11.GL_FOG_START, 0.0F);
			GL11.glFogf(GL11.GL_FOG_END, field_2350 / bnb_fogDensity);
		}
	}
	
	private float bnb_getDensity(int x, int z) {
		Biome biome = minecraft.level.getBiomeSource().getBiome(x, z);
		if (biome instanceof NetherBiome) {
			return ((NetherBiome) biome).getFogDensity();
		}
		return 1.0F;
	}
}
