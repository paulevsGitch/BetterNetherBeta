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
		this.setFogColor("a4222f");
		this.setTopBlock(new BlockState(BlockListener.getBlock("nether_terrain"), NetherTerrainType.CRIMSON_NYLIUM));
		this.addStructure(NetherStructures.FLAME_BAMBOO, 1.0F, 3);
		this.addStructure(NetherStructures.CRIMSON_ROOTS, 1.0F, 10);
		this.addStructure(NetherStructures.LAMELLARIUM, 1.0F, 3);
		this.addStructure(NetherStructures.LANTERN_GRASS, 1.0F, 3);
		this.addStructure(NetherStructures.CRIMSON_BUSH, 1.0F, 3);
		this.addStructure(NetherStructures.CRIMSON_VINE, 1.0F, 8);
		this.setAmbientSound(NetherSounds.NETHER_FOREST_AMBIENCE);
		this.setFogDensity(2F);
		this.setFire(false);
	}
	
	@Override
	public float getParticleChance() {
		return 0.6F;
	}
	
	@Override
	public int getParticleID(Random random) {
		return random.nextInt(3);
	}
}
