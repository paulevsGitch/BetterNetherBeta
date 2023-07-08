package paulevs.bnb.world.biome;

import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.bnb.BNB;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.sound.BNBSounds;
import paulevs.bnb.world.structures.BNBStructures;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BNBBiomes {
	public static final Map<Identifier, NetherBiome> BIOMES = new HashMap<>();
	
	public static final NetherBiome CRIMSON_FOREST = make("crimson_forest", SimpleNetherBiome::new)
		.setSurface(BNBBlocks.CRIMSON_NYLIUM.getDefaultState())
		.addStructure(BNBStructures.CRIMSON_TREE_PLACER)
		.addStructure(BNBStructures.FIREWEED_STRUCTURE_PLACER)
		.addStructure(BNBStructures.NETHER_DAISY_PLACER)
		.addStructure(BNBStructures.CRIMSON_ROOTS_PLACER)
		.addStructure(BNBStructures.FLAME_BULBS_PLACER)
		.setAmbientSound(BNBSounds.NETHER_FOREST_AMBIENCE);
	
	private static <B extends NetherBiome> B make(String name, Function<Identifier, B> constructor) {
		Identifier id = BNB.id(name);
		B biome = constructor.apply(id);
		BIOMES.put(id, biome);
		return biome;
	}
	
	public static void init() {}
}
