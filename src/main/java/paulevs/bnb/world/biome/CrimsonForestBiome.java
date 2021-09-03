package paulevs.bnb.world.biome;

import paulevs.bnb.block.types.NetherTerrainType;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.sound.NetherSounds;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.world.structures.NetherStructures;

import java.util.Random;

public class CrimsonForestBiome extends NetherBiome {
	public CrimsonForestBiome(String name) {
		super(name);
		this.setFogColor("e23f36");
		this.setTopBlock(new BlockState(BlockListener.getBlock("nether_terrain"), NetherTerrainType.CRIMSON_NYLIUM));
		this.addStructure(NetherStructures.CRIMSON_TREE, 1.0F, 5);
		this.addStructure(NetherStructures.CRIMSON_ROOTS, 1.0F, 8);
		this.addStructure(NetherStructures.LAMELLARIUM, 0.2F, 8);
		this.addStructure(NetherStructures.LANTERN_GRASS, 0.2F, 8);
		this.addStructure(NetherStructures.CRIMSON_BUSH, 0.2F, 8);
		this.addStructure(NetherStructures.CRIMSON_FUNGUS, 0.1F, 8);
		this.addStructure(NetherStructures.CRIMSON_COCOON, 0.01F, 8);
		this.addStructure(NetherStructures.CRIMSON_VINE, 1.0F, 20);
		this.setAmbientSound(NetherSounds.NETHER_FOREST);
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
