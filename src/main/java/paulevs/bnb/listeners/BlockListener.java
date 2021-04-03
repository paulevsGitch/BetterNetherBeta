package paulevs.bnb.listeners;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.Maps;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationloader.api.common.event.block.BlockRegister;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.block.GlowingFurBlock;
import paulevs.bnb.block.NetherLeavesBlock;
import paulevs.bnb.block.NetherPlanksBlock;
import paulevs.bnb.block.NetherPlantBlockImpl;
import paulevs.bnb.block.NetherStairsBlock;
import paulevs.bnb.block.NetherTerrainBlock;
import paulevs.bnb.block.NetherVineBlock;
import paulevs.bnb.block.NetherWoodBlock;
import paulevs.bnb.block.TestBlock;
import paulevs.bnb.block.types.NetherPlanks;
import paulevs.bnb.interfaces.BlockInit;

public class BlockListener implements BlockRegister {
	private static final Map<String, BlockBase> BLOCKS = Maps.newHashMap();
	private static int startID = 200;
	
	@Override
	public void registerBlocks() {
		BetterNetherBeta.configBlocks.load();
		register("nether_terrain", NetherTerrainBlock::new);
		register("nether_wood", NetherWoodBlock::new);
		register("nether_leaves", NetherLeavesBlock::new);
		register("nether_plant", NetherPlantBlockImpl::new);
		register("nether_vine", NetherVineBlock::new);
		register("glowing_fur", GlowingFurBlock::new);
		register("nether_planks", NetherPlanksBlock::new);
		for (NetherPlanks plank: NetherPlanks.values()) {
			register("stairs_" + plank.getName(), NetherStairsBlock::new);
		}
		register("test", TestBlock::new);
		BetterNetherBeta.configBlocks.save();
	}
	
	private static int getID(String name) {
		return BetterNetherBeta.configBlocks.getInt("blocks." + name, startID++);
	}

	@SuppressWarnings("unused")
	private static <T extends BlockBase> void register(String name, Function<Integer, T> init) {
		BLOCKS.put(name, init.apply(getID(name)));
	}
	
	private static <T extends BlockBase> void register(String name, BlockInit<String, Integer, T> init) {
		BLOCKS.put(name, init.apply(name, getID(name)));
	}
	
	public static Collection<BlockBase> getModBlocks() {
		return BLOCKS.values();
	}

	@SuppressWarnings("unchecked")
	public static <T extends BlockBase> T getBlock(String name) {
		return (T) BLOCKS.getOrDefault(name, BlockBase.STONE);
	}
	
	public static int getBlockID(String name) {
		return getBlock(name).id;
	}
}
