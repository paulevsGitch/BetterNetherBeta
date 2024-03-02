package paulevs.bnb.world.structures;

import net.minecraft.block.Block;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.maths.BlockPos;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.MossBlock;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.world.structures.common.CrystalStructure;
import paulevs.bnb.world.structures.common.NetherLake;
import paulevs.bnb.world.structures.common.PillarStructure;
import paulevs.bnb.world.structures.placers.CeilingPlacer;
import paulevs.bnb.world.structures.placers.FloorPlacer;
import paulevs.bnb.world.structures.placers.StructurePlacer;
import paulevs.bnb.world.structures.placers.VolumetricPlacer;
import paulevs.bnb.world.structures.plants.BerriesVineStructure;
import paulevs.bnb.world.structures.scatters.BlockMossScatterStructure;
import paulevs.bnb.world.structures.scatters.DoublePlantScatterStructure;
import paulevs.bnb.world.structures.scatters.MossScatterStructure;
import paulevs.bnb.world.structures.scatters.SimpleScatterStructure;
import paulevs.bnb.world.structures.trees.CommonTreeStructure;

import java.util.Random;
import java.util.function.Function;

public class BNBStructures {
	public static final Structure FLAME_BULBS_STRUCTURE = new SimpleScatterStructure(3, 15, BNBBlocks.FLAME_BULBS);
	public static final Structure CRIMSON_ROOTS_STRUCTURE = new SimpleScatterStructure(3, 5, BNBBlocks.CRIMSON_ROOTS);
	public static final Structure NETHER_DAISY_STRUCTURE = new SimpleScatterStructure(3, 5, BNBBlocks.NETHER_DAISY);
	public static final Structure FIREWEED_STRUCTURE = new DoublePlantScatterStructure(2, 3, BNBBlocks.FIREWEED);
	public static final Structure FLAME_BULBS_TALL_STRUCTURE = new DoublePlantScatterStructure(2, 5, BNBBlocks.FLAME_BULBS_TALL);
	public static final Structure LANTERN_GRASS_STRUCTURE = new DoublePlantScatterStructure(2, 4, BNBBlocks.LANTERN_GRASS);
	public static final Structure CRIMSON_MOSS_STRUCTURE = new MossScatterStructure(
		3, 0.4F,
		(MossBlock) BNBBlocks.CRIMSON_MOSS
	);
	public static final Structure CRIMSON_MOSS_BLOCK_STRUCTURE = new BlockMossScatterStructure(
		3, 0.75F,
		BNBBlocks.CRIMSON_MOSS_BLOCK,
		(MossBlock) BNBBlocks.CRIMSON_MOSS
	);
	
	public static final Structure CRIMSON_VINE_SHORT_STRUCTURE = new BerriesVineStructure(
		BNBBlocks.CRIMSON_VINE,
		BNBBlocks.CRIMSON_VINE_WITH_BERRIES,
		3, 9
	);
	public static final Structure CRIMSON_VINE_LONG_STRUCTURE = new BerriesVineStructure(
		BNBBlocks.CRIMSON_VINE,
		BNBBlocks.CRIMSON_VINE_WITH_BERRIES,
		9, 32
	);
	
	public static final Structure CRIMSON_TREE = new CommonTreeStructure(
		BNBBlocks.CRIMSON_WOOD,
		BNBBlocks.CRIMSON_LEAVES,
		BNBBlocks.CRIMSON_STEM,
		BNBBlocks.CRIMSON_BRANCH,
		BNBBlocks.CRIMSON_WEEPING_VINE,
		7, 11
	);
	public static final Structure CRIMSON_TREE_BUSH = new PillarStructure()
		.addSection(BNBBlocks.CRIMSON_STEM.getDefaultState(), 1, 2)
		.addSection(BNBBlocks.CRIMSON_LEAVES.getDefaultState(), 2, 3);
	
