package paulevs.bnb.world.biome;

import java.util.Random;

import net.minecraft.block.BlockBase;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.MHelper;
import paulevs.bnb.world.structures.NetherStructures;

public class SoulValley extends NetherBiome {
	private BlockState soilBlock = new BlockState(BlockListener.getBlockID("soul_soil"));
	
	public SoulValley(String name) {
		super(name);
		this.setFogColor("0a9ea2");
		this.setTopBlock(new BlockState(BlockBase.SOUL_SAND));
		this.addTree(NetherStructures.SOUL_SPIRE);
		this.addPlant(NetherStructures.SOUL_BULBITE, 1.0F);
		this.setMaxPlantCount(2);
		this.setMaxTreeCount(10);
		this.setFire(false);
		this.setTopDepth(5);
	}
	
	@Override
	public float getParticleChance() {
		return 0.1F;
	}
	
	@Override
	public int getParticleID(Random random) {
		return random.nextInt(3);
	}
	
	public BlockState getTopBlock(int x, int z) {
		return getNoiseValue(x, z) > 0 ? topBlock : soilBlock;
	}
	
	private double getNoiseValue(int x, int z) {
		double value = MHelper.getNoiseValue(x * 0.1, z * 0.1);
		value += MHelper.getNoiseValue(x * 0.5, z * 0.5) * 0.3;
		value += (MHelper.getRandomHash(x, z) & 7) / 28.0 - 0.125;
		return value;
	}
}
