package paulevs.bnb.block;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.bnb.BNB;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BNBBlocks {
	public static final List<BlockBase> BLOCKS_WITH_ITEMS = new ArrayList<>();
	
	public static final BlockBase CRIMSON_NYLIUM = make("crimson_nylium", NetherTerrainBlock::new);
	public static final BlockBase WARPED_NYLIUM = make("warped_nylium", NetherTerrainBlock::new);
	public static final BlockBase POISON_NYLIUM = make("poison_nylium", NetherTerrainBlock::new);
	public static final BlockBase CORRUPTED_NYLIUM = make("corrupted_nylium", NetherTerrainBlock::new);
	public static final BlockBase SOUL_NYLIUM = make("soul_nylium", SoulTerrainBlock::new);
	public static final BlockBase DARK_NYLIUM = make("dark_nylium", NetherTerrainBlock::new);
	
	public static final BlockBase CRIMSON_WOOD = make("crimson_wood", NetherWoodBlock::new);
	public static final BlockBase CRIMSON_STEM = make("crimson_stem", StemBlock::new);
	public static final BlockBase CRIMSON_BRANCH = make("crimson_branch", BranchBlock::new);
	public static final BlockBase CRIMSON_LEAVES = make("crimson_leaves", NetherLeavesBlock::new);
	public static final BlockBase CRIMSON_PLANKS = make("crimson_planks", NetherPlanksBlock::new);
	
	public static final BlockBase WARPED_WOOD = make("warped_wood", NetherWoodBlock::new);
	public static final BlockBase WARPED_LEAVES = make("warped_leaves", NetherLeavesBlock::new);
	
	public static final BlockBase POISON_WOOD = make("poison_wood", NetherWoodBlock::new);
	public static final BlockBase POISON_LEAVES = make("poison_leaves", NetherLeavesBlock::new);
	
	public static final BlockBase PALE_WOOD = make("pale_wood", NetherWoodBlock::new);
	public static final BlockBase PALE_LEAVES = make("pale_leaves", NetherLeavesTransparent::new);
	
	public static final BlockBase EMBER_WOOD = make("ember_wood", EmberWoodBlock::new);
	public static final BlockBase EMBER_LEAVES = make("ember_leaves", NetherLeavesTransparent::new);
	
	public static final BlockBase FLAME_BAMBOO_BLOCK = make("flame_bamboo_block", NetherWoodBlock::new);
	
	public static final BlockBase CRIMSON_LANTERN = make("crimson_lantern", NetherLanternBlock::new);
	public static final BlockBase WARPED_LANTERN = make("warped_lantern", NetherLanternBlock::new);
	public static final BlockBase POISON_LANTERN = make("poison_lantern", NetherLanternBlock::new);
	public static final BlockBase GHOST_PUMPKIN = make("ghost_pumpkin", GhostPumpkinBlock::new);
	
	public static final BlockBase CRIMSON_WEEPING_VINE = make("crimson_weeping_vine", NetherVineBlock::new);
	public static final BlockBase WARPED_WEEPING_VINE = make("warped_weeping_vine", NetherVineBlock::new);
	public static final BlockBase POISON_WEEPING_VINE = make("poison_weeping_vine", NetherVineBlock::new);
	public static final BlockBase PALE_TREE_WEEPING_VINE = make("pale_tree_weeping_vine", NetherVineBlock::new);
	public static final BlockBase EMBER_TREE_WEEPING_VINE = make("ember_tree_weeping_vine", NetherVineBlock::new);
	
	public static final BlockBase FLAME_BULBS = make("flame_bulbs", NetherFloorPlantBlock::new);
	public static final BlockBase CRIMSON_ROOTS = make("crimson_roots", NetherFloorPlantBlock::new);
	public static final BlockBase NETHER_DAISY = make("nether_daisy", NetherFloorPlantBlock::new).setLightEmittance(0.5F);
	public static final BlockBase FIREWEED = make("fireweed", DoubleFloorPlantBlock::new).setLightEmittance(0.5F);
	
	private static BlockBase make(String name, Function<Identifier, BlockBase> constructor) {
		Identifier id = BNB.id(name);
		BlockBase block = constructor.apply(id);
		block.setTranslationKey(id.toString());
		BLOCKS_WITH_ITEMS.add(block);
		return block;
	}
	
	public static void init() {}
}
