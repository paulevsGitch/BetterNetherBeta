package paulevs.bnb.world.structures.placers;

import paulevs.bnb.world.structures.BNBStructures;

public class BNBPlacers {
	public static final StructurePlacer LAVA_LAKE_PLACER = new FloorPlacer(BNBStructures.LAVA_LAKE_STRUCTURE, 1).setCentered(true).setRandomDensityFunction(0, 7);
	public static final StructurePlacer GLOWSTONE_CRYSTAL_FLOOR_PLACER = new FloorPlacer(BNBStructures.GLOWSTONE_CRYSTAL_FLOOR_STRUCTURE, 1).setRandomDensityFunction(1, 15);
	public static final StructurePlacer GLOWSTONE_CRYSTAL_CEILING_PLACER = new CeilingPlacer(BNBStructures.GLOWSTONE_CRYSTAL_CEILING_STRUCTURE, 1).setRandomDensityFunction(2, 15);
	
	public static final StructurePlacer FLAME_BULBS_PLACER = new FloorPlacer(BNBStructures.FLAME_BULBS_STRUCTURE, 5);
	public static final StructurePlacer CRIMSON_ROOTS_PLACER = new FloorPlacer(BNBStructures.CRIMSON_ROOTS_STRUCTURE, 1).setNoiseDensityFunction(0);
	public static final StructurePlacer NETHER_DAISY_PLACER = new FloorPlacer(BNBStructures.NETHER_DAISY_STRUCTURE, 1).setNoiseDensityFunction(1);
	public static final StructurePlacer FIREWEED_STRUCTURE_PLACER = new FloorPlacer(BNBStructures.FIREWEED_STRUCTURE, 1).setNoiseDensityFunction(2);
	public static final StructurePlacer FLAME_BULBS_TALL_PLACER = new FloorPlacer(BNBStructures.FLAME_BULBS_TALL_STRUCTURE, 3).setNoiseDensityFunction(3);
	public static final StructurePlacer LANTERN_GRASS_PLACER = new FloorPlacer(BNBStructures.LANTERN_GRASS_STRUCTURE, 1).setNoiseDensityFunction(4);
	public static final StructurePlacer CRIMSON_MOSS_PLACER = new VolumetricPlacer(BNBStructures.CRIMSON_MOSS_STRUCTURE, 4).setNoiseDensityFunction(5);
	public static final StructurePlacer CRIMSON_MOSS_BLOCK_PLACER = new VolumetricPlacer(BNBStructures.CRIMSON_MOSS_BLOCK_STRUCTURE, 3).setNoiseDensityFunction(6);
	public static final StructurePlacer CRIMSON_MOSS_CEILING_PLACER = new CeilingPlacer(BNBStructures.CRIMSON_MOSS_BLOCK_STRUCTURE, 5).setNoiseDensityFunction(7);
	public static final StructurePlacer CRIMSON_VINE_SHORT_PLACER = new CeilingPlacer(BNBStructures.CRIMSON_VINE_SHORT_STRUCTURE, 10).setNoiseDensityFunction(8);
	public static final StructurePlacer CRIMSON_VINE_LONG_PLACER = new CeilingPlacer(BNBStructures.CRIMSON_VINE_LONG_STRUCTURE, 5).setNoiseDensityFunction(9);
	public static final StructurePlacer CRIMSON_TREE_PLACER = new FloorPlacer(BNBStructures.CRIMSON_TREE, 3).setNoiseDensityFunction(10);
	public static final StructurePlacer CRIMSON_BUSH_PLACER = new FloorPlacer(BNBStructures.CRIMSON_TREE_BUSH, 3).setNoiseDensityFunction(11);
	
	public static final StructurePlacer WARPED_TREE_PLACER = new FloorPlacer(BNBStructures.WARPED_TREE, 2).setNoiseDensityFunction(0);
	public static final StructurePlacer WARPED_BUSH_PLACER = new FloorPlacer(BNBStructures.WARPED_TREE_BUSH, 3).setNoiseDensityFunction(1);
	public static final StructurePlacer WARPED_ROOTS_PLACER = new FloorPlacer(BNBStructures.WARPED_ROOTS_STRUCTURE, 1).setNoiseDensityFunction(2);
	
	public static final StructurePlacer POISON_TREE_PLACER = new FloorPlacer(BNBStructures.POISON_TREE, 2).setNoiseDensityFunction(0);
	public static final StructurePlacer POISON_BUSH_PLACER = new FloorPlacer(BNBStructures.POISON_TREE_BUSH, 3).setNoiseDensityFunction(1);
	public static final StructurePlacer POISON_ROOTS_PLACER = new FloorPlacer(BNBStructures.POISON_ROOTS_STRUCTURE, 1).setNoiseDensityFunction(2);
}
