package paulevs.bnb.world.structures;

import net.minecraft.level.structure.Structure;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.MossBlock;
import paulevs.bnb.world.structures.placers.StructurePlacer;
import paulevs.bnb.world.structures.placers.VolumetricPlacer;
import paulevs.bnb.world.structures.scatters.BlockMossScatterStructure;
import paulevs.bnb.world.structures.scatters.DoublePlantScatterStructure;
import paulevs.bnb.world.structures.scatters.MossScatterStructure;
import paulevs.bnb.world.structures.scatters.SimpleScatterStructure;
import paulevs.bnb.world.structures.trees.CommonTreeStructure;

public class BNBStructures {
	public static final Structure FLAME_BULBS_STRUCTURE = new SimpleScatterStructure(3, 15, BNBBlocks.FLAME_BULBS);
	public static final Structure CRIMSON_ROOTS_STRUCTURE = new SimpleScatterStructure(3, 5, BNBBlocks.CRIMSON_ROOTS);
	public static final Structure NETHER_DAISY_STRUCTURE = new SimpleScatterStructure(3, 5, BNBBlocks.NETHER_DAISY);
	public static final Structure FIREWEED_STRUCTURE = new DoublePlantScatterStructure(2, 3, BNBBlocks.FIREWEED);
	public static final Structure CRIMSON_MOSS_STRUCTURE = new MossScatterStructure(
		3, 0.6F,
		(MossBlock) BNBBlocks.CRIMSON_MOSS
	);
	public static final Structure CRIMSON_MOSS_BLOCK_STRUCTURE = new BlockMossScatterStructure(
		3, 0.75F,
		BNBBlocks.CRIMSON_LEAVES, // TODO replace with moss block
		(MossBlock) BNBBlocks.CRIMSON_MOSS
	);
	
	public static final Structure CRIMSON_TREE = new CommonTreeStructure(
		BNBBlocks.CRIMSON_WOOD,
		BNBBlocks.CRIMSON_LEAVES,
		BNBBlocks.CRIMSON_STEM,
		BNBBlocks.CRIMSON_BRANCH,
		BNBBlocks.CRIMSON_WEEPING_VINE,
		7, 11
	);
	
	public static final StructurePlacer FLAME_BULBS_PLACER = new StructurePlacer(FLAME_BULBS_STRUCTURE, 5);
	public static final StructurePlacer CRIMSON_ROOTS_PLACER = new StructurePlacer(CRIMSON_ROOTS_STRUCTURE, 1);
	public static final StructurePlacer NETHER_DAISY_PLACER = new StructurePlacer(NETHER_DAISY_STRUCTURE, 1);
	public static final StructurePlacer FIREWEED_STRUCTURE_PLACER = new StructurePlacer(FIREWEED_STRUCTURE, 1);
	public static final StructurePlacer CRIMSON_MOSS_PLACER = new VolumetricPlacer(CRIMSON_MOSS_STRUCTURE, 3);
	public static final StructurePlacer CRIMSON_MOSS_BLOCK_PLACER = new VolumetricPlacer(CRIMSON_MOSS_BLOCK_STRUCTURE, 3);
	
	public static final StructurePlacer CRIMSON_TREE_PLACER = new StructurePlacer(CRIMSON_TREE, 2);
}
