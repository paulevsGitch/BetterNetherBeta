package paulevs.bnb.world.biome;

import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.bnb.BNB;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.sound.BNBSounds;
import paulevs.bnb.world.structures.BNBStructures;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BNBBiomes {
	private static final List<NetherBiome> BIOMES = new ArrayList<>();
	private static NetherBiome[] biomes;
	
	public static final NetherBiome CRIMSON_FOREST = make("crimson_forest", SimpleNetherBiome::new)
		.setSurface(BNBBlocks.CRIMSON_NYLIUM.getDefaultState())
		.addStructure(BNBStructures.CRIMSON_TREE_PLACER)
		.addStructure(BNBStructures.FIREWEED_STRUCTURE_PLACER)
		.addStructure(BNBStructures.NETHER_DAISY_PLACER)
		.addStructure(BNBStructures.CRIMSON_ROOTS_PLACER)
		.addStructure(BNBStructures.FLAME_BULBS_PLACER)
		.addStructure(BNBStructures.CRIMSON_MOSS_CEILING_PLACER)
		.addStructure(BNBStructures.CRIMSON_VINE_SHORT_PLACER)
		.addStructure(BNBStructures.CRIMSON_VINE_LONG_PLACER)
		.addStructure(BNBStructures.CRIMSON_MOSS_BLOCK_PLACER)
		.addStructure(BNBStructures.CRIMSON_MOSS_PLACER)
		.setAmbientSound(BNBSounds.NETHER_FOREST_AMBIENCE)
		.setFogColor(0x951922);
	
	public static final NetherBiome WARPED_FOREST = make("warped_forest", SimpleNetherBiome::new)
		.setSurface(BNBBlocks.WARPED_NYLIUM.getDefaultState())
		.setAmbientSound(BNBSounds.NETHER_FOREST_AMBIENCE)
		.setFogColor(0x119b85);
	
	public static final NetherBiome POISON_FOREST = make("poison_forest", SimpleNetherBiome::new)
		.setSurface(BNBBlocks.POISON_NYLIUM.getDefaultState())
		.setAmbientSound(BNBSounds.NETHER_FOREST_AMBIENCE)
		.setFogColor(0x7db33d);
	
	private static <B extends NetherBiome> B make(String name, Function<Identifier, B> constructor) {
		Identifier id = BNB.id(name);
		B biome = constructor.apply(id);
		BIOMES.add(biome);
		return biome;
	}
	
	public static void init() {}
	
	public static NetherBiome[] getBiomes() {
		if (biomes == null) {
			biomes = BIOMES.toArray(NetherBiome[]::new);
		}
		return biomes;
	}
}
