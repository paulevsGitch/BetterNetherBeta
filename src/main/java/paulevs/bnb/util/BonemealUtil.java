package paulevs.bnb.util;

import java.util.Map;

import com.google.common.collect.Maps;

import paulevs.bnb.block.types.NetherPlants;
import paulevs.bnb.block.types.NetherTerrain;
import paulevs.bnb.listeners.BlockListener;

public class BonemealUtil {
	private static final Map<NetherTerrain, BlockState[]> GRASSES = Maps.newHashMap();
	
	public static BlockState[] getGrasses(NetherTerrain terrain) {
		return GRASSES.get(terrain);
	}
	
	static {
		GRASSES.put(NetherTerrain.CRIMSON_NYLIUM, new BlockState[] {
			new BlockState(BlockListener.getBlock("nether_plant"), NetherPlants.CRIMSON_ROOTS),
			new BlockState(BlockListener.getBlock("nether_plant"), NetherPlants.LAMELLARIUM),
			new BlockState(BlockListener.getBlock("nether_plant"), NetherPlants.LANTERN_GRASS),
			new BlockState(BlockListener.getBlock("nether_plant"), NetherPlants.CRIMSON_BUSH)
		});
		
		GRASSES.put(NetherTerrain.WARPED_NYLIUM, new BlockState[] {
			new BlockState(BlockListener.getBlock("nether_plant"), NetherPlants.WARPED_ROOTS),
			new BlockState(BlockListener.getBlock("nether_plant"), NetherPlants.GLOWTAIL),
			new BlockState(BlockListener.getBlock("nether_plant"), NetherPlants.WARPED_CORAL),
			new BlockState(BlockListener.getBlock("nether_plant"), NetherPlants.WARPED_MOSS)
		});
		
		GRASSES.put(NetherTerrain.POISON_NYLIUM, new BlockState[] {
			new BlockState(BlockListener.getBlock("nether_plant"), NetherPlants.BUBBLE_GRASS),
			new BlockState(BlockListener.getBlock("nether_plant"), NetherPlants.LONGWEED),
			new BlockState(BlockListener.getBlock("nether_plant"), NetherPlants.JELLYSHROOM),
			new BlockState(BlockListener.getBlock("nether_plant"), NetherPlants.TAILGRASS)
		});
	}
}
