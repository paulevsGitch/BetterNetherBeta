package paulevs.bnb.world.biome;

import paulevs.bnb.block.types.NetherTerrainType;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.world.structures.NetherStructures;

import java.util.Random;

public class CorruptedLands extends NetherBiome {
	public CorruptedLands(String name) {
		super(name);
		this.setFogColor("1c1323");
		this.setTopBlock(new BlockState(BlockListener.getBlock("nether_terrain"), NetherTerrainType.CORRUPTED_NYLIUM));
		this.addStructure(NetherStructures.BULBINE, 0.1F, 8);
		this.addStructure(NetherStructures.VIOLEUM, 0.3F, 8);
		this.addStructure(NetherStructures.SHATTERED_GRASS, 1F, 8);
		this.setFire(false);
	}
	
	@Override
	public float getParticleChance() {
		return 0.3F;
	}
	
	@Override
	public int getParticleID(Random random) {
		return random.nextInt(3) + 9;
	}
}