	public static final Structure LAVA_LAKE_STRUCTURE = new NetherLake();
	public static final Structure GLOWSTONE_CRYSTAL_FLOOR_STRUCTURE = new CrystalStructure(
		Block.GLOWSTONE,
		BNBBlocks.GLOWSTONE_SHARDS,
		false, 5, 3
	);
	public static final Structure GLOWSTONE_CRYSTAL_CEILING_STRUCTURE = new CrystalStructure(
		Block.GLOWSTONE,
		BNBBlocks.GLOWSTONE_SHARDS,
		true, 5, 3
	);
	
	private static final Random RANDOM = new Random(0);
	
	public static final StructurePlacer FLAME_BULBS_PLACER = new FloorPlacer(FLAME_BULBS_STRUCTURE, 5);
	public static final StructurePlacer CRIMSON_ROOTS_PLACER = new FloorPlacer(CRIMSON_ROOTS_STRUCTURE, 1).setDensityFunction(getDensity());
	public static final StructurePlacer NETHER_DAISY_PLACER = new FloorPlacer(NETHER_DAISY_STRUCTURE, 1).setDensityFunction(getDensity());
	public static final StructurePlacer FIREWEED_STRUCTURE_PLACER = new FloorPlacer(FIREWEED_STRUCTURE, 1).setDensityFunction(getDensity());
	public static final StructurePlacer FLAME_BULBS_TALL_PLACER = new FloorPlacer(FLAME_BULBS_TALL_STRUCTURE, 3).setDensityFunction(getDensity());
	public static final StructurePlacer LANTERN_GRASS_PLACER = new FloorPlacer(LANTERN_GRASS_STRUCTURE, 1).setDensityFunction(getDensity());
	public static final StructurePlacer CRIMSON_MOSS_PLACER = new VolumetricPlacer(CRIMSON_MOSS_STRUCTURE, 4).setDensityFunction(getDensity());
	public static final StructurePlacer CRIMSON_MOSS_BLOCK_PLACER = new VolumetricPlacer(CRIMSON_MOSS_BLOCK_STRUCTURE, 3).setDensityFunction(getDensity());
	public static final StructurePlacer CRIMSON_MOSS_CEILING_PLACER = new CeilingPlacer(CRIMSON_MOSS_BLOCK_STRUCTURE, 5).setDensityFunction(getDensity());
	public static final StructurePlacer CRIMSON_VINE_SHORT_PLACER = new CeilingPlacer(CRIMSON_VINE_SHORT_STRUCTURE, 10).setDensityFunction(getDensity());
	public static final StructurePlacer CRIMSON_VINE_LONG_PLACER = new CeilingPlacer(CRIMSON_VINE_LONG_STRUCTURE, 5).setDensityFunction(getDensity());
	public static final StructurePlacer CRIMSON_TREE_PLACER = new FloorPlacer(CRIMSON_TREE, 3).setDensityFunction(getDensity());
	public static final StructurePlacer CRIMSON_BUSH_PLACER = new FloorPlacer(CRIMSON_TREE_BUSH, 3).setDensityFunction(getDensity());
	public static final StructurePlacer LAVA_LAKE_PLACER = new FloorPlacer(LAVA_LAKE_STRUCTURE, 1).setCentered(true).setDensityFunction(pos -> RANDOM.nextInt(7) == 0);
	public static final StructurePlacer GLOWSTONE_CRYSTAL_FLOOR_PLACER = new FloorPlacer(GLOWSTONE_CRYSTAL_FLOOR_STRUCTURE, 1).setDensityFunction(pos -> RANDOM.nextInt(15) == 0);
	public static final StructurePlacer GLOWSTONE_CRYSTAL_CEILING_PLACER = new CeilingPlacer(GLOWSTONE_CRYSTAL_CEILING_STRUCTURE, 1).setDensityFunction(pos -> RANDOM.nextInt(15) == 0);
	
	private static Function<BlockPos, Boolean> getDensity() {
		PerlinNoise noise = new PerlinNoise();
		noise.setSeed(RANDOM.nextInt());
		return pos -> noise.get(pos.x * 0.03, pos.y * 0.03, pos.z * 0.03) > 0.5F;
	}
}
