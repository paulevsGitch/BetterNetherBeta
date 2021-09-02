package paulevs.bnb.listeners;

import com.google.common.collect.Maps;
import net.minecraft.level.biome.Biome;
import net.modificationstation.stationloader.api.common.event.level.biome.BiomeRegister;
import paulevs.bnb.world.NetherBiomeSource;
import paulevs.bnb.world.biome.BasaltGardenBiome;
import paulevs.bnb.world.biome.BasaltShieldBiome;
import paulevs.bnb.world.biome.CorruptedLandsBiome;
import paulevs.bnb.world.biome.CrimsonForestBiome;
import paulevs.bnb.world.biome.DeepDarkBiome;
import paulevs.bnb.world.biome.PoisonForestBiome;
import paulevs.bnb.world.biome.SoulGrasslandBiome;
import paulevs.bnb.world.biome.SoulValleyBiome;
import paulevs.bnb.world.biome.WarpedForestBiome;

import java.util.Map;
import java.util.function.Function;

public class BiomeListener implements BiomeRegister {
	private static final Map<String, Biome> BIOMES = Maps.newHashMap();
	
	@Override
	public void registerBiomes() {
		NetherBiomeSource.addBiome(Biome.NETHER);
		register("Crimson Forest", CrimsonForestBiome::new);
		register("Warped Forest", WarpedForestBiome::new);
		register("Poison Forest", PoisonForestBiome::new);
		register("Soul Valley", SoulValleyBiome::new);
		register("Corrupted Lands", CorruptedLandsBiome::new);
		register("Basalt Shield", BasaltShieldBiome::new);
		register("Basalt Garden", BasaltGardenBiome::new);
		register("Soul Grassland", SoulGrasslandBiome::new);
		register("Deep Dark", DeepDarkBiome::new);
	}
	
	private static void register(String name, Function<String, Biome> init) {
		Biome biome = init.apply(name);
		BIOMES.put(name, biome);
		NetherBiomeSource.addBiome(biome);
	}
	
	public static Biome getBiome(String name) {
		return BIOMES.get(name);
	}
}
