package paulevs.bnb.block;

import net.minecraft.block.Block;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.template.block.TemplateStairsBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNB;
import paulevs.bnb.world.structures.BNBStructures;
import paulevs.vbe.block.VBEFullSlabBlock;
import paulevs.vbe.block.VBEHalfSlabBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class BNBBlocks {
	public static final List<Block> BLOCKS_WITH_ITEMS = new ArrayList<>();
	
	public static final Block CRIMSON_NYLIUM = make("crimson_nylium", NetherTerrainBlock::new);
	public static final Block WARPED_NYLIUM = make("warped_nylium", NetherTerrainBlock::new);
	public static final Block POISON_NYLIUM = make("poison_nylium", NetherTerrainBlock::new);
	public static final Block CORRUPTED_NYLIUM = make("corrupted_nylium", NetherTerrainBlock::new);
	public static final Block SOUL_NYLIUM = make("soul_nylium", SoulTerrainBlock::new);
	public static final Block DARK_NYLIUM = make("dark_nylium", NetherTerrainBlock::new);
	
	public static final Block TREE_LANTERN = make("tree_lantern", NetherLanternBlock::new);
	public static final Block GHOST_PUMPKIN = make("ghost_pumpkin", GhostPumpkinBlock::new);
	
	public static final Block CRIMSON_WOOD = make("crimson_wood", NetherWoodBlock::new);
	public static final Block CRIMSON_STEM = make("crimson_stem", StemBlock::new);
	public static final Block CRIMSON_BRANCH = make("crimson_branch", BranchBlock::new);
	public static final NetherLeavesBlock CRIMSON_LEAVES = make("crimson_leaves", NetherLeavesBlock::new);
	public static final Block CRIMSON_SAPLING = make("crimson_sapling", NetherSaplingBlock::new, () -> BNBStructures.CRIMSON_TREE);
	public static final Block CRIMSON_PLANKS = make("crimson_planks", NetherPlanksBlock::new);
	public static final Block CRIMSON_STAIRS = make("crimson_stairs", TemplateStairsBlock::new, CRIMSON_PLANKS);
	public static final VBEHalfSlabBlock CRIMSON_SLAB_HALF = make("crimson_slab_half", VBEHalfSlabBlock::new, CRIMSON_PLANKS);
	public static final VBEFullSlabBlock CRIMSON_SLAB_FULL = makeNI("crimson_slab_full", VBEFullSlabBlock::new, CRIMSON_PLANKS);
	public static final Block CRIMSON_FENCE = make("crimson_fence", FenceBlock::new, CRIMSON_PLANKS);
	
	public static final Block WARPED_WOOD = make("warped_wood", NetherWoodBlock::new);
	public static final Block WARPED_STEM = make("warped_stem", StemBlock::new);
	public static final Block WARPED_BRANCH = make("warped_branch", BranchBlock::new);
	public static final NetherLeavesBlock WARPED_LEAVES = make("warped_leaves", NetherLeavesBlock::new);
	public static final Block WARPED_SAPLING = make("warped_sapling", NetherSaplingBlock::new, () -> BNBStructures.WARPED_TREE);
	public static final Block WARPED_PLANKS = make("warped_planks", NetherPlanksBlock::new);
	public static final Block WARPED_STAIRS = make("warped_stairs", TemplateStairsBlock::new, WARPED_PLANKS);
	public static final VBEHalfSlabBlock WARPED_SLAB_HALF = make("warped_slab_half", VBEHalfSlabBlock::new, WARPED_PLANKS);
	public static final VBEFullSlabBlock WARPED_SLAB_FULL = makeNI("warped_slab_full", VBEFullSlabBlock::new, WARPED_PLANKS);
	public static final Block WARPED_FENCE = make("warped_fence", FenceBlock::new, WARPED_PLANKS);
	
	public static final Block POISON_WOOD = make("poison_wood", NetherWoodBlock::new);
	public static final Block POISON_STEM = make("poison_stem", StemBlock::new);
	public static final Block POISON_BRANCH = make("poison_branch", BranchBlock::new);
	public static final Block POISON_LEAVES = make("poison_leaves", NetherLeavesBlock::new);
	public static final Block POISON_SAPLING = make("poison_sapling", NetherSaplingBlock::new, () -> BNBStructures.POISON_TREE);
	public static final Block POISON_PLANKS = make("poison_planks", NetherPlanksBlock::new);
	public static final Block POISON_STAIRS = make("poison_stairs", TemplateStairsBlock::new, POISON_PLANKS);
	public static final VBEHalfSlabBlock POISON_SLAB_HALF = make("poison_slab_half", VBEHalfSlabBlock::new, POISON_PLANKS);
	public static final VBEFullSlabBlock POISON_SLAB_FULL = makeNI("poison_slab_full", VBEFullSlabBlock::new, POISON_PLANKS);
	public static final Block POISON_FENCE = make("poison_fence", FenceBlock::new, POISON_PLANKS);
	
	public static final Block PALE_WOOD = make("pale_wood", NetherWoodBlock::new);
	public static final Block PALE_LEAVES = make("pale_leaves", NetherLeavesTransparent::new);
	
	public static final Block EMBER_WOOD = make("ember_wood", EmberWoodBlock::new);
	public static final Block EMBER_LEAVES = make("ember_leaves", NetherLeavesTransparent::new);
	
	public static final Block FLAME_BAMBOO_BLOCK = make("flame_bamboo_block", NetherWoodBlock::new);
	
	public static final Block CRIMSON_WEEPING_VINE = make("crimson_weeping_vine", NetherVineBlock::new);
	public static final Block WARPED_WEEPING_VINE = make("warped_weeping_vine", NetherVineBlock::new);
	public static final Block POISON_WEEPING_VINE = make("poison_weeping_vine", NetherVineBlock::new);
	public static final Block PALE_TREE_WEEPING_VINE = make("pale_tree_weeping_vine", NetherVineBlock::new);
	public static final Block EMBER_TREE_WEEPING_VINE = make("ember_tree_weeping_vine", NetherVineBlock::new);
	
	public static final Block FLAME_BULBS = make("flame_bulbs", NetherGrassBlock::new);
	public static final Block FLAME_BULBS_TALL = make("flame_bulbs_tall", DoubleGrassPlantBlock::new);
	public static final Block CRIMSON_ROOTS = make("crimson_roots", NetherRootsBlock::new);
	public static final Block NETHER_DAISY = make("nether_daisy", NetherFloorPlantBlock::new).setLightEmittance(0.5F);
	public static final Block FIREWEED = make("fireweed", DoubleFloorPlantBlock::new).setLightEmittance(0.5F);
	public static final Block LANTERN_GRASS = make("lantern_grass", DoubleFloorPlantBlock::new).setLightEmittance(0.75F);
	
	public static final Block CRIMSON_MOSS = make("crimson_moss", MossBlock::new);
	public static final Block CRIMSON_MOSS_BLOCK = make("crimson_moss_block", NetherMossBlock::new);
	
	public static final GrowingNetherVineBlock CRIMSON_VINE = make("crimson_vine", GrowingNetherVineBlock::new);
	public static final CollectableNetherVineBlock CRIMSON_VINE_WITH_BERRIES = (CollectableNetherVineBlock) makeNI("crimson_vine_with_berries", CollectableNetherVineBlock::new).setLightEmittance(0.5F);
	
	public static final Block WARPED_ROOTS = make("warped_roots", NetherRootsBlock::new);
	
	public static final GlowstoneShards GLOWSTONE_SHARDS = (GlowstoneShards) make("glowstone_shards", GlowstoneShards::new).setLightEmittance(1F);
	
	private static <B extends Block> B make(String name, Function<Identifier, B> constructor) {
		B block = makeNI(name, constructor);
		BLOCKS_WITH_ITEMS.add(block);
		return block;
	}
	
	private static <B extends Block> B make(String name, BiFunction<Identifier, Block, B> constructor, Block sourceBlock) {
		B block = makeNI(name, constructor, sourceBlock);
		BLOCKS_WITH_ITEMS.add(block);
		return block;
	}
	
	private static <B extends Block> B make(String name, BiFunction<Identifier, Supplier<Structure>, B> constructor, Supplier<Structure> structure) {
		B block = makeNI(name, constructor, structure);
		BLOCKS_WITH_ITEMS.add(block);
		return block;
	}
	
	private static <B extends Block> B makeNI(String name, Function<Identifier, B> constructor) {
		Identifier id = BNB.id(name);
		B block = constructor.apply(id);
		block.setTranslationKey(id.toString());
		return block;
	}
	
	private static <B extends Block> B makeNI(String name, BiFunction<Identifier, Block, B> constructor, Block sourceBlock) {
		Identifier id = BNB.id(name);
		B block = constructor.apply(id, sourceBlock);
		block.setTranslationKey(id.toString());
		return block;
	}
	
	private static <B extends Block> B makeNI(String name, BiFunction<Identifier, Supplier<Structure>, B> constructor, Supplier<Structure> structure) {
		Identifier id = BNB.id(name);
		B block = constructor.apply(id, structure);
		block.setTranslationKey(id.toString());
		return block;
	}
	
	public static void init() {
		CRIMSON_SLAB_HALF.setFullBlock(CRIMSON_SLAB_FULL);
		CRIMSON_SLAB_FULL.setHalfBlock(CRIMSON_SLAB_HALF);
		WARPED_SLAB_HALF.setFullBlock(WARPED_SLAB_FULL);
		WARPED_SLAB_FULL.setHalfBlock(WARPED_SLAB_HALF);
		CRIMSON_LEAVES.setSapling(CRIMSON_SAPLING);
		WARPED_LEAVES.setSapling(WARPED_SAPLING);
		CRIMSON_VINE.setGrown(CRIMSON_VINE_WITH_BERRIES);
		CRIMSON_VINE_WITH_BERRIES.setBasic(CRIMSON_VINE);
	}
}
