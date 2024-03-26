package paulevs.bnb.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.template.block.TemplateStairsBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNB;
import paulevs.bnb.world.structure.BNBStructures;
import paulevs.vbe.block.VBEFullSlabBlock;
import paulevs.vbe.block.VBEHalfSlabBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class BNBBlocks {
	public static final List<Block> BLOCKS_WITH_ITEMS = new ArrayList<>();
	
	public static final NetherTerrainBlock MAROON_NYLIUM = make("maroon_nylium", NetherTerrainBlock::new);
	public static final NetherTerrainBlock WARPED_NYLIUM = make("warped_nylium", NetherTerrainBlock::new);
	public static final NetherTerrainBlock POISON_NYLIUM = make("poison_nylium", NetherTerrainBlock::new);
	public static final NetherTerrainBlock CORRUPTED_NYLIUM = make("corrupted_nylium", NetherTerrainBlock::new);
	public static final NetherTerrainBlock SOUL_NYLIUM = make("soul_nylium", SoulTerrainBlock::new);
	public static final NetherTerrainBlock DARK_NYLIUM = make("dark_nylium", NetherTerrainBlock::new);
	
	public static final Block TREE_LANTERN = make("tree_lantern", NetherLanternBlock::new);
	public static final Block GHOST_PUMPKIN = make("ghost_pumpkin", GhostPumpkinBlock::new);
	
	public static final Block FALUN_LOG = make("falurian_log", NetherLogBlock::new);
	public static final Block FALUN_STEM = make("falurian_stem", StemBlock::new);
	public static final Block FALUN_BRANCH = make("falurian_branch", BranchBlock::new);
	public static final NetherLeavesBlock FALUN_LEAVES = make("falurian_leaves", NetherLeavesBlock::new);
	public static final Block FALUN_SAPLING = makeSapling(
		"falurian_sapling",
		() -> BNBStructures.FALUN_TREE,
		new String[] {
			" # ",
			"###",
			" # "
		},
		() -> BNBStructures.LARGE_FALUN_TREE
	);
	public static final Block FALUN_PLANKS = make("falurian_planks", NetherPlanksBlock::new);
	public static final Block FALUN_STAIRS = make("falurian_stairs", TemplateStairsBlock::new, FALUN_PLANKS);
	public static final VBEHalfSlabBlock FALUN_SLAB_HALF = make("falurian_slab_half", VBEHalfSlabBlock::new, FALUN_PLANKS);
	public static final VBEFullSlabBlock FALUN_SLAB_FULL = makeNI("falurian_slab_full", VBEFullSlabBlock::new, FALUN_PLANKS);
	public static final Block FALUN_FENCE = make("falurian_fence", FenceBlock::new, FALUN_PLANKS);
	
	public static final Block WARPED_LOG = make("warped_log", NetherLogBlock::new);
	public static final Block WARPED_STEM = make("warped_stem", StemBlock::new);
	public static final Block WARPED_BRANCH = make("warped_branch", BranchBlock::new);
	public static final NetherLeavesBlock WARPED_LEAVES = make("warped_leaves", NetherLeavesBlock::new);
	public static final Block WARPED_SAPLING = makeSapling(
		"warped_sapling",
		() -> BNBStructures.WARPED_TREE,
		new String[] {
			" # ",
			"###",
			" # "
		},
		() -> BNBStructures.LARGE_WARPED_TREE
	);
	public static final Block WARPED_PLANKS = make("warped_planks", NetherPlanksBlock::new);
	public static final Block WARPED_STAIRS = make("warped_stairs", TemplateStairsBlock::new, WARPED_PLANKS);
	public static final VBEHalfSlabBlock WARPED_SLAB_HALF = make("warped_slab_half", VBEHalfSlabBlock::new, WARPED_PLANKS);
	public static final VBEFullSlabBlock WARPED_SLAB_FULL = makeNI("warped_slab_full", VBEFullSlabBlock::new, WARPED_PLANKS);
	public static final Block WARPED_FENCE = make("warped_fence", FenceBlock::new, WARPED_PLANKS);
	
	public static final Block POISON_LOG = make("poison_log", NetherLogBlock::new);
	public static final Block POISON_STEM = make("poison_stem", StemBlock::new);
	public static final Block POISON_BRANCH = make("poison_branch", BranchBlock::new);
	public static final NetherLeavesBlock POISON_LEAVES = make("poison_leaves", NetherLeavesBlock::new);
	public static final Block POISON_SAPLING = makeSapling(
		"poison_sapling",
		() -> BNBStructures.POISON_TREE,
		new String[] {
			" # ",
			"###",
			" # "
		},
		() -> BNBStructures.LARGE_POISON_TREE
	);
	public static final Block POISON_PLANKS = make("poison_planks", NetherPlanksBlock::new);
	public static final Block POISON_STAIRS = make("poison_stairs", TemplateStairsBlock::new, POISON_PLANKS);
	public static final VBEHalfSlabBlock POISON_SLAB_HALF = make("poison_slab_half", VBEHalfSlabBlock::new, POISON_PLANKS);
	public static final VBEFullSlabBlock POISON_SLAB_FULL = makeNI("poison_slab_full", VBEFullSlabBlock::new, POISON_PLANKS);
	public static final Block POISON_FENCE = make("poison_fence", FenceBlock::new, POISON_PLANKS);
	
	public static final Block PALE_LOG = make("pale_log", NetherLogBlock::new);
	public static final Block PALE_LEAVES = make("pale_leaves", NetherLeavesTransparent::new);
	
	public static final Block EMBER_LOG = make("ember_log", EmberLogBlock::new);
	public static final Block EMBER_LEAVES = make("ember_leaves", NetherLeavesTransparent::new);
	
	public static final Block FLAME_BAMBOO_BLOCK = make("flame_bamboo_block", NetherLogBlock::new);
	
	public static final Block FALUN_WEEPING_VINE = make("falurian_weeping_vine", NetherVineBlock::new);
	public static final Block WARPED_WEEPING_VINE = make("warped_weeping_vine", NetherVineBlock::new);
	public static final Block POISON_WEEPING_VINE = make("poison_weeping_vine", NetherVineBlock::new);
	public static final Block PALE_TREE_WEEPING_VINE = make("pale_tree_weeping_vine", NetherVineBlock::new);
	public static final Block EMBER_TREE_WEEPING_VINE = make("ember_tree_weeping_vine", NetherVineBlock::new);
	
	public static final Block FLAME_BULBS = make("flame_bulbs", NetherGrassBlock::new);
	public static final Block FLAME_BULBS_TALL = make("flame_bulbs_tall", DoubleGrassPlantBlock::new);
	public static final Block FALUN_ROOTS = make("falurian_roots", NetherRootsBlock::new);
	public static final Block NETHER_DAISY = make("nether_daisy", NetherFloorPlantBlock::new).setLightEmittance(0.5F);
	public static final Block FIREWEED = make("fireweed", DoubleFloorPlantBlock::new).setLightEmittance(0.5F);
	public static final Block LANTERN_GRASS = make("lantern_grass", DoubleFloorPlantBlock::new).setLightEmittance(0.75F);
	
	public static final Block FALUN_MOSS = make("falurian_moss", MossBlock::new);
	public static final Block FALUN_MOSS_BLOCK = make("falurian_moss_block", NetherMossBlock::new);
	
	public static final GrowingNetherVineBlock FALUN_VINE = make("falurian_vine", GrowingNetherVineBlock::new);
	public static final CollectableNetherVineBlock FALUN_VINE_WITH_BERRIES = (CollectableNetherVineBlock) makeNI("falurian_vine_with_berries", CollectableNetherVineBlock::new).setLightEmittance(0.5F);
	
	public static final Block WARPED_ROOTS = make("warped_roots", NetherRootsBlock::new);
	public static final Block POISON_ROOTS = make("poison_roots", NetherRootsBlock::new);
	
	public static final GlowstoneShards GLOWSTONE_SHARDS = (GlowstoneShards) make("glowstone_shards", GlowstoneShards::new).setLightEmittance(1F);
	
	public static final SpiderNetBlock SPIDER_NET = make("spider_net", SpiderNetBlock::new);
	public static final Block FALUN_SPIDER_COCOON = make("falurian_spider_cocoon", SpiderCocoonBlock::new);
	public static final Block WARPED_SPIDER_COCOON = make("warped_spider_cocoon", SpiderCocoonBlock::new);
	public static final Block POISON_SPIDER_COCOON = make("poison_spider_cocoon", SpiderCocoonBlock::new);
	
	public static final Block ORICHALCUM_ORE = make("orichalcum_ore", NetherOre::new);
	public static final Block ORICHALCUM_BLOCK = make("orichalcum_block", NetherMetalBlock::new);
	public static final Block ORICHALCUM_STAIRS = make("orichalcum_stairs", TemplateStairsBlock::new, ORICHALCUM_BLOCK);
	public static final VBEHalfSlabBlock ORICHALCUM_SLAB_HALF = make("orichalcum_slab_half", VBEHalfSlabBlock::new, ORICHALCUM_BLOCK);
	public static final VBEFullSlabBlock ORICHALCUM_SLAB_FULL = makeNI("orichalcum_slab_full", VBEFullSlabBlock::new, ORICHALCUM_BLOCK);
	
	public static final Block NETHERRACK_FURNACE = make("netherrack_furnace", NetherrackFurnaceBlock::new);
	public static final Block SPINNING_WHEEL = make("spinning_wheel", SpinningWheelBlock::new);
	
	public static final Block NETHER_CLOTH = make("nether_cloth", NetherCloth::new);
	public static final Block NETHER_CLOTH_BLACK = make("nether_cloth_black", NetherCloth::new);
	public static final Block NETHER_CLOTH_RED = make("nether_cloth_red", NetherCloth::new);
	public static final Block NETHER_CLOTH_GREEN = make("nether_cloth_green", NetherCloth::new);
	public static final Block NETHER_CLOTH_BROWN = make("nether_cloth_brown", NetherCloth::new);
	public static final Block NETHER_CLOTH_BLUE = make("nether_cloth_blue", NetherCloth::new);
	public static final Block NETHER_CLOTH_PURPLE = make("nether_cloth_purple", NetherCloth::new);
	public static final Block NETHER_CLOTH_CYAN = make("nether_cloth_cyan", NetherCloth::new);
	public static final Block NETHER_CLOTH_LIGHT_GRAY = make("nether_cloth_light_gray", NetherCloth::new);
	public static final Block NETHER_CLOTH_GRAY = make("nether_cloth_gray", NetherCloth::new);
	public static final Block NETHER_CLOTH_PINK = make("nether_cloth_pink", NetherCloth::new);
	public static final Block NETHER_CLOTH_LIME = make("nether_cloth_lime", NetherCloth::new);
	public static final Block NETHER_CLOTH_YELLOW = make("nether_cloth_yellow", NetherCloth::new);
	public static final Block NETHER_CLOTH_LIGHT_BLUE = make("nether_cloth_light_blue", NetherCloth::new);
	public static final Block NETHER_CLOTH_MAGENTA = make("nether_cloth_magenta", NetherCloth::new);
	public static final Block NETHER_CLOTH_ORANGE = make("nether_cloth_orange", NetherCloth::new);
	public static final Block NETHER_CLOTH_WHITE = make("nether_cloth_white", NetherCloth::new);
	
	private static <B extends Block> B make(String name, Function<Identifier, B> constructor) {
		Identifier id = BNB.id(name);
		B block = constructor.apply(id);
		block.setTranslationKey(id.toString());
		BLOCKS_WITH_ITEMS.add(block);
		return block;
	}
	
	private static <B extends Block> B make(String name, BiFunction<Identifier, Block, B> constructor, Block sourceBlock) {
		Identifier id = BNB.id(name);
		B block = constructor.apply(id, sourceBlock);
		block.setTranslationKey(id.toString());
		BLOCKS_WITH_ITEMS.add(block);
		return block;
	}
	
	private static <B extends Block> B make(String name, BiFunction<Identifier, Supplier<Structure>, B> constructor, Supplier<Structure> structure) {
		Identifier id = BNB.id(name);
		B block = constructor.apply(id, structure);
		block.setTranslationKey(id.toString());
		BLOCKS_WITH_ITEMS.add(block);
		return block;
	}
	
	private static TreeSaplingBlock makeSapling(
		String name,
		Supplier<Structure> normalTree,
		String[] bigTreeShape,
		Supplier<Structure> bigTree
	) {
		Identifier id = BNB.id(name);
		TreeSaplingBlock block = new TreeSaplingBlock(id, normalTree, bigTreeShape, bigTree);
		block.setTranslationKey(id.toString());
		BLOCKS_WITH_ITEMS.add(block);
		return block;
	}
	
	private static <B extends Block> B makeNI(String name, Function<Identifier, B> constructor) {
		Identifier id = BNB.id(name);
		B block = constructor.apply(id);
		block.setTranslationKey(id.toString());
		BlockItem.BLOCK_ITEMS.remove(block);
		return block;
	}
	
	private static <B extends Block> B makeNI(String name, BiFunction<Identifier, Block, B> constructor, Block sourceBlock) {
		Identifier id = BNB.id(name);
		B block = constructor.apply(id, sourceBlock);
		block.setTranslationKey(id.toString());
		BlockItem.BLOCK_ITEMS.remove(block);
		return block;
	}
	
	public static void init() {
		FALUN_SLAB_HALF.setFullBlock(FALUN_SLAB_FULL);
		FALUN_SLAB_FULL.setHalfBlock(FALUN_SLAB_HALF);
		WARPED_SLAB_HALF.setFullBlock(WARPED_SLAB_FULL);
		WARPED_SLAB_FULL.setHalfBlock(WARPED_SLAB_HALF);
		POISON_SLAB_HALF.setFullBlock(POISON_SLAB_FULL);
		POISON_SLAB_FULL.setHalfBlock(POISON_SLAB_HALF);
		ORICHALCUM_SLAB_HALF.setFullBlock(ORICHALCUM_SLAB_FULL);
		ORICHALCUM_SLAB_FULL.setHalfBlock(ORICHALCUM_SLAB_HALF);
		
		FALUN_LEAVES.setSapling(FALUN_SAPLING);
		WARPED_LEAVES.setSapling(WARPED_SAPLING);
		POISON_LEAVES.setSapling(POISON_SAPLING);
		
		FALUN_VINE.setGrown(FALUN_VINE_WITH_BERRIES);
		FALUN_VINE_WITH_BERRIES.setBasic(FALUN_VINE);
	}
}
