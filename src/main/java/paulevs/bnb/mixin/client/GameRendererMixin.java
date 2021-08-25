package paulevs.bnb.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.dimension.Nether;
import net.minecraft.sortme.GameRenderer;
import net.minecraft.util.maths.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.particles.BiomeParticle;
import paulevs.bnb.util.ClientUtil;
import paulevs.bnb.world.biome.NetherBiome;

@Mixin(value = GameRenderer.class, priority = 100)
public class GameRendererMixin {
	@Shadow
	private Minecraft minecraft;
	
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
					minecraft.particleManager.addParticle(new BiomeParticle(minecraft.level, x, y, z, netherBiome.getParticleID(ClientUtil.getRandom())));
				}
			}
		}
	}
}
