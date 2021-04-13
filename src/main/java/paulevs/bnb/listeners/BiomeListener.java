package paulevs.bnb.listeners;

import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.Maps;

import net.minecraft.level.biome.Biome;
import net.modificationstation.stationloader.api.common.event.level.biome.BiomeRegister;
import paulevs.bnb.world.NetherBiomeSource;
import paulevs.bnb.world.biome.CrimsonForest;
import paulevs.bnb.world.biome.PoisonForest;
import paulevs.bnb.world.biome.SoulValley;
import paulevs.bnb.world.biome.WarpedForest;

public class BiomeListener implements BiomeRegister {
	private static final Map<String, Biome> BIOMES = Maps.newHashMap();
	
	@Override
	public void registerBiomes() {
		NetherBiomeSource.addBiome(Biome.NETHER);
		register("Crimson Forest", CrimsonForest::new);
		register("Warped Forest", WarpedForest::new);
		register("Poison Forest", PoisonForest::new);
		register("Soul Valley", SoulValley::new);
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
