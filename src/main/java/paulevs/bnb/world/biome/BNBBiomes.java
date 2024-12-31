package paulevs.bnb.world.biome;

import net.minecraft.block.Block;
import net.minecraft.entity.living.monster.GhastEntity;
import net.minecraft.entity.living.monster.ZombiePigmanEntity;
import net.minecraft.level.biome.Biome;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeBuilder;
import net.modificationstation.stationapi.api.worldgen.surface.SurfaceBuilder;
import net.modificationstation.stationapi.api.worldgen.surface.SurfaceRule;
import net.modificationstation.stationapi.api.worldgen.surface.condition.PositionSurfaceCondition;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.VoronoiNoise;
import paulevs.bnb.sound.BNBSounds;
import paulevs.bnb.world.generator.terrain.TerrainRegion;
import paulevs.bnb.world.structure.BNBPlacers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

@SuppressWarnings("unused")
public class BNBBiomes {
	public static final EnumMap<TerrainRegion, EnumMap<BiomeArea, List<Biome>>> BIOME_BY_TERRAIN = new EnumMap<>(TerrainRegion.class);
	public static final List<Biome> BIOMES = new ArrayList<>();
	
	private static final FractalNoise SHORE_NOISE = new FractalNoise(PerlinNoise::new);
	private static final PositionSurfaceCondition SHORE_COND = new PositionSurfaceCondition(BNBBiomes::shoreHeight);
	private static final FractalNoise GRAPE_NYLIUM_NOISE = new FractalNoise(VoronoiNoise::new);
	private static final SurfaceRule LOW_LAND_GRAVEL = SurfaceBuilder
		.start(BNBBlocks.NETHERRACK_GRAVEL)
		.replace(Block.NETHERRACK)
		.ground(2)
		.range(0, 96)
		.build();
	
