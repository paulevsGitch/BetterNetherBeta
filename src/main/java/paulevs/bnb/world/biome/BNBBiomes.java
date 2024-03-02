package paulevs.bnb.world.biome;

import net.minecraft.block.Block;
import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeBuilder;
import net.modificationstation.stationapi.api.worldgen.surface.SurfaceBuilder;
import paulevs.bnb.block.BNBBlocks;

import java.util.ArrayList;
import java.util.List;

public class BNBBiomes {
	private static final List<Biome> BIOMES = new ArrayList<>();
	private static Biome[] biomes;
	
	public static final Biome CRIMSON_FOREST = add(BiomeBuilder
		.start("bnb_crimson_forest")
		.fogColor(0x951922)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.CRIMSON_NYLIUM).replace(Block.NETHERRACK).ground(1).build())
		/*.feature(BNBStructures.LAVA_LAKE_PLACER)
		.feature(BNBStructures.GLOWSTONE_CRYSTAL_FLOOR_PLACER)
		.feature(BNBStructures.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.feature(BNBStructures.CRIMSON_TREE_PLACER)
		.feature(BNBStructures.CRIMSON_BUSH_PLACER)
		.feature(BNBStructures.FIREWEED_STRUCTURE_PLACER)
		.feature(BNBStructures.NETHER_DAISY_PLACER)
		.feature(BNBStructures.CRIMSON_ROOTS_PLACER)
		.feature(BNBStructures.LANTERN_GRASS_PLACER)
		.feature(BNBStructures.FLAME_BULBS_TALL_PLACER)
		.feature(BNBStructures.FLAME_BULBS_PLACER)
		.feature(BNBStructures.CRIMSON_MOSS_CEILING_PLACER)
		.feature(BNBStructures.CRIMSON_VINE_SHORT_PLACER)
		.feature(BNBStructures.CRIMSON_VINE_LONG_PLACER)
		.feature(BNBStructures.CRIMSON_MOSS_BLOCK_PLACER)
		.feature(BNBStructures.CRIMSON_MOSS_PLACER)
		.setAmbientSound(BNBSounds.NETHER_FOREST_AMBIENCE)*/
		.build());
	
		/*make("crimson_forest", SimpleNetherBiome::new)
		.setSurface(BNBBlocks.CRIMSON_NYLIUM.getDefaultState())
		.addStructure(BNBStructures.LAVA_LAKE_PLACER)
		.addStructure(BNBStructures.GLOWSTONE_CRYSTAL_FLOOR_PLACER)
		.addStructure(BNBStructures.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.addStructure(BNBStructures.CRIMSON_TREE_PLACER)
		.addStructure(BNBStructures.CRIMSON_BUSH_PLACER)
		.addStructure(BNBStructures.FIREWEED_STRUCTURE_PLACER)
		.addStructure(BNBStructures.NETHER_DAISY_PLACER)
		.addStructure(BNBStructures.CRIMSON_ROOTS_PLACER)
		.addStructure(BNBStructures.LANTERN_GRASS_PLACER)
		.addStructure(BNBStructures.FLAME_BULBS_TALL_PLACER)
		.addStructure(BNBStructures.FLAME_BULBS_PLACER)
		.addStructure(BNBStructures.CRIMSON_MOSS_CEILING_PLACER)
		.addStructure(BNBStructures.CRIMSON_VINE_SHORT_PLACER)
		.addStructure(BNBStructures.CRIMSON_VINE_LONG_PLACER)
		.addStructure(BNBStructures.CRIMSON_MOSS_BLOCK_PLACER)
		.addStructure(BNBStructures.CRIMSON_MOSS_PLACER)
		.setAmbientSound(BNBSounds.NETHER_FOREST_AMBIENCE);*/
		//.setFogColor(0x951922);
	
	
	public static final Biome WARPED_FOREST = add(BiomeBuilder
		.start("bnb_warped_forest")
		.fogColor(0x119b85)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.WARPED_NYLIUM).replace(Block.NETHERRACK).ground(1).build())
		.build());
	
	/*make("warped_forest", SimpleNetherBiome::new)
		.setSurface(BNBBlocks.WARPED_NYLIUM.getDefaultState())
		.setAmbientSound(BNBSounds.NETHER_FOREST_AMBIENCE);*/
		//.setFogColor(0x119b85);
	
	public static final Biome POISON_FOREST = add(BiomeBuilder
		.start("bnb_poison_forest")
		.fogColor(0x7db33d)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.POISON_NYLIUM).replace(Block.NETHERRACK).ground(1).build())
		.build());
		/*make("poison_forest", SimpleNetherBiome::new)
		.setSurface(BNBBlocks.POISON_NYLIUM.getDefaultState())
		.setAmbientSound(BNBSounds.NETHER_FOREST_AMBIENCE);*/
		//.setFogColor(0x7db33d);
	
	/*private static <B extends NetherBiome> B make(String name, Function<Identifier, B> constructor) {
		Identifier id = BNB.id(name);
		B biome = constructor.apply(id);
		BIOMES.add(biome);
		return biome;
	}*/
	
	private static Biome add(Biome biome) {
		BIOMES.add(biome);
		return biome;
	}
	
	public static void init() {}
	
	public static Biome[] getBiomes() {
		if (biomes == null) {
			biomes = BIOMES.toArray(Biome[]::new);
		}
		return biomes;
	}
}
