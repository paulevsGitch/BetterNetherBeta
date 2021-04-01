package paulevs.bnb.world.biome;

import paulevs.bnb.block.types.NetherTerrain;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.world.structures.NetherStructures;

public class WarpedForest extends NetherBiome {
	public WarpedForest(String name) {
		super(name);
		this.setTopBlock(new BlockState(BlockListener.getBlock("nether_terrain"), NetherTerrain.WARPED_NYLIUM));
		this.addTree(NetherStructures.WARPED_TREE);
		this.addPlant(NetherStructures.WARPED_ROOTS);
		this.addPlant(NetherStructures.GLOWTAIL);
		this.addPlant(NetherStructures.WARPED_CORAL);
		this.addPlant(NetherStructures.WARPED_MOSS);
		this.addCeilPlant(NetherStructures.WARPED_VINE);
		this.setMaxTreeCount(5);
		this.setMaxPlantCount(15);
		this.setMaxCeilPlantCount(30);
		this.setFire(false);
	}
}
