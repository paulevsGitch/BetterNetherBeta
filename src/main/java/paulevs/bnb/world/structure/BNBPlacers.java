package paulevs.bnb.world.structure;

import paulevs.bnb.world.structure.placer.CeilingPlacer;
import paulevs.bnb.world.structure.placer.FloorPlacer;
import paulevs.bnb.world.structure.placer.StructurePlacer;
import paulevs.bnb.world.structure.placer.VolumetricPlacer;

public class BNBPlacers {
	public static final StructurePlacer LAVA_LAKE_PLACER = new FloorPlacer(BNBStructures.LAVA_LAKE, 1).setCentered(true).setRandomDensityFunction(0, 7);
	public static final StructurePlacer GLOWSTONE_CRYSTAL_FLOOR_PLACER = new FloorPlacer(BNBStructures.GLOWSTONE_CRYSTAL_FLOOR, 1).setRandomDensityFunction(1, 15);
	public static final StructurePlacer GLOWSTONE_CRYSTAL_CEILING_PLACER = new CeilingPlacer(BNBStructures.GLOWSTONE_CRYSTAL_CEILING, 1).setRandomDensityFunction(2, 15);
	
	public static final StructurePlacer FLAME_BULBS_PLACER = new FloorPlacer(BNBStructures.FLAME_BULBS, 5);
	public static final StructurePlacer FALUN_ROOTS_PLACER = new FloorPlacer(BNBStructures.FALUN_ROOTS, 1).setNoiseDensityFunction(0);
	public static final StructurePlacer NETHER_DAISY_PLACER = new FloorPlacer(BNBStructures.NETHER_DAISY, 1).setNoiseDensityFunction(1);
	public static final StructurePlacer FIREWEED_STRUCTURE_PLACER = new FloorPlacer(BNBStructures.FIREWEED, 1).setNoiseDensityFunction(2);
	public static final StructurePlacer FLAME_BULBS_TALL_PLACER = new FloorPlacer(BNBStructures.FLAME_BULBS_TALL, 3).setNoiseDensityFunction(3);
	public static final StructurePlacer LANTERN_GRASS_PLACER = new FloorPlacer(BNBStructures.LANTERN_GRASS, 1).setNoiseDensityFunction(4);
	public static final StructurePlacer FALUN_MOSS_PLACER = new VolumetricPlacer(BNBStructures.FALUN_MOSS, 4).setNoiseDensityFunction(5);
	public static final StructurePlacer FALUN_MOSS_BLOCK_PLACER = new VolumetricPlacer(BNBStructures.FALUN_MOSS_BLOCK, 3).setNoiseDensityFunction(6);
	public static final StructurePlacer FALUN_MOSS_CEILING_PLACER = new CeilingPlacer(BNBStructures.FALUN_MOSS_BLOCK, 5).setNoiseDensityFunction(7);
	public static final StructurePlacer FALUN_VINE_SHORT_PLACER = new CeilingPlacer(BNBStructures.FALUN_VINE_SHORT, 10).setNoiseDensityFunction(8);
	public static final StructurePlacer FALUN_VINE_LONG_PLACER = new CeilingPlacer(BNBStructures.FALUN_VINE_LONG, 5).setNoiseDensityFunction(9);
	public static final StructurePlacer FALUN_TREE_PLACER = new FloorPlacer(BNBStructures.FALUN_TREE, 3).setNoiseDensityFunction(10);
	public static final StructurePlacer FALUN_BUSH_PLACER = new FloorPlacer(BNBStructures.FALUN_TREE_BUSH, 3).setNoiseDensityFunction(11);
	public static final StructurePlacer FALUN_SPIDER_COCOON = new FloorPlacer(BNBStructures.FALUN_SPIDER_COCOON, 1).setRandomDensityFunction(1, 23);
	public static final StructurePlacer LARGE_FALUN_TREE_PLACER = new FloorPlacer(BNBStructures.LARGE_FALUN_TREE, 1).setRandomDensityFunction(3, 15);
	public static final StructurePlacer FALLEN_FALUN_TREE_PLACER = new FloorPlacer(BNBStructures.FALLEN_FALUN_TREE, 1).setRandomDensityFunction(4, 7);
	
	public static final StructurePlacer PIROZEN_TREE_PLACER = new FloorPlacer(BNBStructures.PIROZEN_TREE, 2).setNoiseDensityFunction(0);
	public static final StructurePlacer PIROZEN_BUSH_PLACER = new FloorPlacer(BNBStructures.PIROZEN_TREE_BUSH, 3).setNoiseDensityFunction(1);
	public static final StructurePlacer PIROZEN_ROOTS_PLACER = new FloorPlacer(BNBStructures.PIROZEN_ROOTS, 1).setNoiseDensityFunction(2);
	public static final StructurePlacer PIROZEN_SPIDER_COCOON = new FloorPlacer(BNBStructures.PIROZEN_SPIDER_COCOON, 1).setRandomDensityFunction(1, 23);
	public static final StructurePlacer LARGE_PIROZEN_TREE_PLACER = new FloorPlacer(BNBStructures.LARGE_PIROZEN_TREE, 1).setRandomDensityFunction(3, 15);
	public static final StructurePlacer FALLEN_PIROZEN_TREE_PLACER = new FloorPlacer(BNBStructures.FALLEN_PIROZEN_TREE, 1).setRandomDensityFunction(4, 7);
	
	public static final StructurePlacer POISON_TREE_PLACER = new FloorPlacer(BNBStructures.POISON_TREE, 2).setNoiseDensityFunction(0);
	public static final StructurePlacer POISON_BUSH_PLACER = new FloorPlacer(BNBStructures.POISON_TREE_BUSH, 3).setNoiseDensityFunction(1);
	public static final StructurePlacer POISON_ROOTS_PLACER = new FloorPlacer(BNBStructures.POISON_ROOTS, 1).setNoiseDensityFunction(2);
	public static final StructurePlacer POISON_SPIDER_COCOON = new FloorPlacer(BNBStructures.POISON_SPIDER_COCOON, 1).setRandomDensityFunction(1, 23);
	public static final StructurePlacer LARGE_POISON_TREE_PLACER = new FloorPlacer(BNBStructures.LARGE_POISON_TREE, 1).setRandomDensityFunction(3, 15);
	public static final StructurePlacer FALLEN_POISON_TREE_PLACER = new FloorPlacer(BNBStructures.FALLEN_POISON_TREE, 1).setRandomDensityFunction(4, 7);
	
	public static final StructurePlacer ORICHALCUM_PLACER = new VolumetricPlacer(BNBStructures.ORICHALCUM, 1);
	public static final StructurePlacer LAVA_STREAM_PLACER = new CeilingPlacer(BNBStructures.LAVA_STREAM, 1).setRandomDensityFunction(5, 15);
}
