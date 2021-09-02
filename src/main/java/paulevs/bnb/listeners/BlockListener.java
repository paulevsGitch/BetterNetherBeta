package paulevs.bnb.listeners;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationloader.api.common.event.block.BlockRegister;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.block.BasaltBlock;
import paulevs.bnb.block.DarkshroomBlock;
import paulevs.bnb.block.NetherFungusBlock;
import paulevs.bnb.block.NetherGrassBlock;
import paulevs.bnb.block.NetherLanternBlock;
import paulevs.bnb.block.NetherLeavesBlock;
import paulevs.bnb.block.NetherOreBlock;
import paulevs.bnb.block.NetherPlanksBlock;
import paulevs.bnb.block.NetherSlabBlock;
import paulevs.bnb.block.NetherStairsBlock;
import paulevs.bnb.block.NetherStemBlock;
import paulevs.bnb.block.NetherTerrainBlock;
import paulevs.bnb.block.NetherTreeFurBlock;
import paulevs.bnb.block.NetherVineBlock;
import paulevs.bnb.block.NetherWoodBlock;
import paulevs.bnb.block.NetherrackBricksBlock;
import paulevs.bnb.block.SoulHeartBlock;
import paulevs.bnb.block.SoulPlantBlock;
import paulevs.bnb.block.SoulSpireBlock;
import paulevs.bnb.block.SoulSpirePlantBlock;
import paulevs.bnb.block.SoulTerrainBlock;
import paulevs.bnb.block.SpiderCocoonBlock;
import paulevs.bnb.block.TallNetherPlant;
import paulevs.bnb.block.UmbraPlantBlock;
import paulevs.bnb.block.UmbralithBlock;
import paulevs.bnb.block.types.BasaltBlockType;
import paulevs.bnb.block.types.NetherPlanksType;
import paulevs.bnb.block.types.NetherrackBricksType;
import paulevs.bnb.block.types.TallGlowNetherPlantType;
import paulevs.bnb.block.types.UmbralithType;
import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.interfaces.QuadFunction;
import paulevs.bnb.interfaces.TriFunction;
import paulevs.bnb.util.BlockUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public class BlockListener implements BlockRegister {
	private static final Map<String, BlockBase> BLOCKS = Maps.newHashMap();
	private static final List<BlockBase> BLOCKS_TAB = Lists.newArrayList();
	private static Set<Integer> occupiedIDs;
	private static int startID = 180;
	
	@Override
	public void registerBlocks() {
		BetterNetherBeta.configBlocks.load();
		occupiedIDs = BetterNetherBeta.configBlocks.getSet("blocks");
		
		register("nether_terrain", NetherTerrainBlock::new);
		register("soul_soil", SoulTerrainBlock::new);
		
		register("nether_wood", NetherWoodBlock::new);
		register("nether_leaves", NetherLeavesBlock::new);
		register("nether_lantern", NetherLanternBlock::new);
		register("nether_tree_fur", NetherTreeFurBlock::new);
		register("nether_planks", NetherPlanksBlock::new);
		
		for (NetherPlanksType plank: NetherPlanksType.values()) {
			BlockBase block = getBlock("nether_planks");
			register(plank.getName() + "_stairs", NetherStairsBlock::new, block, plank.getMeta());
			register(plank.getName() + "_slab", NetherSlabBlock::new, block, plank.getMeta());
		}
		
		register("nether_stem", NetherStemBlock::new);
		register("darkshroom", DarkshroomBlock::new);
		
		register("nether_grass", NetherGrassBlock::new);
		register("nether_vine", NetherVineBlock::new);
		register("nether_fungus", NetherFungusBlock::new);
		
		register("soul_spire", SoulSpirePlantBlock::new);
		register("soul_spire_block", SoulSpireBlock::new);
		register("soul_grass", SoulPlantBlock::new);
		register("tall_glow_nether_plant", TallNetherPlant::new, TallGlowNetherPlantType.class, 0.75F);
		register("soul_heart", SoulHeartBlock::new);
		
		register("umbra_plant", UmbraPlantBlock::new);
		
		register("spider_cocoon", SpiderCocoonBlock::new);
		
		register("netherrack_bricks", NetherrackBricksBlock::new);
		register("netherrack_brick_stairs", NetherStairsBlock::new, getBlock("netherrack_bricks"), NetherrackBricksType.NETHERRACK_BRICKS.getMeta());
		register("netherrack_brick_slab", NetherSlabBlock::new, getBlock("netherrack_bricks"), NetherrackBricksType.NETHERRACK_BRICKS.getMeta());
		register("netherrack_tile_slab", NetherSlabBlock::new, getBlock("netherrack_bricks"), NetherrackBricksType.NETHERRACK_BRICK_SMALL_TILE.getMeta());
		
		register("basalt", BasaltBlock::new);
		register("basalt_stairs", NetherStairsBlock::new, getBlock("basalt"), BasaltBlockType.BASALT.getMeta());
		register("basalt_slab", NetherSlabBlock::new, getBlock("basalt"), BasaltBlockType.BASALT.getMeta());
		register("basalt_brick_stairs", NetherStairsBlock::new, getBlock("basalt"), BasaltBlockType.BASALT_BRICKS.getMeta());
		register("basalt_brick_slab", NetherSlabBlock::new, getBlock("basalt"), BasaltBlockType.BASALT_BRICKS.getMeta());
		
		register("umbralith", UmbralithBlock::new);
		register("umbralith_stairs", NetherStairsBlock::new, getBlock("umbralith"), UmbralithType.UMBRALITH.getMeta());
		register("umbralith_slab", NetherSlabBlock::new, getBlock("umbralith"), UmbralithType.UMBRALITH.getMeta());
		register("umbralith_brick_stairs", NetherStairsBlock::new, getBlock("umbralith"), UmbralithType.UMBRALITH_BRICKS.getMeta());
		register("umbralith_brick_slab", NetherSlabBlock::new, getBlock("umbralith"), UmbralithType.UMBRALITH_BRICKS.getMeta());
		
		register("nether_ore", NetherOreBlock::new);
		
		//register("soul_layered_block", SoulLayeredBlock::new);
		
		occupiedIDs = null;
		BetterNetherBeta.configBlocks.save();
	}
	
	private static int getID(String name) {
		while ((BlockUtil.blockByID(startID) != null || occupiedIDs.contains(startID)) && startID < BlockUtil.getMaxID()) {
			startID++;
		}
		return BetterNetherBeta.configBlocks.getInt("blocks." + name, startID);
	}
	
	private static <T extends BlockBase> void register(String name, BiFunction<String, Integer, T> init) {
		BlockBase block = init.apply(name, getID(name));
		BLOCKS.put(name, block);
		BLOCKS_TAB.add(block);
	}
	
	private static <T extends BlockBase, B extends BlockEnum> void register(String name, TriFunction<String, Integer, Class<B>, T> init, Class<B> type) {
		BlockBase block = init.apply(name, getID(name), type);
		BLOCKS.put(name, block);
		BLOCKS_TAB.add(block);
	}
	
	@SuppressWarnings("unused")
	private static <B extends BlockBase, S extends BlockBase> void register(String name, TriFunction<String, Integer, S, B> init, S source) {
		BlockBase block = init.apply(name, getID(name), source);
		BLOCKS.put(name, block);
		BLOCKS_TAB.add(block);
	}
	
	private static <B extends BlockBase, S extends BlockBase> void register(String name, QuadFunction<String, Integer, S, Integer, B> init, S source, int meta) {
		BlockBase block = init.apply(name, getID(name), source, meta);
		BLOCKS.put(name, block);
		BLOCKS_TAB.add(block);
	}
	
	private static <B extends BlockBase, S extends BlockEnum> void register(String name, QuadFunction<String, Integer, Class<S>, Float, B> init, Class<S> type, float light) {
		BlockBase block = init.apply(name, getID(name), type, light);
		BLOCKS.put(name, block);
		BLOCKS_TAB.add(block);
	}
	
	public static Collection<BlockBase> getModBlocks() {
		return BLOCKS_TAB;
	}

	@SuppressWarnings("unchecked")
	public static <T extends BlockBase> T getBlock(String name) {
		return (T) BLOCKS.getOrDefault(name, BlockBase.STONE);
	}
	
	public static int getBlockID(String name) {
		return getBlock(name).id;
	}
}
