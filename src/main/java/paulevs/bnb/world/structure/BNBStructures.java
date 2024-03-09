package paulevs.bnb.world.structure;

import net.minecraft.block.Block;
import net.minecraft.level.structure.Structure;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.MossBlock;
import paulevs.bnb.world.structure.common.CocoonStructure;
import paulevs.bnb.world.structure.common.CrystalStructure;
import paulevs.bnb.world.structure.common.FallenTreeStructure;
import paulevs.bnb.world.structure.common.NetherLake;
import paulevs.bnb.world.structure.common.NetherOreStructure;
import paulevs.bnb.world.structure.common.PillarStructure;
import paulevs.bnb.world.structure.plant.BerriesVineStructure;
import paulevs.bnb.world.structure.scatter.BlockMossScatterStructure;
import paulevs.bnb.world.structure.scatter.DoublePlantScatterStructure;
import paulevs.bnb.world.structure.scatter.MossScatterStructure;
import paulevs.bnb.world.structure.scatter.SimpleScatterStructure;
import paulevs.bnb.world.structure.tree.CommonLargeTreeStructure;
import paulevs.bnb.world.structure.tree.CommonTreeStructure;

public class BNBStructures {
	public static final Structure FLAME_BULBS = new SimpleScatterStructure(3, 15, BNBBlocks.FLAME_BULBS);
	public static final Structure CRIMSON_ROOTS = new SimpleScatterStructure(3, 5, BNBBlocks.CRIMSON_ROOTS);
	public static final Structure WARPED_ROOTS = new SimpleScatterStructure(3, 5, BNBBlocks.WARPED_ROOTS);
	public static final Structure POISON_ROOTS = new SimpleScatterStructure(3, 5, BNBBlocks.POISON_ROOTS);
	public static final Structure NETHER_DAISY = new SimpleScatterStructure(3, 5, BNBBlocks.NETHER_DAISY);
	public static final Structure FIREWEED = new DoublePlantScatterStructure(2, 3, BNBBlocks.FIREWEED);
	public static final Structure FLAME_BULBS_TALL = new DoublePlantScatterStructure(2, 5, BNBBlocks.FLAME_BULBS_TALL);
	public static final Structure LANTERN_GRASS = new DoublePlantScatterStructure(2, 4, BNBBlocks.LANTERN_GRASS);
	public static final Structure CRIMSON_MOSS = new MossScatterStructure(
		3, 0.4F,
		(MossBlock) BNBBlocks.CRIMSON_MOSS
	);
	public static final Structure CRIMSON_MOSS_BLOCK = new BlockMossScatterStructure(
		3, 0.75F,
		BNBBlocks.CRIMSON_MOSS_BLOCK,
		(MossBlock) BNBBlocks.CRIMSON_MOSS
	);
	
	public static final Structure CRIMSON_VINE_SHORT = new BerriesVineStructure(
		BNBBlocks.CRIMSON_VINE,
		BNBBlocks.CRIMSON_VINE_WITH_BERRIES,
		3, 9
	);
	public static final Structure CRIMSON_VINE_LONG = new BerriesVineStructure(
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
		0.75F, 1.7F,
		1.0F
	);
	public static final Structure WARPED_TREE = new CommonTreeStructure(
		BNBBlocks.WARPED_WOOD,
		BNBBlocks.WARPED_LEAVES,
		BNBBlocks.WARPED_STEM,
		BNBBlocks.WARPED_BRANCH,
		BNBBlocks.WARPED_WEEPING_VINE,
		7, 11,
		2.5F, 1.5F,
		1.25F
	);
	public static final Structure POISON_TREE = new CommonTreeStructure(
		BNBBlocks.POISON_WOOD,
		BNBBlocks.POISON_LEAVES,
		BNBBlocks.POISON_STEM,
		BNBBlocks.POISON_BRANCH,
		BNBBlocks.POISON_WEEPING_VINE,
		7, 11,
		1.5F, 1.5F,
		0.25F
	);
	
