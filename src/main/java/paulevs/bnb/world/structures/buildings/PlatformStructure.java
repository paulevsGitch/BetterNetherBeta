package paulevs.bnb.world.structures.buildings;

import paulevs.bnb.block.types.NetherLanternType;
import paulevs.bnb.block.types.NetherPlanksType;
import paulevs.bnb.block.types.NetherTreeFurType;
import paulevs.bnb.block.types.NetherWoodType;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;

public class PlatformStructure extends PaletteStructure {
	public PlatformStructure() {
		super(4, 7, -2);
	}
	
	@Override
	protected BlockState[] makePalette() {
		return new BlockState[] {
			new BlockState(BlockListener.getBlock("nether_lantern"), NetherLanternType.GHOST_PUMPKIN),
			new BlockState(BlockListener.getBlock("nether_planks"), NetherPlanksType.PALE_PLANKS),
			new BlockState(BlockListener.getBlock("nether_wood"), NetherWoodType.PALE_WOOD),
			new BlockState(BlockListener.getBlock("nether_tree_fur"), NetherTreeFurType.PALE_FUR)
		};
	}
	
	@Override
	protected byte[] makeData() {
		return new byte[] {
			3, 0, 0, 3,
			0, 0, 0, 0,
			0, 0, 0, 0,
			3, 0, 0, 3,
			
			3, 0, 0, 3,
			0, 0, 0, 0,
			0, 0, 0, 0,
			3, 0, 0, 3,
			
			3, 0, 0, 3,
			0, 0, 0, 0,
			4, 0, 0, 0,
			3, 0, 0, 3,
			
			3, 0, 4, 3,
			4, 0, 0, 0,
			4, 0, 4, 0,
			3, 0, 0, 3,
			
			3, 2, 2, 3,
			2, 2, 2, 2,
			2, 2, 2, 0,
			3, 2, 0, 0,
			
			1, 1, 0, 0,
			0, 1, 0, 0,
			0, 0, 0, 0,
			0, 0, 0, 0,
			
			0, 1, 0, 0,
			0, 0, 0, 0,
			0, 0, 0, 0,
			0, 0, 0, 0
		};
	}
}
