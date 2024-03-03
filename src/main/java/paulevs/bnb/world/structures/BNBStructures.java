package paulevs.bnb.world.structures;

import net.minecraft.block.Block;
import net.minecraft.level.structure.Structure;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.MossBlock;
import paulevs.bnb.world.structures.common.CrystalStructure;
import paulevs.bnb.world.structures.common.NetherLake;
import paulevs.bnb.world.structures.common.PillarStructure;
import paulevs.bnb.world.structures.plants.BerriesVineStructure;
import paulevs.bnb.world.structures.scatters.BlockMossScatterStructure;
import paulevs.bnb.world.structures.scatters.DoublePlantScatterStructure;
import paulevs.bnb.world.structures.scatters.MossScatterStructure;
import paulevs.bnb.world.structures.scatters.SimpleScatterStructure;
import paulevs.bnb.world.structures.trees.CommonTreeStructure;

public class BNBStructures {
	public static final Structure FLAME_BULBS_STRUCTURE = new SimpleScatterStructure(3, 15, BNBBlocks.FLAME_BULBS);
	public static final Structure CRIMSON_ROOTS_STRUCTURE = new SimpleScatterStructure(3, 5, BNBBlocks.CRIMSON_ROOTS);
	public static final Structure WARPED_ROOTS_STRUCTURE = new SimpleScatterStructure(3, 5, BNBBlocks.WARPED_ROOTS);
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
		7, 11,
		0.75F, 1.7F
	);
	public static final Structure CRIMSON_TREE_BUSH = new PillarStructure()
		.addSection(BNBBlocks.CRIMSON_STEM.getDefaultState(), 1, 2)
		.addSection(BNBBlocks.CRIMSON_LEAVES.getDefaultState(), 2, 3);
	
	public static final Structure WARPED_TREE = new CommonTreeStructure(
		BNBBlocks.WARPED_WOOD,
		BNBBlocks.WARPED_LEAVES,
		BNBBlocks.WARPED_STEM,
		BNBBlocks.WARPED_BRANCH,
		BNBBlocks.WARPED_WEEPING_VINE,
		7, 11,
		4.0F, 2.5F
	);
	
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
}
