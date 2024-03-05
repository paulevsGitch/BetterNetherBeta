package paulevs.bnb.world.biome;

import net.minecraft.block.Block;
import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeBuilder;
import net.modificationstation.stationapi.api.worldgen.surface.SurfaceBuilder;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.sound.BNBSounds;
import paulevs.bnb.world.structure.placer.BNBPlacers;

import java.util.ArrayList;
import java.util.List;

public class BNBBiomes {
	private static final List<Biome> BIOMES = new ArrayList<>();
	private static Biome[] biomes;
	
	public static final Biome CRIMSON_FOREST = add(BiomeBuilder
		.start("bnb_crimson_forest")
		.fogColor(0x951922)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.CRIMSON_NYLIUM).replace(Block.NETHERRACK).ground(1).build())
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		.feature(BNBPlacers.LAVA_LAKE_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_FLOOR_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.feature(BNBPlacers.CRIMSON_TREE_PLACER)
		.feature(BNBPlacers.CRIMSON_BUSH_PLACER)
		.feature(BNBPlacers.FIREWEED_STRUCTURE_PLACER)
		.feature(BNBPlacers.NETHER_DAISY_PLACER)
		.feature(BNBPlacers.CRIMSON_ROOTS_PLACER)
		.feature(BNBPlacers.LANTERN_GRASS_PLACER)
		.feature(BNBPlacers.FLAME_BULBS_TALL_PLACER)
		.feature(BNBPlacers.FLAME_BULBS_PLACER)
		.feature(BNBPlacers.CRIMSON_MOSS_CEILING_PLACER)
		.feature(BNBPlacers.CRIMSON_VINE_SHORT_PLACER)
		.feature(BNBPlacers.CRIMSON_VINE_LONG_PLACER)
		.feature(BNBPlacers.CRIMSON_MOSS_BLOCK_PLACER)
		.feature(BNBPlacers.CRIMSON_MOSS_PLACER)
		.build()).bnb_setBiomeAmbience(BNBSounds.NETHER_FOREST_AMBIENCE);
	
	
	public static final Biome WARPED_FOREST = add(BiomeBuilder
		.start("bnb_warped_forest")
		.fogColor(0x119b85)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.WARPED_NYLIUM).replace(Block.NETHERRACK).ground(1).build())
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		.feature(BNBPlacers.LAVA_LAKE_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_FLOOR_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.feature(BNBPlacers.WARPED_TREE_PLACER)
		.feature(BNBPlacers.WARPED_BUSH_PLACER)
		.feature(BNBPlacers.WARPED_ROOTS_PLACER)
		.build()).bnb_setBiomeAmbience(BNBSounds.NETHER_FOREST_AMBIENCE);
	
	public static final Biome POISON_FOREST = add(BiomeBuilder
		.start("bnb_poison_forest")
		.fogColor(0x7db33d)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.POISON_NYLIUM).replace(Block.NETHERRACK).ground(1).build())
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		.feature(BNBPlacers.LAVA_LAKE_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_FLOOR_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.feature(BNBPlacers.POISON_TREE_PLACER)
		.feature(BNBPlacers.POISON_BUSH_PLACER)
		.feature(BNBPlacers.POISON_ROOTS_PLACER)
		.build()).bnb_setBiomeAmbience(BNBSounds.NETHER_FOREST_AMBIENCE);
	
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
