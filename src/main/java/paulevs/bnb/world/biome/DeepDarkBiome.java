package paulevs.bnb.world.biome;

import paulevs.bnb.block.types.UmbralithType;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.world.structures.NetherStructures;

import java.util.Random;

public class DeepDarkBiome extends NetherBiome {
	public DeepDarkBiome(String name) {
		super(name);
		this.setFogColor("052a32");
		this.setFogDensity(3F);
		this.setTopBlock(new BlockState(BlockListener.getBlock("umbralith"), UmbralithType.DARK_NYLIUM));
		this.setFillBlock(new BlockState(BlockListener.getBlock("umbralith"), UmbralithType.UMBRALITH));
		this.addStructure(NetherStructures.DARKSHROOM, 1.0F, 10);
		this.addStructure(NetherStructures.CYANIA, 1.0F, 4);
		this.addStructure(NetherStructures.SMALL_DARKSHROOM, 0.4F, 3);
		this.addStructure(NetherStructures.DARK_WILLOW, 0.4F, 3);
		this.addStructure(NetherStructures.DEEP_ROSE, 0.2F, 3);
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