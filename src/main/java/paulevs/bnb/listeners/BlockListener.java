package paulevs.bnb.listeners;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationloader.api.common.event.block.BlockRegister;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.block.NetherComplexStoneBlock;
import paulevs.bnb.block.NetherFungusBlock;
import paulevs.bnb.block.NetherLanternBlock;
import paulevs.bnb.block.NetherLeavesBlock;
import paulevs.bnb.block.NetherOreBlock;
import paulevs.bnb.block.NetherPlanksBlock;
import paulevs.bnb.block.NetherPlantBlockImpl;
import paulevs.bnb.block.NetherStairsBlock;
import paulevs.bnb.block.NetherTerrainBlock;
import paulevs.bnb.block.NetherTreeFurBlock;
import paulevs.bnb.block.NetherVineBlock;
import paulevs.bnb.block.NetherWoodBlock;
import paulevs.bnb.block.SpiderCocoonBlock;
import paulevs.bnb.block.types.NetherPlanks;
import paulevs.bnb.block.types.NetherrackBricks;
import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.interfaces.TriFunction;
import paulevs.bnb.util.BlockUtil;

public class BlockListener implements BlockRegister {
	private static final Map<String, BlockBase> BLOCKS = Maps.newHashMap();
	private static final List<BlockBase> BLOCKS_TAB = Lists.newArrayList();
	private static Set<Integer> occupiedIDs;
	private static int startID = 200;
	
	@Override
	public void registerBlocks() {
		BetterNetherBeta.configBlocks.load();
		occupiedIDs = BetterNetherBeta.configBlocks.getSet("blocks");
		
		register("nether_terrain", NetherTerrainBlock::new);
		
		register("nether_wood", NetherWoodBlock::new);
		register("nether_leaves", NetherLeavesBlock::new);
		register("nether_lantern", NetherLanternBlock::new);
		register("nether_tree_fur", NetherTreeFurBlock::new);
		register("nether_planks", NetherPlanksBlock::new);
		
		for (NetherPlanks plank: NetherPlanks.values()) {
			register("stairs_" + plank.getName(), NetherStairsBlock::new, getBlock("nether_planks"));
		}
		
		register("nether_plant", NetherPlantBlockImpl::new);
		register("nether_vine", NetherVineBlock::new);
		register("nether_fungus", NetherFungusBlock::new);
		
		register("spider_cocoon", SpiderCocoonBlock::new);
		
		register("nether_ore", NetherOreBlock::new);
		
		register("netherrack_bricks", NetherComplexStoneBlock::new, NetherrackBricks.class);
		register("stairs_netherrack_bricks", NetherStairsBlock::new, getBlock("netherrack_bricks"));
		
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
	
	private static <T extends NetherStairsBlock, B extends BlockBase> void register(String name, TriFunction<String, Integer, B, T> init, B source) {
		NetherStairsBlock block = init.apply(name, getID(name), source);
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
