package paulevs.bnb.world.biome;

import paulevs.bnb.block.types.BasaltBlockType;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.world.structures.NetherStructures;

import java.util.Random;

public class BasaltGardenBiome extends NetherBiome {
	public BasaltGardenBiome(String name) {
		super(name);
		this.setFogColor("c68885");
		this.setTopBlock(new BlockState(BlockListener.getBlock("basalt"), BasaltBlockType.BASALT));
		this.addStructure(NetherStructures.FLAMING_BASALT_PEAK, 1.0F, 5);
		this.addStructure(NetherStructures.CRIMSON_ROOTS, 1.0F, 10);
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