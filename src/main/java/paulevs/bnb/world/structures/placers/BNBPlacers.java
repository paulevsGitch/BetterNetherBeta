package paulevs.bnb.world.structures.placers;

import net.minecraft.util.maths.BlockPos;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.world.structures.BNBStructures;
import paulevs.bnb.world.structures.placers.CeilingPlacer;
import paulevs.bnb.world.structures.placers.FloorPlacer;
import paulevs.bnb.world.structures.placers.StructurePlacer;
import paulevs.bnb.world.structures.placers.VolumetricPlacer;

import java.util.Random;
import java.util.function.Function;

public class BNBPlacers {
	private static final Random RANDOM = new Random(0);
	
	public static final StructurePlacer LAVA_LAKE_PLACER = new FloorPlacer(BNBStructures.LAVA_LAKE_STRUCTURE, 1).setCentered(true).setDensityFunction(pos -> RANDOM.nextInt(7) == 0);
	public static final StructurePlacer GLOWSTONE_CRYSTAL_FLOOR_PLACER = new FloorPlacer(BNBStructures.GLOWSTONE_CRYSTAL_FLOOR_STRUCTURE, 1).setDensityFunction(pos -> RANDOM.nextInt(15) == 0);
	public static final StructurePlacer GLOWSTONE_CRYSTAL_CEILING_PLACER = new CeilingPlacer(BNBStructures.GLOWSTONE_CRYSTAL_CEILING_STRUCTURE, 1).setDensityFunction(pos -> RANDOM.nextInt(15) == 0);
	
	public static final StructurePlacer FLAME_BULBS_PLACER = new FloorPlacer(BNBStructures.FLAME_BULBS_STRUCTURE, 5);
	public static final StructurePlacer CRIMSON_ROOTS_PLACER = new FloorPlacer(BNBStructures.CRIMSON_ROOTS_STRUCTURE, 1).setDensityFunction(getDensity());
	public static final StructurePlacer NETHER_DAISY_PLACER = new FloorPlacer(BNBStructures.NETHER_DAISY_STRUCTURE, 1).setDensityFunction(getDensity());
	public static final StructurePlacer FIREWEED_STRUCTURE_PLACER = new FloorPlacer(BNBStructures.FIREWEED_STRUCTURE, 1).setDensityFunction(getDensity());
	public static final StructurePlacer FLAME_BULBS_TALL_PLACER = new FloorPlacer(BNBStructures.FLAME_BULBS_TALL_STRUCTURE, 3).setDensityFunction(getDensity());
	public static final StructurePlacer LANTERN_GRASS_PLACER = new FloorPlacer(BNBStructures.LANTERN_GRASS_STRUCTURE, 1).setDensityFunction(getDensity());
	public static final StructurePlacer CRIMSON_MOSS_PLACER = new VolumetricPlacer(BNBStructures.CRIMSON_MOSS_STRUCTURE, 4).setDensityFunction(getDensity());
	public static final StructurePlacer CRIMSON_MOSS_BLOCK_PLACER = new VolumetricPlacer(BNBStructures.CRIMSON_MOSS_BLOCK_STRUCTURE, 3).setDensityFunction(getDensity());
	public static final StructurePlacer CRIMSON_MOSS_CEILING_PLACER = new CeilingPlacer(BNBStructures.CRIMSON_MOSS_BLOCK_STRUCTURE, 5).setDensityFunction(getDensity());
	public static final StructurePlacer CRIMSON_VINE_SHORT_PLACER = new CeilingPlacer(BNBStructures.CRIMSON_VINE_SHORT_STRUCTURE, 10).setDensityFunction(getDensity());
	public static final StructurePlacer CRIMSON_VINE_LONG_PLACER = new CeilingPlacer(BNBStructures.CRIMSON_VINE_LONG_STRUCTURE, 5).setDensityFunction(getDensity());
	public static final StructurePlacer CRIMSON_TREE_PLACER = new FloorPlacer(BNBStructures.CRIMSON_TREE, 3).setDensityFunction(getDensity());
	public static final StructurePlacer CRIMSON_BUSH_PLACER = new FloorPlacer(BNBStructures.CRIMSON_TREE_BUSH, 3).setDensityFunction(getDensity());
	
	public static final StructurePlacer WARPED_TREE_PLACER = new FloorPlacer(BNBStructures.WARPED_TREE, 2).setDensityFunction(getDensity());
	public static final StructurePlacer WARPED_ROOTS_PLACER = new FloorPlacer(BNBStructures.WARPED_ROOTS_STRUCTURE, 1).setDensityFunction(getDensity());
	
	private static Function<BlockPos, Boolean> getDensity() {
		PerlinNoise noise = new PerlinNoise();
		noise.setSeed(RANDOM.nextInt());
		return pos -> noise.get(pos.x * 0.03, pos.y * 0.03, pos.z * 0.03) > 0.5F;
	}
}
