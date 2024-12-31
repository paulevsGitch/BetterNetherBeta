package paulevs.bnb.world.structure;

import paulevs.bnb.world.structure.placer.CeilingPlacer;
import paulevs.bnb.world.structure.placer.FloorPlacer;
import paulevs.bnb.world.structure.placer.FloorPlacerLimited;
import paulevs.bnb.world.structure.placer.StructurePlacer;
import paulevs.bnb.world.structure.placer.VolumetricPlacer;

public class BNBPlacers {
	public static final StructurePlacer LAVA_LAKE_PLACER = new FloorPlacer(BNBStructures.LAVA_LAKE, 1).setCentered(true).setRandomDensityFunction(0, 7);
	public static final StructurePlacer GLOWSTONE_CRYSTAL_FLOOR_PLACER = new FloorPlacer(BNBStructures.GLOWSTONE_CRYSTAL_FLOOR, 1).setRandomDensityFunction(1, 15);
	public static final StructurePlacer GLOWSTONE_CRYSTAL_CEILING_PLACER = new CeilingPlacer(BNBStructures.GLOWSTONE_CRYSTAL_CEILING, 1).setRandomDensityFunction(2, 15);
	public static final StructurePlacer GLOWSTONE_CRYSTAL_FLOOR_PLACER_FREQUENT = new FloorPlacer(BNBStructures.GLOWSTONE_CRYSTAL_FLOOR, 1).setRandomDensityFunction(1, 4);
	public static final StructurePlacer GLOWSTONE_CRYSTAL_CEILING_PLACER_FREQUENT = new CeilingPlacer(BNBStructures.GLOWSTONE_CRYSTAL_CEILING, 1).setRandomDensityFunction(2, 4);
	public static final StructurePlacer GLOWSTONE_SHARDS_PLACER = new FloorPlacer(BNBStructures.GLOWSTONE_SHARDS, 1).setNoiseDensityFunction(13);
	
	public static final StructurePlacer FLAME_BULBS_PLACER = new FloorPlacer(BNBStructures.FLAME_BULBS, 5);
	public static final StructurePlacer FALURIAN_ROOTS_PLACER = new FloorPlacer(BNBStructures.FALURIAN_ROOTS, 1).setNoiseDensityFunction(0);
	public static final StructurePlacer NETHER_DAISY_PLACER = new FloorPlacer(BNBStructures.NETHER_DAISY, 1).setNoiseDensityFunction(1);
	public static final StructurePlacer FIREWEED_STRUCTURE_PLACER = new FloorPlacer(BNBStructures.FIREWEED, 1).setNoiseDensityFunction(2);
	public static final StructurePlacer FLAME_BULBS_TALL_PLACER = new FloorPlacer(BNBStructures.FLAME_BULBS_TALL, 3).setNoiseDensityFunction(3);
	public static final StructurePlacer LANTERN_GRASS_PLACER = new FloorPlacer(BNBStructures.LANTERN_GRASS, 1).setNoiseDensityFunction(4);
	public static final StructurePlacer FALURIAN_MOSS_PLACER = new VolumetricPlacer(BNBStructures.FALURIAN_MOSS, 4).setNoiseDensityFunction(5);
	public static final StructurePlacer FALURIAN_MOSS_BLOCK_PLACER = new VolumetricPlacer(BNBStructures.FALURIAN_MOSS_BLOCK, 3).setNoiseDensityFunction(6);
	public static final StructurePlacer FALURIAN_MOSS_CEILING_PLACER = new CeilingPlacer(BNBStructures.FALURIAN_MOSS_BLOCK, 5).setNoiseDensityFunction(7);
	public static final StructurePlacer FALURIAN_VINE_SHORT_PLACER = new CeilingPlacer(BNBStructures.FALURIAN_VINE_SHORT, 10).setNoiseDensityFunction(8);
	public static final StructurePlacer FALURIAN_VINE_LONG_PLACER = new CeilingPlacer(BNBStructures.FALURIAN_VINE_LONG, 5).setNoiseDensityFunction(9);
	public static final StructurePlacer FALURIAN_TREE_PLACER = new FloorPlacer(BNBStructures.FALURIAN_TREE, 3).setNoiseDensityFunction(10);
	public static final StructurePlacer FALURIAN_BUSH_PLACER = new FloorPlacer(BNBStructures.FALURIAN_TREE_BUSH, 3).setNoiseDensityFunction(11);
	public static final StructurePlacer FALURIAN_SPIDER_COCOON = new FloorPlacer(BNBStructures.FALURIAN_SPIDER_COCOON, 1).setRandomNoiseDensityFunction(1, 5);
	public static final StructurePlacer LARGE_FALURIAN_TREE_PLACER = new FloorPlacer(BNBStructures.LARGE_FALURIAN_TREE, 1).setRandomDensityFunction(3, 15);
	public static final StructurePlacer FALLEN_FALURIAN_TREE_PLACER = new FloorPlacer(BNBStructures.FALLEN_FALURIAN_TREE, 1).setRandomDensityFunction(4, 7);
	
