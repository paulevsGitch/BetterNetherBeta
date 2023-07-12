package paulevs.bnb.block;

import net.minecraft.block.BaseBlock;
import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.bnb.BNB;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BNBBlocks {
	public static final List<BaseBlock> BLOCKS_WITH_ITEMS = new ArrayList<>();
	
	public static final BaseBlock CRIMSON_NYLIUM = make("crimson_nylium", NetherTerrainBlock::new);
	public static final BaseBlock WARPED_NYLIUM = make("warped_nylium", NetherTerrainBlock::new);
	public static final BaseBlock POISON_NYLIUM = make("poison_nylium", NetherTerrainBlock::new);
	public static final BaseBlock CORRUPTED_NYLIUM = make("corrupted_nylium", NetherTerrainBlock::new);
	public static final BaseBlock SOUL_NYLIUM = make("soul_nylium", SoulTerrainBlock::new);
	public static final BaseBlock DARK_NYLIUM = make("dark_nylium", NetherTerrainBlock::new);
	
	public static final BaseBlock CRIMSON_WOOD = make("crimson_wood", NetherWoodBlock::new);
	public static final BaseBlock CRIMSON_STEM = make("crimson_stem", StemBlock::new);
	public static final BaseBlock CRIMSON_BRANCH = make("crimson_branch", BranchBlock::new);
	public static final BaseBlock CRIMSON_LEAVES = make("crimson_leaves", NetherLeavesBlock::new);
	public static final BaseBlock CRIMSON_PLANKS = make("crimson_planks", NetherPlanksBlock::new);
	
	public static final BaseBlock WARPED_WOOD = make("warped_wood", NetherWoodBlock::new);
	public static final BaseBlock WARPED_LEAVES = make("warped_leaves", NetherLeavesBlock::new);
	
	public static final BaseBlock POISON_WOOD = make("poison_wood", NetherWoodBlock::new);
	public static final BaseBlock POISON_LEAVES = make("poison_leaves", NetherLeavesBlock::new);
	
	public static final BaseBlock PALE_WOOD = make("pale_wood", NetherWoodBlock::new);
	public static final BaseBlock PALE_LEAVES = make("pale_leaves", NetherLeavesTransparent::new);
	
	public static final BaseBlock EMBER_WOOD = make("ember_wood", EmberWoodBlock::new);
	public static final BaseBlock EMBER_LEAVES = make("ember_leaves", NetherLeavesTransparent::new);
	
	public static final BaseBlock FLAME_BAMBOO_BLOCK = make("flame_bamboo_block", NetherWoodBlock::new);
	
	public static final BaseBlock CRIMSON_LANTERN = make("crimson_lantern", NetherLanternBlock::new);
	public static final BaseBlock WARPED_LANTERN = make("warped_lantern", NetherLanternBlock::new);
	public static final BaseBlock POISON_LANTERN = make("poison_lantern", NetherLanternBlock::new);
	public static final BaseBlock GHOST_PUMPKIN = make("ghost_pumpkin", GhostPumpkinBlock::new);
	
	public static final BaseBlock CRIMSON_WEEPING_VINE = make("crimson_weeping_vine", NetherVineBlock::new);
	public static final BaseBlock WARPED_WEEPING_VINE = make("warped_weeping_vine", NetherVineBlock::new);
	public static final BaseBlock POISON_WEEPING_VINE = make("poison_weeping_vine", NetherVineBlock::new);
	public static final BaseBlock PALE_TREE_WEEPING_VINE = make("pale_tree_weeping_vine", NetherVineBlock::new);
	public static final BaseBlock EMBER_TREE_WEEPING_VINE = make("ember_tree_weeping_vine", NetherVineBlock::new);
	
	public static final BaseBlock FLAME_BULBS = make("flame_bulbs", NetherFloorPlantBlock::new);
	public static final BaseBlock CRIMSON_ROOTS = make("crimson_roots", NetherFloorPlantBlock::new);
	public static final BaseBlock NETHER_DAISY = make("nether_daisy", NetherFloorPlantBlock::new).setLightEmittance(0.5F);
	public static final BaseBlock FIREWEED = make("fireweed", DoubleFloorPlantBlock::new).setLightEmittance(0.5F);
	
	public static final BaseBlock CRIMSON_MOSS = make("crimson_moss", MossBlock::new);
	public static final BaseBlock CRIMSON_MOSS_BLOCK = make("crimson_moss_block", NetherLeavesBlock::new);
	
	private static BaseBlock make(String name, Function<Identifier, BaseBlock> constructor) {
		Identifier id = BNB.id(name);
		BaseBlock block = constructor.apply(id);
		block.setTranslationKey(id.toString());
		BLOCKS_WITH_ITEMS.add(block);
		return block;
	}
	
	public static void init() {}
}
