package paulevs.bnb.world.biome;

import paulevs.bnb.block.types.NetherTerrain;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.world.structures.NetherStructures;

public class CrimsonForest extends NetherBiome {
	public CrimsonForest(String name) {
		super(name);
		this.setFogColor("e23f36");
		this.setTopBlock(new BlockState(BlockListener.getBlock("nether_terrain"), NetherTerrain.CRIMSON_NYLIUM));
		this.addTree(NetherStructures.CRIMSON_TREE);
		this.addPlant(NetherStructures.CRIMSON_ROOTS);
		this.addPlant(NetherStructures.LAMELLARIUM);
		this.addPlant(NetherStructures.LANTERN_GRASS);
		this.addPlant(NetherStructures.CRIMSON_BUSH);
		this.addCeilPlant(NetherStructures.CRIMSON_VINE);
		this.setMaxTreeCount(5);
		this.setMaxPlantCount(15);
		this.setMaxCeilPlantCount(30);
		this.setFire(false);
	}
}
