package paulevs.bnb.block;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.template.block.TemplateStairsBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNB;
import paulevs.bnb.Datagen;
import paulevs.bnb.block.crafting.BNBFurnaceBlock;
import paulevs.bnb.block.crafting.SpinningWheelBlock;
import paulevs.bnb.block.falling.NetherrackGravelBlock;
import paulevs.bnb.block.falling.ObsidianGravelBlock;
import paulevs.bnb.block.plant.BNBCollectableVineBlock;
import paulevs.bnb.block.plant.BNBDoubleFloorPlantBlock;
import paulevs.bnb.block.plant.BNBDoubleGrassPlantBlock;
import paulevs.bnb.block.plant.BNBFloorPlantBlock;
import paulevs.bnb.block.plant.BNBRootsBlock;
import paulevs.bnb.block.plant.BNBVineBlock;
import paulevs.bnb.block.plant.FerruminePlantBlock;
import paulevs.bnb.block.plant.MossCoverBlock;
import paulevs.bnb.block.plant.NetherMossBlock;
import paulevs.bnb.block.property.BNBBlockProperties;
import paulevs.bnb.block.slab.SlabUtil;
import paulevs.bnb.block.stone.BNBNetherrack;
import paulevs.bnb.block.stone.BNBObsidianBlock;
import paulevs.bnb.block.stone.FlameQuartzBlock;
import paulevs.bnb.block.stone.LavarrackBlock;
import paulevs.bnb.block.stone.NetherrackBricksBlock;
import paulevs.bnb.block.stone.ObsidianShardsBlock;
import paulevs.bnb.block.stone.ShardsBlock;
import paulevs.bnb.block.stone.SoulSandstoneBlock;
import paulevs.bnb.block.stone.SoulSandstoneTexturedBlock;
import paulevs.bnb.block.terrain.NetherTerrainBlock;
import paulevs.bnb.block.terrain.SoulTerrainBlock;
import paulevs.bnb.block.tree.BNBLeavesBlock;
import paulevs.bnb.block.tree.BNBLeavesTransparent;
import paulevs.bnb.block.tree.BranchBlock;
import paulevs.bnb.block.tree.JalumineFlowerBlock;
import paulevs.bnb.block.tree.NetherLogBlock;
import paulevs.bnb.block.tree.StemBlock;
import paulevs.bnb.block.tree.TreeLanternBlock;
import paulevs.bnb.block.tree.TreeSaplingBlock;
import paulevs.bnb.rendering.BlockTextureUpdate;
import paulevs.bnb.world.structure.BNBStructures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class BNBBlocks {
	static {
		Block.NETHERRACK.setHardness(0.75F);
	}
	
	private static final String[] OLD_TREE_SHAPE = new String[] {
		" # ",
		"###",
		" # "
	};
	
	public static final List<Block> BLOCKS_WITH_ITEMS = new ArrayList<>();
	public static final Set<BlockTextureUpdate> UPDATE_TEXTURE_INTERFACE = new HashSet<>();
	public static final Set<Block> UPDATE_TEXTURE_SINGLE = new HashSet<>();
	
	public static final NetherTerrainBlock NETHERRACK_MYCORRUM = make("netherrack_mycorrum", NetherTerrainBlock::new);
	public static final NetherTerrainBlock SOUL_MYCORRUM = make("soul_mycorrum", SoulTerrainBlock::new);
	public static final Block NETHERRACK_GRAVEL = make("netherrack_gravel", NetherrackGravelBlock::new);
	public static final Block MOSSY_NETHERRACK = make("mossy_netherrack", NetherTerrainBlock::new);
	public static final MossCoverBlock NETHER_MOSS_COVER = makeNI("nether_moss_cover", MossCoverBlock::new);
	public static final Block NETHER_MOSS_BLOCK = make("nether_moss_block", NetherMossBlock::new);
	public static final Block HARDENED_NETHERRACK = make("hardened_netherrack", BNBNetherrack::new);
	
	public static final Block TREE_LANTERN = make("tree_lantern", TreeLanternBlock::new);
	
	public static final Block FALURIAN_LOG = make("falurian_log", NetherLogBlock::new);
	public static final Block FALURIAN_STEM = make("falurian_stem", StemBlock::new);
	public static final Block FALURIAN_BRANCH = make("falurian_branch", BranchBlock::new);
	public static final BNBLeavesBlock FALURIAN_LEAVES = make("falurian_leaves", BNBLeavesBlock::new);
	public static final Block FALURIAN_SAPLING = makeSapling(
		"falurian_sapling",
		() -> BNBStructures.FALURIAN_TREE,
		OLD_TREE_SHAPE,
		() -> BNBStructures.LARGE_FALURIAN_TREE
	);
	public static final Block FALURIAN_PLANKS = make("falurian_planks", BNBPlanksBlock::new);
	public static final Block FALURIAN_STAIRS = make("falurian_stairs", TemplateStairsBlock::new, FALURIAN_PLANKS);
	public static final Block FALURIAN_SLAB_HALF = add(SlabUtil.makeHalfSlab("falurian", FALURIAN_PLANKS));
	public static final Block FALURIAN_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block FALURIAN_FENCE = make("falurian_fence", BNBFenceBlock::new, FALURIAN_PLANKS);
	
	public static final Block PIROZEN_LOG = make("pirozen_log", NetherLogBlock::new);
	public static final Block PIROZEN_STEM = make("pirozen_stem", StemBlock::new);
	public static final Block PIROZEN_BRANCH = make("pirozen_branch", BranchBlock::new);
	public static final BNBLeavesBlock PIROZEN_LEAVES = make("pirozen_leaves", BNBLeavesBlock::new);
	public static final Block PIROZEN_SAPLING = makeSapling(
		"pirozen_sapling",
		() -> BNBStructures.PIROZEN_TREE,
		OLD_TREE_SHAPE,
		() -> BNBStructures.LARGE_PIROZEN_TREE
	);
	public static final Block PIROZEN_PLANKS = make("pirozen_planks", BNBPlanksBlock::new);
	public static final Block PIROZEN_STAIRS = make("pirozen_stairs", TemplateStairsBlock::new, PIROZEN_PLANKS);
	public static final Block PIROZEN_SLAB_HALF = add(SlabUtil.makeHalfSlab("pirozen", PIROZEN_PLANKS));
	public static final Block PIROZEN_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block PIROZEN_FENCE = make("pirozen_fence", BNBFenceBlock::new, PIROZEN_PLANKS);
	
	public static final Block CHLOROPHATE_LOG = make("chlorophate_log", NetherLogBlock::new);
	public static final Block CHLOROPHATE_STEM = make("chlorophate_stem", StemBlock::new);
	public static final Block CHLOROPHATE_BRANCH = make("chlorophate_branch", BranchBlock::new);
	public static final BNBLeavesBlock CHLOROPHATE_LEAVES = make("chlorophate_leaves", BNBLeavesBlock::new);
	public static final Block CHLOROPHATE_SAPLING = makeSapling(
		"chlorophate_sapling",
		() -> BNBStructures.CHLOROPHATE_TREE,
		OLD_TREE_SHAPE,
		() -> BNBStructures.LARGE_CHLOROPHATE_TREE
	);
	public static final Block CHLOROPHATE_PLANKS = make("chlorophate_planks", BNBPlanksBlock::new);
	public static final Block CHLOROPHATE_STAIRS = make("chlorophate_stairs", TemplateStairsBlock::new, CHLOROPHATE_PLANKS);
	public static final Block CHLOROPHATE_SLAB_HALF = add(SlabUtil.makeHalfSlab("chlorophate", CHLOROPHATE_PLANKS));
	public static final Block CHLOROPHATE_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block CHLOROPHATE_FENCE = make("chlorophate_fence", BNBFenceBlock::new, CHLOROPHATE_PLANKS);
	
	public static final BNBLeavesBlock JALUMINE_LEAVES = make("jalumine_leaves", BNBLeavesTransparent::new);
	public static final Block JALUMINE_FLOWER = make("jalumine_flower", JalumineFlowerBlock::new);
	public static final Block JALUMINE_STEM = make("jalumine_stem", StemBlock::new);
	public static final Block JALUMINE_BRANCH = make("jalumine_branch", BranchBlock::new);
	public static final Block JALUMINE_PLANKS = make("jalumine_planks", BNBPlanksBlock::new);
	public static final Block JALUMINE_STAIRS = make("jalumine_stairs", TemplateStairsBlock::new, JALUMINE_PLANKS);
	public static final Block JALUMINE_SLAB_HALF = add(SlabUtil.makeHalfSlab("jalumine", JALUMINE_PLANKS));
	public static final Block JALUMINE_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block JALUMINE_FENCE = make("jalumine_fence", BNBFenceBlock::new, JALUMINE_PLANKS);
	
	public static final Block FALURIAN_WEEPING_VINE = make("falurian_weeping_vine", BNBVineBlock::new);
	public static final Block PIROZEN_WEEPING_VINE = make("pirozen_weeping_vine", BNBVineBlock::new);
	public static final Block CHLOROPHATE_WEEPING_VINE = make("chlorophate_weeping_vine", BNBVineBlock::new);
	
	public static final Block FLAME_BULBS = make("flame_bulbs", BNBFloorPlantBlock::new);
	public static final Block FLAME_BULBS_TALL = make("flame_bulbs_tall", BNBDoubleGrassPlantBlock::new);
	public static final Block FALURIAN_ROOTS = make("falurian_roots", BNBRootsBlock::new);
	public static final Block NETHER_DAISY = make("nether_daisy", BNBFloorPlantBlock::new).setLightEmittance(0.5F);
	public static final Block FIREWEED = make("fireweed", BNBDoubleFloorPlantBlock::new).setLightEmittance(0.5F);
	public static final Block LANTERN_GRASS = make("lantern_grass", BNBDoubleFloorPlantBlock::new).setLightEmittance(0.75F);
	
	public static final FerruminePlantBlock FERRUMINE_PLANT = make("ferrumine_plant", FerruminePlantBlock::new);
	public static final Block NETHER_SPROUTS = make("nether_sprouts", BNBFloorPlantBlock::new);
	
	public static final BNBCollectableVineBlock FALURIAN_VINE = (BNBCollectableVineBlock) make(
		"falurian_vine",
		BNBCollectableVineBlock::new
	).setLuminance(BNBBlocks::getVineLight);
	
	public static final BNBVineBlock PIROZEN_VINE = make("pirozen_vine", BNBVineBlock::new);
	
	public static final Block PIROZEN_ROOTS = make("pirozen_roots", BNBRootsBlock::new);
	public static final Block CHLOROPHATE_ROOTS = make("chlorophate_roots", BNBRootsBlock::new);
	
	public static final ShardsBlock GLOWSTONE_SHARDS = (ShardsBlock) make("glowstone_shards", ShardsBlock::new).setLightEmittance(1F);
	public static final ShardsBlock OBSIDIAN_SHARDS = make("obsidian_shards", ObsidianShardsBlock::new);
	public static final Block OBSIDIAN_GRAVEL = make("obsidian_gravel", ObsidianGravelBlock::new);
	
	public static final Block OBSIDIAN_TILE = make("obsidian_tile", BNBObsidianBlock::new);
	public static final Block OBSIDIAN_TILES = make("obsidian_tiles", BNBObsidianBlock::new);
	public static final Block OBSIDIAN_BRICKS = make("obsidian_bricks", BNBObsidianBlock::new);
	public static final Block OBSIDIAN_TILES_STAIRS = make("obsidian_tiles_stairs", TemplateStairsBlock::new, OBSIDIAN_TILES);
	public static final Block OBSIDIAN_TILES_SLAB_HALF = add(SlabUtil.makeHalfSlab("obsidian_tiles", OBSIDIAN_TILES));
	public static final Block OBSIDIAN_TILES_SLAB_FULL = add(SlabUtil.getFullSlab());
	
	public static final SpiderNetBlock SPIDER_NET = make("spider_net", SpiderNetBlock::new);
	public static final Block FALURIAN_SPIDER_COCOON = make("falurian_spider_cocoon", SpiderCocoonBlock::new);
	public static final Block PIROZEN_SPIDER_COCOON = make("pirozen_spider_cocoon", SpiderCocoonBlock::new);
	public static final Block CHLOROPHATE_SPIDER_COCOON = make("chlorophate_spider_cocoon", SpiderCocoonBlock::new);
	
	public static final Block ORICHALCUM_ORE = make("orichalcum_ore", BNBOreBlock::new);
	public static final Block ORICHALCUM_BLOCK = make("orichalcum_block", BNBMetalBlock::new);
	public static final Block ORICHALCUM_TILES = make("orichalcum_tiles", BNBMetalBlock::new);
	public static final Block ORICHALCUM_TILES_STAIRS = make("orichalcum_tiles_stairs", TemplateStairsBlock::new, ORICHALCUM_TILES);
	public static final Block ORICHALCUM_TILES_SLAB_HALF = add(SlabUtil.makeHalfSlab("orichalcum_tiles", ORICHALCUM_TILES));
	public static final Block ORICHALCUM_TILES_SLAB_FULL = add(SlabUtil.getFullSlab());
	
	public static final Block NETHERRACK_FURNACE = makeFurnace("netherrack_furnace", 800, "gui.bnb.netherrack_furnace");
	public static final Block NETHERRACK_BRICK_FURNACE = makeFurnace("netherrack_brick_furnace", 200, "gui.bnb.netherrack_brick_furnace");
	public static final Block SPINNING_WHEEL = make("spinning_wheel", SpinningWheelBlock::new);
	
	public static final Block NETHER_CLOTH = make("nether_cloth", BNBCloth::new);
	public static final Block NETHER_CLOTH_BLACK = make("nether_cloth_black", BNBCloth::new);
	public static final Block NETHER_CLOTH_RED = make("nether_cloth_red", BNBCloth::new);
	public static final Block NETHER_CLOTH_GREEN = make("nether_cloth_green", BNBCloth::new);
	public static final Block NETHER_CLOTH_BROWN = make("nether_cloth_brown", BNBCloth::new);
	public static final Block NETHER_CLOTH_BLUE = make("nether_cloth_blue", BNBCloth::new);
	public static final Block NETHER_CLOTH_PURPLE = make("nether_cloth_purple", BNBCloth::new);
	public static final Block NETHER_CLOTH_CYAN = make("nether_cloth_cyan", BNBCloth::new);
	public static final Block NETHER_CLOTH_LIGHT_GRAY = make("nether_cloth_light_gray", BNBCloth::new);
	public static final Block NETHER_CLOTH_GRAY = make("nether_cloth_gray", BNBCloth::new);
	public static final Block NETHER_CLOTH_PINK = make("nether_cloth_pink", BNBCloth::new);
	public static final Block NETHER_CLOTH_LIME = make("nether_cloth_lime", BNBCloth::new);
	public static final Block NETHER_CLOTH_YELLOW = make("nether_cloth_yellow", BNBCloth::new);
	public static final Block NETHER_CLOTH_LIGHT_BLUE = make("nether_cloth_light_blue", BNBCloth::new);
	public static final Block NETHER_CLOTH_MAGENTA = make("nether_cloth_magenta", BNBCloth::new);
	public static final Block NETHER_CLOTH_ORANGE = make("nether_cloth_orange", BNBCloth::new);
	public static final Block NETHER_CLOTH_WHITE = make("nether_cloth_white", BNBCloth::new);
	
	public static final Block NETHER_CLOTH_STAIRS = make("nether_cloth_stairs", TemplateStairsBlock::new, NETHER_CLOTH);
	public static final Block NETHER_CLOTH_BLACK_STAIRS = make("nether_cloth_black_stairs", TemplateStairsBlock::new, NETHER_CLOTH_BLACK);
	public static final Block NETHER_CLOTH_RED_STAIRS = make("nether_cloth_red_stairs", TemplateStairsBlock::new, NETHER_CLOTH_RED);
	public static final Block NETHER_CLOTH_GREEN_STAIRS = make("nether_cloth_green_stairs", TemplateStairsBlock::new, NETHER_CLOTH_GREEN);
	public static final Block NETHER_CLOTH_BROWN_STAIRS = make("nether_cloth_brown_stairs", TemplateStairsBlock::new, NETHER_CLOTH_BROWN);
	public static final Block NETHER_CLOTH_BLUE_STAIRS = make("nether_cloth_blue_stairs", TemplateStairsBlock::new, NETHER_CLOTH_BLUE);
	public static final Block NETHER_CLOTH_PURPLE_STAIRS = make("nether_cloth_purple_stairs", TemplateStairsBlock::new, NETHER_CLOTH_PURPLE);
	public static final Block NETHER_CLOTH_CYAN_STAIRS = make("nether_cloth_cyan_stairs", TemplateStairsBlock::new, NETHER_CLOTH_CYAN);
	public static final Block NETHER_CLOTH_LIGHT_GRAY_STAIRS = make("nether_cloth_light_gray_stairs", TemplateStairsBlock::new, NETHER_CLOTH_LIGHT_GRAY);
	public static final Block NETHER_CLOTH_GRAY_STAIRS = make("nether_cloth_gray_stairs", TemplateStairsBlock::new, NETHER_CLOTH_GRAY);
	public static final Block NETHER_CLOTH_PINK_STAIRS = make("nether_cloth_pink_stairs", TemplateStairsBlock::new, NETHER_CLOTH_PINK);
	public static final Block NETHER_CLOTH_LIME_STAIRS = make("nether_cloth_lime_stairs", TemplateStairsBlock::new, NETHER_CLOTH_LIME);
	public static final Block NETHER_CLOTH_YELLOW_STAIRS = make("nether_cloth_yellow_stairs", TemplateStairsBlock::new, NETHER_CLOTH_YELLOW);
	public static final Block NETHER_CLOTH_LIGHT_BLUE_STAIRS = make("nether_cloth_light_blue_stairs", TemplateStairsBlock::new, NETHER_CLOTH_LIGHT_BLUE);
	public static final Block NETHER_CLOTH_MAGENTA_STAIRS = make("nether_cloth_magenta_stairs", TemplateStairsBlock::new, NETHER_CLOTH_MAGENTA);
	public static final Block NETHER_CLOTH_ORANGE_STAIRS = make("nether_cloth_orange_stairs", TemplateStairsBlock::new, NETHER_CLOTH_ORANGE);
	public static final Block NETHER_CLOTH_WHITE_STAIRS = make("nether_cloth_white_stairs", TemplateStairsBlock::new, NETHER_CLOTH_WHITE);
	
	public static final Block NETHER_CLOTH_SLAB_HALF = add(SlabUtil.makeHalfSlab("nether_cloth", NETHER_CLOTH));
	public static final Block NETHER_CLOTH_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block NETHER_CLOTH_BLACK_SLAB_HALF = add(SlabUtil.makeHalfSlab("nether_cloth_black", NETHER_CLOTH_BLACK));
	public static final Block NETHER_CLOTH_BLACK_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block NETHER_CLOTH_RED_SLAB_HALF = add(SlabUtil.makeHalfSlab("nether_cloth_red", NETHER_CLOTH_RED));
	public static final Block NETHER_CLOTH_RED_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block NETHER_CLOTH_GREEN_SLAB_HALF = add(SlabUtil.makeHalfSlab("nether_cloth_green", NETHER_CLOTH_GREEN));
	public static final Block NETHER_CLOTH_GREEN_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block NETHER_CLOTH_BROWN_SLAB_HALF = add(SlabUtil.makeHalfSlab("nether_cloth_brown", NETHER_CLOTH_BROWN));
	public static final Block NETHER_CLOTH_BROWN_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block NETHER_CLOTH_BLUE_SLAB_HALF = add(SlabUtil.makeHalfSlab("nether_cloth_blue", NETHER_CLOTH_BLUE));
	public static final Block NETHER_CLOTH_BLUE_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block NETHER_CLOTH_PURPLE_SLAB_HALF = add(SlabUtil.makeHalfSlab("nether_cloth_purple", NETHER_CLOTH_PURPLE));
	public static final Block NETHER_CLOTH_PURPLE_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block NETHER_CLOTH_CYAN_SLAB_HALF = add(SlabUtil.makeHalfSlab("nether_cloth_cyan", NETHER_CLOTH_CYAN));
	public static final Block NETHER_CLOTH_CYAN_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block NETHER_CLOTH_LIGHT_GRAY_SLAB_HALF = add(SlabUtil.makeHalfSlab("nether_cloth_light_gray", NETHER_CLOTH_LIGHT_GRAY));
	public static final Block NETHER_CLOTH_LIGHT_GRAY_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block NETHER_CLOTH_GRAY_SLAB_HALF = add(SlabUtil.makeHalfSlab("nether_cloth_gray", NETHER_CLOTH_GRAY));
	public static final Block NETHER_CLOTH_GRAY_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block NETHER_CLOTH_PINK_SLAB_HALF = add(SlabUtil.makeHalfSlab("nether_cloth_pink", NETHER_CLOTH_PINK));
	public static final Block NETHER_CLOTH_PINK_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block NETHER_CLOTH_LIME_SLAB_HALF = add(SlabUtil.makeHalfSlab("nether_cloth_lime", NETHER_CLOTH_LIME));
	public static final Block NETHER_CLOTH_LIME_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block NETHER_CLOTH_YELLOW_SLAB_HALF = add(SlabUtil.makeHalfSlab("nether_cloth_yellow", NETHER_CLOTH_YELLOW));
	public static final Block NETHER_CLOTH_YELLOW_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block NETHER_CLOTH_LIGHT_BLUE_SLAB_HALF = add(SlabUtil.makeHalfSlab("nether_cloth_light_blue", NETHER_CLOTH_LIGHT_BLUE));
	public static final Block NETHER_CLOTH_LIGHT_BLUE_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block NETHER_CLOTH_MAGENTA_SLAB_HALF = add(SlabUtil.makeHalfSlab("nether_cloth_magenta", NETHER_CLOTH_MAGENTA));
	public static final Block NETHER_CLOTH_MAGENTA_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block NETHER_CLOTH_ORANGE_SLAB_HALF = add(SlabUtil.makeHalfSlab("nether_cloth_orange", NETHER_CLOTH_ORANGE));
	public static final Block NETHER_CLOTH_ORANGE_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block NETHER_CLOTH_WHITE_SLAB_HALF = add(SlabUtil.makeHalfSlab("nether_cloth_white", NETHER_CLOTH_WHITE));
	public static final Block NETHER_CLOTH_WHITE_SLAB_FULL = add(SlabUtil.getFullSlab());
	
	public static final Block NETHERRACK_BRICKS = make("netherrack_bricks", NetherrackBricksBlock::new);
	public static final Block NETHERRACK_LARGE_TILE = make("netherrack_large_tile", NetherrackBricksBlock::new);
	public static final Block NETHERRACK_TILES = make("netherrack_tiles", NetherrackBricksBlock::new);
	public static final Block NETHERRACK_BRICKS_STAIRS = make("netherrack_bricks_stairs", TemplateStairsBlock::new, NETHERRACK_BRICKS);
	public static final Block NETHERRACK_TILES_STAIRS = make("netherrack_tiles_stairs", TemplateStairsBlock::new, NETHERRACK_TILES);
	public static final Block NETHERRACK_BRICKS_SLAB_HALF = add(SlabUtil.makeHalfSlab("netherrack_bricks", NETHERRACK_BRICKS));
	public static final Block NETHERRACK_BRICKS_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block NETHERRACK_TILES_SLAB_HALF = add(SlabUtil.makeHalfSlab("netherrack_tiles", NETHERRACK_TILES));
	public static final Block NETHERRACK_TILES_SLAB_FULL = add(SlabUtil.getFullSlab());
	
	public static final Block NETHERRACK_ORICHALCUM_TILES = make("netherrack_orichalcum_tiles", NetherrackBricksBlock::new);
	public static final Block LAVARRACK = make("lavarrack", LavarrackBlock::new);
	
	public static final Block SOUL_SANDSTONE = make("soul_sandstone", SoulSandstoneTexturedBlock::new);
	public static final Block SOUL_SANDSTONE_BRICKS = make("soul_sandstone_bricks", SoulSandstoneBlock::new);
	public static final Block SOUL_SANDSTONE_TILES = make("soul_sandstone_tiles", SoulSandstoneBlock::new);
	public static final Block SOUL_SANDSTONE_STAIRS = make("soul_sandstone_stairs", TemplateStairsBlock::new, SOUL_SANDSTONE);
	public static final Block SOUL_SANDSTONE_BRICKS_STAIRS = make("soul_sandstone_bricks_stairs", TemplateStairsBlock::new, SOUL_SANDSTONE_BRICKS);
	public static final Block SOUL_SANDSTONE_TILES_STAIRS = make("soul_sandstone_tiles_stairs", TemplateStairsBlock::new, SOUL_SANDSTONE_TILES);
	public static final Block SOUL_SANDSTONE_SLAB_HALF = add(SlabUtil.makeHalfSlab("soul_sandstone", SOUL_SANDSTONE));
	public static final Block SOUL_SANDSTONE_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block SOUL_SANDSTONE_BRICKS_SLAB_HALF = add(SlabUtil.makeHalfSlab("soul_sandstone_bricks", SOUL_SANDSTONE_BRICKS));
	public static final Block SOUL_SANDSTONE_BRICKS_SLAB_FULL = add(SlabUtil.getFullSlab());
	public static final Block SOUL_SANDSTONE_TILES_SLAB_HALF = add(SlabUtil.makeHalfSlab("soul_sandstone_tiles", SOUL_SANDSTONE_TILES));
	public static final Block SOUL_SANDSTONE_TILES_SLAB_FULL = add(SlabUtil.getFullSlab());
	
	public static final Block FLAME_QUARTZ_YELLOW = add(new FlameQuartzBlock(BNB.id("flame_quartz_yellow"), 0.0F));
	public static final Block FLAME_QUARTZ_ORANGE = add(new FlameQuartzBlock(BNB.id("flame_quartz_orange"), 0.5F));
	public static final Block FLAME_QUARTZ_RED = add(new FlameQuartzBlock(BNB.id("flame_quartz_red"), 1.0F));
	
	private static <B extends Block> B add(B block) {
		Identifier id = BlockRegistry.INSTANCE.getId(block);
		block.setTranslationKey(id);
		if (!block.isAutoItemRegistrationDisabled()) {
			BLOCKS_WITH_ITEMS.add(block);
		}
		if (block instanceof BlockTextureUpdate update) {
			UPDATE_TEXTURE_INTERFACE.add(update);
		}
		return block;
	}
	
	private static <B extends Block> B make(String name, Function<Identifier, B> constructor) {
		Identifier id = BNB.id(name);
		B block = constructor.apply(id);
		block.setTranslationKey(id);
		BLOCKS_WITH_ITEMS.add(block);
		if (block instanceof BlockTextureUpdate update) {
			UPDATE_TEXTURE_INTERFACE.add(update);
		}
		return block;
	}
	
	private static BNBFurnaceBlock makeFurnace(String name, int cookingTime, String guiTranslationKey) {
		Identifier id = BNB.id(name);
		BNBFurnaceBlock block = new BNBFurnaceBlock(id, cookingTime, guiTranslationKey);
		block.setTranslationKey(id);
		BLOCKS_WITH_ITEMS.add(block);
		return block;
	}
	
	private static <B extends Block> B make(String name, BiFunction<Identifier, Block, B> constructor, Block sourceBlock) {
		Identifier id = BNB.id(name);
		B block = constructor.apply(id, sourceBlock);
		block.setTranslationKey(id);
		BLOCKS_WITH_ITEMS.add(block);
		if (block instanceof BlockTextureUpdate update) {
			UPDATE_TEXTURE_INTERFACE.add(update);
		}
		if (block instanceof StairsBlock) {
			UPDATE_TEXTURE_SINGLE.add(sourceBlock);
			if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
				if (block instanceof StairsBlock) {
					Identifier sourceID = BlockRegistry.INSTANCE.getId(sourceBlock);
					assert sourceID != null;
					Datagen.makeStairsRecipe(name, sourceID, id);
				}
			}
		}
		return block;
	}
	
	private static <B extends Block> B make(String name, BiFunction<Identifier, Supplier<Structure>, B> constructor, Supplier<Structure> structure) {
		Identifier id = BNB.id(name);
		B block = constructor.apply(id, structure);
		block.setTranslationKey(id);
		BLOCKS_WITH_ITEMS.add(block);
		if (block instanceof BlockTextureUpdate update) {
			UPDATE_TEXTURE_INTERFACE.add(update);
		}
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
		block.setTranslationKey(id);
		BLOCKS_WITH_ITEMS.add(block);
		return block;
	}
	
	private static <B extends Block> B makeNI(String name, Function<Identifier, B> constructor) {
		Identifier id = BNB.id(name);
		B block = constructor.apply(id);
		block.disableAutoItemRegistration();
		block.setTranslationKey(id);
		Item item = BlockItem.BLOCK_ITEMS.get(block);
		BlockItem.BLOCK_ITEMS.remove(block);
		if (block instanceof BlockTextureUpdate update) {
			UPDATE_TEXTURE_INTERFACE.add(update);
		}
		return block;
	}
	
	private static <B extends Block> B makeNI(String name, BiFunction<Identifier, Block, B> constructor, Block sourceBlock) {
		Identifier id = BNB.id(name);
		B block = constructor.apply(id, sourceBlock);
		block.disableAutoItemRegistration();
		block.setTranslationKey(id);
		BlockItem.BLOCK_ITEMS.remove(block);
		if (block instanceof BlockTextureUpdate update) {
			UPDATE_TEXTURE_INTERFACE.add(update);
		}
		return block;
	}
	
	private static int getVineLight(BlockState state) {
		return state.get(BNBBlockProperties.BERRIES) ? 8 : 0;
	}
	
	public static void init() {
		FALURIAN_LEAVES.setSapling(FALURIAN_SAPLING);
		PIROZEN_LEAVES.setSapling(PIROZEN_SAPLING);
		CHLOROPHATE_LEAVES.setSapling(CHLOROPHATE_SAPLING);
	}
}
