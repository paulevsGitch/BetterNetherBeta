package paulevs.bnb.world.biome;

import paulevs.bnb.block.types.NetherTerrain;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.world.structures.NetherStructures;

import java.util.Random;

public class CrimsonForest extends NetherBiome {
	public CrimsonForest(String name) {
		super(name);
		this.setFogColor("e23f36");
		this.setTopBlock(new BlockState(BlockListener.getBlock("nether_terrain"), NetherTerrain.CRIMSON_NYLIUM));
		this.addTree(NetherStructures.CRIMSON_TREE);
		this.addPlant(NetherStructures.CRIMSON_ROOTS, 1.0F);
		this.addPlant(NetherStructures.LAMELLARIUM, 0.5F);
		this.addPlant(NetherStructures.LANTERN_GRASS, 0.2F);
		this.addPlant(NetherStructures.CRIMSON_BUSH, 0.1F);
		this.addPlant(NetherStructures.CRIMSON_FUNGUS, 0.1F);
		this.addPlant(NetherStructures.CRIMSON_COCOON, 0.01F);
		this.addCeilPlant(NetherStructures.CRIMSON_VINE);
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
		return random.nextInt(3);
	}
}
