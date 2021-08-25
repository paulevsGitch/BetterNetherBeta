package paulevs.bnb.world.biome;

import paulevs.bnb.block.types.NetherTerrain;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.world.structures.NetherStructures;

import java.util.Random;

public class PoisonForest extends NetherBiome {
	public PoisonForest(String name) {
		super(name);
		this.setFogColor("b1fb43");
		this.setTopBlock(new BlockState(BlockListener.getBlock("nether_terrain"), NetherTerrain.POISON_NYLIUM));
		this.addTree(NetherStructures.POISON_TREE);
		this.addPlant(NetherStructures.BUBBLE_GRASS, 0.2F);
		this.addPlant(NetherStructures.LONGWEED, 0.3F);
		this.addPlant(NetherStructures.JELLYSHROOM, 0.1F);
		this.addPlant(NetherStructures.TAILGRASS, 1.0F);
		this.addPlant(NetherStructures.POISON_COCOON, 0.01F);
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
		return 6 + random.nextInt(3);
	}
}
