package paulevs.bnb.world.biome;

import net.minecraft.entity.monster.Creeper;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.sound.NetherSounds;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.world.structures.NetherStructures;

import java.util.Random;

public class PaleForestBiome extends NetherBiome {
	public PaleForestBiome(String name) {
		super(name);
		this.setFogColor("b09f91");
		this.setTopBlock(new BlockState(BlockListener.getBlockID("soul_soil")));
		this.addStructure(NetherStructures.PALE_TREE, 1.0F, 7);
		this.addStructure(NetherStructures.COBWEB_PEAK, 1.0F, 4);
		this.addStructure(NetherStructures.GHOST_PUMPKIN, 1.0F, 15);
		this.addStructure(NetherStructures.BONE_PEAKS, 1.0F, 4);
		this.setAmbientSound(NetherSounds.NETHER_FOREST_AMBIENCE);
		this.addMonsterSpawn(Creeper.class, 5);
		this.setFire(false);
		this.setTopDepth(5);
	}
	
	@Override
	public float getParticleChance() {
		return 0.5F;
	}
	
	@Override
	public int getParticleID(Random random) {
		return 14 + random.nextInt(2);
	}
	
	@Override
	public boolean isParticlesEmissive() {
		return false;
	}
}
