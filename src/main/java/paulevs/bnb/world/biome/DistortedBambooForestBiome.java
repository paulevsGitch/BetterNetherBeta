package paulevs.bnb.world.biome;

import paulevs.bnb.block.types.NetherTerrainType;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.sound.NetherSounds;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.world.structures.NetherStructures;

import java.util.Random;

public class DistortedBambooForestBiome extends NetherBiome {
	public DistortedBambooForestBiome(String name) {
		super(name);
		this.setFogColor("217b82");
		this.setTopBlock(new BlockState(BlockListener.getBlock("nether_terrain"), NetherTerrainType.WARPED_NYLIUM));
		this.addStructure(NetherStructures.DISTORTED_BAMBOO, 1.0F, 3);
		this.addStructure(NetherStructures.GLOWTAIL, 1.0F, 3);
		this.addStructure(NetherStructures.WARPED_CORAL, 1.0F, 3);
		this.addStructure(NetherStructures.WARPED_ROOTS, 1.0F, 10);
		this.addStructure(NetherStructures.WARPED_VINE, 1.0F, 8);
		this.setAmbientSound(NetherSounds.NETHER_FOREST_AMBIENCE);
		this.setFogDensity(2F);
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
