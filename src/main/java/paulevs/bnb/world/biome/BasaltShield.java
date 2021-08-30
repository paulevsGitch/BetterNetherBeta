package paulevs.bnb.world.biome;

import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.world.structures.NetherStructures;

import java.util.Random;

public class BasaltShield extends NetherBiome {
	public BasaltShield(String name) {
		super(name);
		this.setFogColor("a8a8a8");
		this.setTopBlock(new BlockState(BlockListener.getBlock("basalt")));
		this.addTree(NetherStructures.BASALT_PEAK);
		this.setMaxCeilPlantCount(0);
		this.setMaxPlantCount(0);
		this.setMaxTreeCount(5);
		this.setTopDepth(10);
		this.setFire(false);
	}
	
	@Override
	public float getParticleChance() {
		return 1F;
	}
	
	@Override
	public int getParticleID(Random random) {
		return random.nextInt(2) + 12;
	}
	
	@Override
	public boolean isParticlesEmissive() {
		return false;
	}
}