	public static final StructurePlacer PIROZEN_TREE_PLACER = new FloorPlacer(BNBStructures.PIROZEN_TREE, 2).setNoiseDensityFunction(0);
	public static final StructurePlacer PIROZEN_BUSH_PLACER = new FloorPlacer(BNBStructures.PIROZEN_TREE_BUSH, 3).setNoiseDensityFunction(1);
	public static final StructurePlacer PIROZEN_ROOTS_PLACER = new FloorPlacer(BNBStructures.PIROZEN_ROOTS, 1).setNoiseDensityFunction(2);
	public static final StructurePlacer PIROZEN_SPIDER_COCOON = new FloorPlacer(BNBStructures.PIROZEN_SPIDER_COCOON, 1).setRandomNoiseDensityFunction(1, 5);
	public static final StructurePlacer LARGE_PIROZEN_TREE_PLACER = new FloorPlacer(BNBStructures.LARGE_PIROZEN_TREE, 1).setRandomDensityFunction(3, 15);
	public static final StructurePlacer FALLEN_PIROZEN_TREE_PLACER = new FloorPlacer(BNBStructures.FALLEN_PIROZEN_TREE, 1).setRandomDensityFunction(4, 7);
	public static final StructurePlacer PIROZEN_VINE_SHORT_PLACER = new CeilingPlacer(BNBStructures.PIROZEN_VINE_SHORT, 10).setNoiseDensityFunction(8);
	public static final StructurePlacer PIROZEN_VINE_LONG_PLACER = new CeilingPlacer(BNBStructures.PIROZEN_VINE_LONG, 5).setNoiseDensityFunction(9);
	public static final StructurePlacer PIROZEN_MOSS_PLACER = new VolumetricPlacer(BNBStructures.PIROZEN_MOSS, 4).setNoiseDensityFunction(5);
	public static final StructurePlacer PIROZEN_MOSS_BLOCK_PLACER = new VolumetricPlacer(BNBStructures.PIROZEN_MOSS_BLOCK, 3).setNoiseDensityFunction(6);
	public static final StructurePlacer PIROZEN_MOSS_CEILING_PLACER = new CeilingPlacer(BNBStructures.PIROZEN_MOSS_BLOCK, 5).setNoiseDensityFunction(7);
	
	public static final StructurePlacer CHLOROPHATE_TREE_PLACER = new FloorPlacer(BNBStructures.CHLOROPHATE_TREE, 2).setNoiseDensityFunction(0);
	public static final StructurePlacer CHLOROPHATE_BUSH_PLACER = new FloorPlacer(BNBStructures.CHLOROPHATE_TREE_BUSH, 3).setNoiseDensityFunction(1);
	public static final StructurePlacer CHLOROPHATE_ROOTS_PLACER = new FloorPlacer(BNBStructures.CHLOROPHATE_ROOTS, 1).setNoiseDensityFunction(2);
	public static final StructurePlacer CHLOROPHATE_SPIDER_COCOON = new FloorPlacer(BNBStructures.CHLOROPHATE_SPIDER_COCOON, 1).setRandomNoiseDensityFunction(1, 5);
	public static final StructurePlacer LARGE_CHLOROPHATE_TREE_PLACER = new FloorPlacer(BNBStructures.LARGE_CHLOROPHATE_TREE, 1).setRandomDensityFunction(3, 15);
	public static final StructurePlacer FALLEN_CHLOROPHATE_TREE_PLACER = new FloorPlacer(BNBStructures.FALLEN_CHLOROPHATE_TREE, 1).setRandomDensityFunction(4, 7);
	
	public static final StructurePlacer ORICHALCUM_PLACER = new VolumetricPlacer(BNBStructures.ORICHALCUM, 1);
	public static final StructurePlacer LAVA_STREAM_PLACER = new CeilingPlacer(BNBStructures.LAVA_STREAM, 1).setRandomDensityFunction(5, 15);
	public static final StructurePlacer OBSIDIAN_BOLDER_PLACER = new FloorPlacer(BNBStructures.OBSIDIAN_BOLDER, 3).setNoiseDensityFunction(15);
	public static final StructurePlacer OBSIDIAN_GRAVEL_BLOB_PLACER = new FloorPlacer(BNBStructures.OBSIDIAN_GRAVEL_BLOB, 7);
	public static final StructurePlacer OBSIDIAN_SHARDS_PLACER = new FloorPlacer(BNBStructures.OBSIDIAN_SHARDS, 4).setNoiseDensityFunction(13);
	public static final StructurePlacer LAVARRACK_BOLDER_PLACER = new FloorPlacerLimited(BNBStructures.LAVARRACK_BOLDER, 3, 0, 90).setNoiseDensityFunction(17);
	
	public static final StructurePlacer JALUMINE_TREE_PLACER = new FloorPlacer(BNBStructures.JALUMINE_TREE, 3).setNoiseDensityFunction(0, 0.75F);
	public static final StructurePlacer JALUMINE_BUSH_PLACER = new FloorPlacer(BNBStructures.JALUMINE_BUSH, 3).setNoiseDensityFunction(1, 0.75F);
	public static final StructurePlacer FERRUMINE_PLANT_PLACER = new FloorPlacer(BNBStructures.FERRUMINE_PLANT, 5);
}
