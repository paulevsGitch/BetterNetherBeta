package paulevs.bnb.world.biome;

import paulevs.bnb.block.types.NetherTerrainType;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.world.structures.NetherStructures;

import java.util.Random;

public class WarpedForest extends NetherBiome {
	public WarpedForest(String name) {
		super(name);
		this.setFogColor("30b7a9");
		this.setTopBlock(new BlockState(BlockListener.getBlock("nether_terrain"), NetherTerrainType.WARPED_NYLIUM));
		this.addStructure(NetherStructures.WARPED_TREE, 1.0F, 5);
		this.addStructure(NetherStructures.WARPED_ROOTS, 1.0F, 8);
		this.addStructure(NetherStructures.GLOWTAIL, 0.4F, 8);
		this.addStructure(NetherStructures.WARPED_CORAL, 0.2F, 8);
		this.addStructure(NetherStructures.WARPED_MOSS, 1F, 8);
		this.addStructure(NetherStructures.WARPED_FUNGUS, 0.1F, 8);
		this.addStructure(NetherStructures.WARPED_COCOON, 0.01F, 8);
		this.addStructure(NetherStructures.WARPED_VINE, 1.0F, 20);
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
