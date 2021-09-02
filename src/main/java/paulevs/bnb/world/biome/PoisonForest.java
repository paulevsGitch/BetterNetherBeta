package paulevs.bnb.world.biome;

import paulevs.bnb.block.types.NetherTerrainType;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.world.structures.NetherStructures;

import java.util.Random;

public class PoisonForest extends NetherBiome {
	public PoisonForest(String name) {
		super(name);
		this.setFogColor("72a950");
		this.setTopBlock(new BlockState(BlockListener.getBlock("nether_terrain"), NetherTerrainType.POISON_NYLIUM));
		this.addStructure(NetherStructures.POISON_TREE, 1.0F, 5);
		this.addStructure(NetherStructures.BUBBLE_GRASS, 0.2F, 15);
		this.addStructure(NetherStructures.LONGWEED, 0.3F, 15);
		this.addStructure(NetherStructures.JELLYSHROOM, 0.1F, 15);
		this.addStructure(NetherStructures.TAILGRASS, 1.0F, 15);
		this.addStructure(NetherStructures.POISON_COCOON, 0.01F, 15);
		this.addStructure(NetherStructures.VIRID_VINE, 1.0F, 10);
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