	public static final Structure LARGE_CRIMSON_TREE = new CommonLargeTreeStructure(
		BNBBlocks.CRIMSON_WOOD,
		BNBBlocks.CRIMSON_LEAVES,
		BNBBlocks.CRIMSON_STEM,
		BNBBlocks.CRIMSON_BRANCH,
		BNBBlocks.CRIMSON_WEEPING_VINE,
		10, 16,
		0.75F * 2.5F, 1.7F * 2.0F,
		1.0F
	);
	public static final Structure LARGE_WARPED_TREE = new CommonLargeTreeStructure(
		BNBBlocks.WARPED_WOOD,
		BNBBlocks.WARPED_LEAVES,
		BNBBlocks.WARPED_STEM,
		BNBBlocks.WARPED_BRANCH,
		BNBBlocks.WARPED_WEEPING_VINE,
		7, 11,
		2.5F * 2.5F, 1.5F * 2.0F,
		1.25F
	);
	public static final Structure LARGE_POISON_TREE = new CommonLargeTreeStructure(
		BNBBlocks.POISON_WOOD,
		BNBBlocks.POISON_LEAVES,
		BNBBlocks.POISON_STEM,
		BNBBlocks.POISON_BRANCH,
		BNBBlocks.POISON_WEEPING_VINE,
		7, 11,
		1.5F * 2.5F, 1.5F * 2.0F,
		0.25F
	);
	
	public static final Structure CRIMSON_TREE_BUSH = new PillarStructure()
		.addSection(BNBBlocks.CRIMSON_STEM.getDefaultState(), 1, 2)
		.addSection(BNBBlocks.CRIMSON_LEAVES.getDefaultState(), 2, 3);
	public static final Structure WARPED_TREE_BUSH = new PillarStructure()
		.addSection(BNBBlocks.WARPED_STEM.getDefaultState(), 1, 1)
		.addSection(BNBBlocks.WARPED_LEAVES.getDefaultState(), 1, 2);
	public static final Structure POISON_TREE_BUSH = new PillarStructure()
		.addSection(BNBBlocks.POISON_STEM.getDefaultState(), 2, 4)
		.addSection(BNBBlocks.POISON_LEAVES.getDefaultState(), 1, 1);
	
	public static final Structure FALLEN_CRIMSON_TREE = new FallenTreeStructure(BNBBlocks.CRIMSON_WOOD, 5, 7);
	public static final Structure FALLEN_WARPED_TREE = new FallenTreeStructure(BNBBlocks.WARPED_WOOD, 5, 7);
	public static final Structure FALLEN_POISON_TREE = new FallenTreeStructure(BNBBlocks.POISON_WOOD, 5, 7);
	
	public static final Structure LAVA_LAKE = new NetherLake();
	public static final Structure GLOWSTONE_CRYSTAL_FLOOR = new CrystalStructure(
		Block.GLOWSTONE,
		BNBBlocks.GLOWSTONE_SHARDS,
		false, 5, 3
	);
	public static final Structure GLOWSTONE_CRYSTAL_CEILING = new CrystalStructure(
		Block.GLOWSTONE,
		BNBBlocks.GLOWSTONE_SHARDS,
		true, 5, 3
	);
	
	public static final Structure CRIMSON_SPIDER_COCOON = new CocoonStructure(BNBBlocks.CRIMSON_SPIDER_COCOON);
	public static final Structure WARPED_SPIDER_COCOON = new CocoonStructure(BNBBlocks.WARPED_SPIDER_COCOON);
	public static final Structure POISON_SPIDER_COCOON = new CocoonStructure(BNBBlocks.POISON_SPIDER_COCOON);
	
	public static final Structure ORICHALCUM = new NetherOreStructure(BNBBlocks.ORICHALCUM_ORE, 2);
}
