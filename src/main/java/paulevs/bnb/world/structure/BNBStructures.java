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
import paulevs.bnb.world.structure.common.StreamStructure;
import paulevs.bnb.world.structure.plant.BerriesVineStructure;
import paulevs.bnb.world.structure.scatter.BlockMossScatterStructure;
import paulevs.bnb.world.structure.scatter.DoublePlantScatterStructure;
import paulevs.bnb.world.structure.scatter.MossScatterStructure;
import paulevs.bnb.world.structure.scatter.SimpleScatterStructure;
import paulevs.bnb.world.structure.tree.CommonLargeTreeStructure;
import paulevs.bnb.world.structure.tree.CommonTreeStructure;

public class BNBStructures {
	public static final Structure FLAME_BULBS = new SimpleScatterStructure(3, 15, BNBBlocks.FLAME_BULBS);
	public static final Structure FALUN_ROOTS = new SimpleScatterStructure(3, 5, BNBBlocks.FALUN_ROOTS);
	public static final Structure PIROZEN_ROOTS = new SimpleScatterStructure(3, 5, BNBBlocks.PIROZEN_ROOTS);
	public static final Structure POISON_ROOTS = new SimpleScatterStructure(3, 5, BNBBlocks.POISON_ROOTS);
	public static final Structure NETHER_DAISY = new SimpleScatterStructure(3, 5, BNBBlocks.NETHER_DAISY);
	public static final Structure FIREWEED = new DoublePlantScatterStructure(2, 3, BNBBlocks.FIREWEED);
	public static final Structure FLAME_BULBS_TALL = new DoublePlantScatterStructure(2, 5, BNBBlocks.FLAME_BULBS_TALL);
	public static final Structure LANTERN_GRASS = new DoublePlantScatterStructure(2, 4, BNBBlocks.LANTERN_GRASS);
	public static final Structure FALUN_MOSS = new MossScatterStructure(
		3, 0.4F,
		(MossBlock) BNBBlocks.FALUN_MOSS
	);
	public static final Structure FALUN_MOSS_BLOCK = new BlockMossScatterStructure(
		3, 0.75F,
		BNBBlocks.FALUN_MOSS_BLOCK,
		(MossBlock) BNBBlocks.FALUN_MOSS
	);
	
	public static final Structure FALUN_VINE_SHORT = new BerriesVineStructure(
		BNBBlocks.FALUN_VINE,
		BNBBlocks.FALUN_VINE_WITH_BERRIES,
		3, 9
	);
	public static final Structure FALUN_VINE_LONG = new BerriesVineStructure(
		BNBBlocks.FALUN_VINE,
		BNBBlocks.FALUN_VINE_WITH_BERRIES,
		9, 32
	);
	
	public static final Structure FALUN_TREE = new CommonTreeStructure(
		BNBBlocks.FALUN_LOG,
		BNBBlocks.FALUN_LEAVES,
		BNBBlocks.FALUN_STEM,
		BNBBlocks.FALUN_BRANCH,
		BNBBlocks.FALUN_WEEPING_VINE,
		7, 11,
		0.75F, 1.7F,
		1.0F
	);
	public static final Structure PIROZEN_TREE = new CommonTreeStructure(
		BNBBlocks.PIROZEN_LOG,
		BNBBlocks.PIROZEN_LEAVES,
		BNBBlocks.PIROZEN_STEM,
		BNBBlocks.PIROZEN_BRANCH,
		BNBBlocks.PIROZEN_WEEPING_VINE,
		7, 11,
		2.5F, 1.5F,
		1.25F
	);
	public static final Structure POISON_TREE = new CommonTreeStructure(
		BNBBlocks.POISON_LOG,
		BNBBlocks.POISON_LEAVES,
		BNBBlocks.POISON_STEM,
		BNBBlocks.POISON_BRANCH,
		BNBBlocks.POISON_WEEPING_VINE,
		7, 11,
		1.5F, 1.5F,
		0.25F
	);
	
	public static final Structure LARGE_FALUN_TREE = new CommonLargeTreeStructure(
		BNBBlocks.FALUN_LOG,
		BNBBlocks.FALUN_LEAVES,
		BNBBlocks.FALUN_STEM,
		BNBBlocks.FALUN_BRANCH,
		BNBBlocks.FALUN_WEEPING_VINE,
		10, 16,
		0.75F * 2.5F, 1.7F * 2.0F,
		1.0F
	);
	public static final Structure LARGE_PIROZEN_TREE = new CommonLargeTreeStructure(
		BNBBlocks.PIROZEN_LOG,
		BNBBlocks.PIROZEN_LEAVES,
		BNBBlocks.PIROZEN_STEM,
		BNBBlocks.PIROZEN_BRANCH,
		BNBBlocks.PIROZEN_WEEPING_VINE,
		7, 11,
		2.5F * 2.5F, 1.5F * 2.0F,
		1.25F
	);
	public static final Structure LARGE_POISON_TREE = new CommonLargeTreeStructure(
		BNBBlocks.POISON_LOG,
		BNBBlocks.POISON_LEAVES,
		BNBBlocks.POISON_STEM,
		BNBBlocks.POISON_BRANCH,
		BNBBlocks.POISON_WEEPING_VINE,
		7, 11,
		1.5F * 2.5F, 1.5F * 2.0F,
		0.25F
	);
	
	public static final Structure FALUN_TREE_BUSH = new PillarStructure()
		.addSection(BNBBlocks.FALUN_STEM.getDefaultState(), 1, 2)
		.addSection(BNBBlocks.FALUN_LEAVES.getDefaultState(), 2, 3);
	public static final Structure PIROZEN_TREE_BUSH = new PillarStructure()
		.addSection(BNBBlocks.PIROZEN_STEM.getDefaultState(), 1, 1)
		.addSection(BNBBlocks.PIROZEN_LEAVES.getDefaultState(), 1, 2);
	public static final Structure POISON_TREE_BUSH = new PillarStructure()
		.addSection(BNBBlocks.POISON_STEM.getDefaultState(), 2, 4)
		.addSection(BNBBlocks.POISON_LEAVES.getDefaultState(), 1, 1);
	
	public static final Structure FALLEN_FALUN_TREE = new FallenTreeStructure(BNBBlocks.FALUN_LOG, 5, 7);
	public static final Structure FALLEN_PIROZEN_TREE = new FallenTreeStructure(BNBBlocks.PIROZEN_LOG, 5, 7);
	public static final Structure FALLEN_POISON_TREE = new FallenTreeStructure(BNBBlocks.POISON_LOG, 5, 7);
	
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
	
	public static final Structure FALUN_SPIDER_COCOON = new CocoonStructure(BNBBlocks.FALUN_SPIDER_COCOON);
	public static final Structure PIROZEN_SPIDER_COCOON = new CocoonStructure(BNBBlocks.PIROZEN_SPIDER_COCOON);
	public static final Structure POISON_SPIDER_COCOON = new CocoonStructure(BNBBlocks.POISON_SPIDER_COCOON);
	
	public static final Structure ORICHALCUM = new NetherOreStructure(BNBBlocks.ORICHALCUM_ORE, 2);
	
	public static final Structure LAVA_STREAM = new StreamStructure();
}
