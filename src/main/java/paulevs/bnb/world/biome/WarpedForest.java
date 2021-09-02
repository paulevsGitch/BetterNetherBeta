package paulevs.bnb.world.biome;

import paulevs.bnb.block.types.NetherTerrainType;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.world.structures.NetherStructures;

import java.util.Random;

public class WarpedForest extends NetherBiome {
	public WarpedForest(String name) {
		super(name);
		this.setFogColor("30b7a9");
		this.setTopBlock(new BlockState(BlockListener.getBlock("nether_terrain"), NetherTerrainType.WARPED_NYLIUM));
		this.addTree(NetherStructures.WARPED_TREE);
		this.addPlant(NetherStructures.WARPED_ROOTS, 1F);
		this.addPlant(NetherStructures.GLOWTAIL, 0.4F);
		this.addPlant(NetherStructures.WARPED_CORAL, 0.2F);
		this.addPlant(NetherStructures.WARPED_MOSS, 1F);
		this.addPlant(NetherStructures.WARPED_FUNGUS, 0.1F);
		this.addPlant(NetherStructures.WARPED_COCOON, 0.01F);
		this.addCeilPlant(NetherStructures.WARPED_VINE);
		this.setMaxTreeCount(5);
		this.setMaxPlantCount(15);
		this.setMaxCeilPlantCount(30);
		this.setFire(false);
	}
	
	@Override
	public float getParticleChance() {
		return 0.1F;
	}
	
	@Override
	public int getParticleID(Random random) {
		return 3 + random.nextInt(3);
	}
}
