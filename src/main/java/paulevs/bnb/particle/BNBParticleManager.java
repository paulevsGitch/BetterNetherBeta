package paulevs.bnb.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.block.BlockState;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class BNBParticleManager {
	public static void tick(Minecraft minecraft) {
		if ((minecraft.level.getLevelTime() & 1) > 0) return;
		Random random = minecraft.level.random;
		int x = (int) (minecraft.viewEntity.x) + random.nextInt(31) - 15;
		int y = (int) (minecraft.viewEntity.y) + random.nextInt(31) - 15;
		int z = (int) (minecraft.viewEntity.z) + random.nextInt(31) - 15;
		BlockState state = minecraft.level.getBlockState(x, y, z);
		if (!state.isAir() && state.getMaterial().blocksMovement()) return;
		Biome biome = minecraft.level.getBiomeSource().getBiome(x, z);
		int index = biome.bnb_getParticleTexture(random);
		if (index == -1) return;
		minecraft.particleManager.addParticle(new BiomeParticleEntity(
			minecraft.level,
			x, y, z,
			index,
			biome.bnb_getParticleEmissive()
		));
	}
}
