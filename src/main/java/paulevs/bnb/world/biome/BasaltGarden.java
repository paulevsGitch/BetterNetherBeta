package paulevs.bnb.world.biome;

import paulevs.bnb.block.types.BasaltBlockType;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.world.structures.NetherStructures;

import java.util.Random;

public class BasaltGarden extends NetherBiome {
	public BasaltGarden(String name) {
		super(name);
		this.setFogColor("c68885");
		this.setTopBlock(new BlockState(BlockListener.getBlock("basalt"), BasaltBlockType.BASALT));
		this.addTree(NetherStructures.FLAMING_BASALT_PEAK);
		this.addPlant(NetherStructures.CRIMSON_ROOTS, 1.0F);
		this.setMaxCeilPlantCount(0);
		this.setMaxPlantCount(10);
		this.setMaxTreeCount(5);
		this.setTopDepth(10);
		this.setFire(false);
	}
	
	@Override
	public float getParticleChance() {
		return 0.3F;
	}
	
	@Override
	public int getParticleID(Random random) {
		return random.nextInt(3);
	}
}