	public static final Biome FALURIAN_FOREST = addLand(BiomeBuilder
		.start("bnb_falurian_forest")
		.fogColor(0x951922)
		.grassAndLeavesColor(0x881624)
		.surfaceRule(LOW_LAND_GRAVEL)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.NETHERRACK_MYCORRUM).replace(Block.NETHERRACK).ground(1).build())
		.noDimensionFeatures()
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		.feature(BNBPlacers.LAVA_LAKE_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_FLOOR_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.feature(BNBPlacers.LARGE_FALURIAN_TREE_PLACER)
		.feature(BNBPlacers.FALLEN_FALURIAN_TREE_PLACER)
		.feature(BNBPlacers.FALURIAN_TREE_PLACER)
		.feature(BNBPlacers.FALURIAN_BUSH_PLACER)
		.feature(BNBPlacers.FALURIAN_SPIDER_COCOON)
		.feature(BNBPlacers.FIREWEED_STRUCTURE_PLACER)
		.feature(BNBPlacers.NETHER_DAISY_PLACER)
		.feature(BNBPlacers.FALURIAN_ROOTS_PLACER)
		.feature(BNBPlacers.LANTERN_GRASS_PLACER)
		.feature(BNBPlacers.FLAME_BULBS_TALL_PLACER)
		.feature(BNBPlacers.FLAME_BULBS_PLACER)
		.feature(BNBPlacers.FALURIAN_MOSS_CEILING_PLACER)
		.feature(BNBPlacers.FALURIAN_VINE_SHORT_PLACER)
		.feature(BNBPlacers.FALURIAN_VINE_LONG_PLACER)
		.feature(BNBPlacers.FALURIAN_MOSS_BLOCK_PLACER)
		.feature(BNBPlacers.FALURIAN_MOSS_PLACER)
		.build(), BiomeArea.NETHERRACK_LUSH).bnb_setBiomeAmbience(BNBSounds.NETHER_FOREST_AMBIENCE);
	
	public static final Biome FALURIAN_GRASSLAND = addLand(BiomeBuilder
		.start("bnb_falurian_grassland")
		.fogColor(0x951922)
		.grassAndLeavesColor(0x881624)
		.surfaceRule(LOW_LAND_GRAVEL)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.NETHERRACK_MYCORRUM).replace(Block.NETHERRACK).ground(1).build())
		.noDimensionFeatures()
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		.feature(BNBPlacers.LAVA_LAKE_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_FLOOR_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.feature(BNBPlacers.FALURIAN_BUSH_PLACER)
		.feature(BNBPlacers.FALURIAN_SPIDER_COCOON)
		.feature(BNBPlacers.FIREWEED_STRUCTURE_PLACER)
		.feature(BNBPlacers.NETHER_DAISY_PLACER)
		.feature(BNBPlacers.FALURIAN_ROOTS_PLACER)
		.feature(BNBPlacers.LANTERN_GRASS_PLACER)
		.feature(BNBPlacers.FLAME_BULBS_TALL_PLACER)
		.feature(BNBPlacers.FLAME_BULBS_PLACER)
		.feature(BNBPlacers.FALURIAN_MOSS_CEILING_PLACER)
		.feature(BNBPlacers.FALURIAN_VINE_SHORT_PLACER)
		.feature(BNBPlacers.FALURIAN_VINE_LONG_PLACER)
		.build(), BiomeArea.NETHERRACK_MEDIUM).bnb_setBiomeAmbience(BNBSounds.NETHER_FOREST_AMBIENCE);
	
	public static final Biome PIROZEN_FOREST = addLand(BiomeBuilder
		.start("bnb_pirozen_forest")
		.fogColor(0x119b85)
		.grassAndLeavesColor(0x158e7e)
		.surfaceRule(LOW_LAND_GRAVEL)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.NETHERRACK_MYCORRUM).replace(Block.NETHERRACK).ground(1).build())
		.noDimensionFeatures()
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		.feature(BNBPlacers.LAVA_LAKE_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_FLOOR_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.feature(BNBPlacers.LARGE_PIROZEN_TREE_PLACER)
		.feature(BNBPlacers.FALLEN_PIROZEN_TREE_PLACER)
		.feature(BNBPlacers.PIROZEN_TREE_PLACER)
		.feature(BNBPlacers.PIROZEN_BUSH_PLACER)
		.feature(BNBPlacers.PIROZEN_SPIDER_COCOON)
		.feature(BNBPlacers.PIROZEN_ROOTS_PLACER)
		.feature(BNBPlacers.PIROZEN_VINE_SHORT_PLACER)
		.feature(BNBPlacers.PIROZEN_VINE_LONG_PLACER)
		.feature(BNBPlacers.PIROZEN_MOSS_BLOCK_PLACER)
		.feature(BNBPlacers.PIROZEN_MOSS_PLACER)
		.build(), BiomeArea.NETHERRACK_LUSH).bnb_setBiomeAmbience(BNBSounds.NETHER_FOREST_AMBIENCE);
	
	public static final Biome PIROZEN_GRASSLAND = addLand(BiomeBuilder
		.start("bnb_pirozen_grassland")
		.fogColor(0x119b85)
		.grassAndLeavesColor(0x158e7e)
		.surfaceRule(LOW_LAND_GRAVEL)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.NETHERRACK_MYCORRUM).replace(Block.NETHERRACK).ground(1).build())
		.noDimensionFeatures()
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		.feature(BNBPlacers.LAVA_LAKE_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_FLOOR_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.feature(BNBPlacers.LARGE_PIROZEN_TREE_PLACER)
		.feature(BNBPlacers.PIROZEN_SPIDER_COCOON)
		.feature(BNBPlacers.PIROZEN_ROOTS_PLACER)
		.feature(BNBPlacers.PIROZEN_VINE_SHORT_PLACER)
		.feature(BNBPlacers.PIROZEN_VINE_LONG_PLACER)
		.build(), BiomeArea.NETHERRACK_MEDIUM).bnb_setBiomeAmbience(BNBSounds.NETHER_FOREST_AMBIENCE);
	
	public static final Biome CHLOROPHATE_FOREST = addLand(BiomeBuilder
		.start("bnb_chlorophate_forest")
		.fogColor(0x7db33d)
		.grassAndLeavesColor(0x558b30)
		.surfaceRule(LOW_LAND_GRAVEL)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.NETHERRACK_MYCORRUM).replace(Block.NETHERRACK).ground(1).build())
		.noDimensionFeatures()
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		.feature(BNBPlacers.LAVA_LAKE_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_FLOOR_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.feature(BNBPlacers.LARGE_CHLOROPHATE_TREE_PLACER)
		.feature(BNBPlacers.FALLEN_CHLOROPHATE_TREE_PLACER)
		.feature(BNBPlacers.CHLOROPHATE_TREE_PLACER)
		.feature(BNBPlacers.CHLOROPHATE_BUSH_PLACER)
		.feature(BNBPlacers.CHLOROPHATE_SPIDER_COCOON)
		.feature(BNBPlacers.CHLOROPHATE_ROOTS_PLACER)
		.build(), BiomeArea.NETHERRACK_LUSH).bnb_setBiomeAmbience(BNBSounds.NETHER_FOREST_AMBIENCE);
	
	public static final Biome CHLOROPHATE_GRASSLAND = addLand(BiomeBuilder
		.start("bnb_chlorophate_grassland")
		.fogColor(0x7db33d)
		.grassAndLeavesColor(0x558b30)
		.surfaceRule(LOW_LAND_GRAVEL)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.NETHERRACK_MYCORRUM).replace(Block.NETHERRACK).ground(1).build())
		.noDimensionFeatures()
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		.feature(BNBPlacers.LAVA_LAKE_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_FLOOR_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.feature(BNBPlacers.CHLOROPHATE_BUSH_PLACER)
		.feature(BNBPlacers.CHLOROPHATE_SPIDER_COCOON)
		.feature(BNBPlacers.CHLOROPHATE_ROOTS_PLACER)
		.build(), BiomeArea.NETHERRACK_MEDIUM).bnb_setBiomeAmbience(BNBSounds.NETHER_FOREST_AMBIENCE);
	
	public static final Biome GRAVEL_SHORE = addShore(BiomeBuilder
		.start("bnb_gravel_shore")
		.fogColor(0xab1302)
		.grassAndLeavesColor(0xFF511515)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.NETHERRACK_GRAVEL).replace(Block.NETHERRACK).ground(3).condition(SHORE_COND, 1).build())
		.noDimensionFeatures()
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.build(), BiomeArea.VALUES).bnb_setBiomeAmbience(BNBSounds.LAVA_SEA_AMBIENCE);
	
	public static final Biome OBSIDIAN_SHORE = addShore(BiomeBuilder
		.start("bnb_obsidian_shore")
		.fogColor(0xab1302)
		.grassAndLeavesColor(0xFF511515)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.NETHERRACK_GRAVEL).replace(Block.NETHERRACK).ground(3).condition(SHORE_COND, 1).build())
		.noDimensionFeatures()
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		.feature(BNBPlacers.OBSIDIAN_GRAVEL_BLOB_PLACER)
		.feature(BNBPlacers.OBSIDIAN_BOLDER_PLACER)
		.feature(BNBPlacers.OBSIDIAN_SHARDS_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.build(), BiomeArea.VALUES).bnb_setBiomeAmbience(BNBSounds.LAVA_SEA_AMBIENCE);
	
	public static final Biome LAVA_OCEAN = addOcean(BiomeBuilder
		.start("bnb_lava_ocean")
		.fogColor(0xab1302)
		.grassAndLeavesColor(0xFF511515)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.NETHERRACK_GRAVEL).replace(Block.NETHERRACK).ground(3).condition(SHORE_COND, 1).build())
		.noDimensionFeatures()
		.hostileEntity(GhastEntity.class, 1)
		.hostileEntity(ZombiePigmanEntity.class, 10)
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		.feature(BNBPlacers.LAVARRACK_BOLDER_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER)
		.feature(BNBPlacers.LAVA_STREAM_PLACER)
		.build(), BiomeArea.VALUES).bnb_setBiomeAmbience(BNBSounds.LAVA_SEA_AMBIENCE);
	
	public static final Biome LUSH_SOUL_BIOME = addLand(BiomeBuilder
		.start("bnb_lush_soul_biome")
		.fogColor(Color.CYAN.getRGB())
		.grassAndLeavesColor(Color.CYAN.getRGB())
		.surfaceRule(LOW_LAND_GRAVEL)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.SOUL_MYCORRUM).replace(Block.NETHERRACK).ground(1).build())
		.surfaceRule(SurfaceBuilder.start(Block.SOUL_SAND).replace(Block.NETHERRACK).ground(3).build())
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.SOUL_SANDSTONE).replace(Block.NETHERRACK).ground(10).build())
		.noDimensionFeatures()
		.build(), BiomeArea.SOUL_LUSH);
	
	public static final Biome GLOWSTONE_FOREST = addLand(BiomeBuilder
		.start("bnb_glowstone_forest")
		.fogColor(0x4A306B)
		.grassAndLeavesColor(0xFF372a65)
		.surfaceRule(LOW_LAND_GRAVEL)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.SOUL_MYCORRUM).replace(Block.NETHERRACK).ground(1).condition(
			(level, x, y, z, state) -> GRAPE_NYLIUM_NOISE.get(x * 0.1, z * 0.1) < 0.5F, 5
		).build())
		.surfaceRule(SurfaceBuilder.start(Block.SOUL_SAND).replace(Block.NETHERRACK).ground(3).build())
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.SOUL_SANDSTONE).replace(Block.NETHERRACK).ground(10).build())
		.noDimensionFeatures()
		.feature(BNBPlacers.ORICHALCUM_PLACER)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_FLOOR_PLACER_FREQUENT)
		.feature(BNBPlacers.GLOWSTONE_CRYSTAL_CEILING_PLACER_FREQUENT)
		.feature(BNBPlacers.JALUMINE_TREE_PLACER)
		.feature(BNBPlacers.JALUMINE_BUSH_PLACER)
		.feature(BNBPlacers.GLOWSTONE_SHARDS_PLACER)
		.feature(BNBPlacers.FERRUMINE_PLANT_PLACER)
		.build(), BiomeArea.SOUL_LUSH);
	
	public static final Biome MEDIUM_SOUL_BIOME = addLand(BiomeBuilder
		.start("bnb_medium_soul_biome")
		.fogColor(Color.CYAN.darker().getRGB())
		.grassAndLeavesColor(Color.CYAN.getRGB())
		.surfaceRule(LOW_LAND_GRAVEL)
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.SOUL_MYCORRUM).replace(Block.NETHERRACK).ground(1).build())
		.surfaceRule(SurfaceBuilder.start(Block.SOUL_SAND).replace(Block.NETHERRACK).ground(3).build())
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.SOUL_SANDSTONE).replace(Block.NETHERRACK).ground(10).build())
		.noDimensionFeatures()
		.build(), BiomeArea.SOUL_MEDIUM);
	
	public static final Biome BARREN_SOUL_BIOME = addLand(BiomeBuilder
		.start("bnb_barren_soul_biome")
		.fogColor(Color.CYAN.darker().darker().getRGB())
		.grassAndLeavesColor(Color.CYAN.getRGB())
		.surfaceRule(LOW_LAND_GRAVEL)
		.surfaceRule(SurfaceBuilder.start(Block.SOUL_SAND).replace(Block.NETHERRACK).ground(3).build())
		.surfaceRule(SurfaceBuilder.start(BNBBlocks.SOUL_SANDSTONE).replace(Block.NETHERRACK).ground(10).build())
		.noDimensionFeatures()
		.build(), BiomeArea.SOUL_BARREN);
	
	private static void add(TerrainRegion region, BiomeArea area, Biome biome) {
		BIOME_BY_TERRAIN
			.computeIfAbsent(region, k -> new EnumMap<>(BiomeArea.class))
			.computeIfAbsent(area, k -> new ArrayList<>())
			.add(biome);
	}
	
	private static Biome addLand(Biome biome, BiomeArea... areas) {
		BIOMES.add(biome);
		for (BiomeArea area : areas) {
			add(TerrainRegion.PLAINS, area, biome);
			add(TerrainRegion.HILLS, area, biome);
			add(TerrainRegion.MOUNTAINS, area, biome);
			add(TerrainRegion.BRIDGES, area, biome);
			add(TerrainRegion.SHORE_MOUNTAINS, area, biome);
		}
		return biome;
	}
	
	private static Biome addShore(Biome biome, BiomeArea... areas) {
		BIOMES.add(biome);
		for (BiomeArea area : areas) {
			add(TerrainRegion.SHORE_NORMAL, area, biome);
		}
		return biome;
	}
	
	private static Biome addOcean(Biome biome, BiomeArea... areas) {
		BIOMES.add(biome);
		for (BiomeArea area : areas) {
			add(TerrainRegion.OCEAN_NORMAL, area, biome);
			add(TerrainRegion.OCEAN_MOUNTAINS, area, biome);
		}
		return biome;
	}
	
	private static boolean shoreHeight(BlockPos pos) {
		if (pos.y < 100) return true;
		return pos.y - 100 < SHORE_NOISE.get(pos.x * 0.1, pos.z * 0.1) * 5;
	}
	
	public static void init() {
		SHORE_NOISE.setOctaves(2);
		SHORE_NOISE.setSeed(123);
		GRAPE_NYLIUM_NOISE.setSeed(513);
		Biome.NETHER.setGrassColorProvider((source, x, z) -> 0xFF511515);
	}
}
