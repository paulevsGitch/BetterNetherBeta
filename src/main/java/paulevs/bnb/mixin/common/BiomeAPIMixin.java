package paulevs.bnb.mixin.common;

import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.world.biome.BNBBiomes;

@Mixin(value = BiomeAPI.class, remap = false)
public class BiomeAPIMixin {
	@Inject(method = "addNetherBiomeProvider", at = @At("HEAD"), remap = false)
	private static void bnb_onAddNetherBiomeProvider(Identifier id, BiomeProvider provider, CallbackInfo info) {
		provider.getBiomes().forEach(BNBBiomes::addExternalBiome);
	}
	
	@Inject(method = "addNetherBiome", at = @At("HEAD"), remap = false)
	private static void bnb_onAddNetherBiome(Biome biome, CallbackInfo info) {
		BNBBiomes.addExternalBiome(biome);
	}
}
