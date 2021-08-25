package paulevs.bnb.world.biome;

import paulevs.bnb.block.types.NetherTerrain;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;

import java.util.Random;

public class CorruptedLands extends NetherBiome {
	public CorruptedLands(String name) {
		super(name);
		this.setFogColor("1c1323");
		this.setTopBlock(new BlockState(BlockListener.getBlock("nether_terrain"), NetherTerrain.CORRUPTED_NYLIUM));
		this.setMaxTreeCount(2);
		this.setMaxPlantCount(2);
		this.setMaxCeilPlantCount(2);
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
