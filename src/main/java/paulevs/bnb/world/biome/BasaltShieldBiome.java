package paulevs.bnb.world.biome;

import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.sound.NetherSounds;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.world.structures.NetherStructures;

import java.util.Random;

public class BasaltShieldBiome extends NetherBiome {
	public BasaltShieldBiome(String name) {
		super(name);
		this.setFogColor("a8a8a8");
		this.setTopBlock(new BlockState(BlockListener.getBlock("basalt")));
		this.addStructure(NetherStructures.BASALT_PEAK, 1.0F, 5);
		this.setAmbientSound(NetherSounds.GRASSLANDS);
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