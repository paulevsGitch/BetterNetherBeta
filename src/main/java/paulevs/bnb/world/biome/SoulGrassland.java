package paulevs.bnb.world.biome;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.block.types.SoulTerrainType;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.MHelper;
import paulevs.bnb.world.structures.NetherStructures;

import java.util.Random;

public class SoulGrassland extends NetherBiome {
	private BlockState soulNylium = new BlockState(BlockListener.getBlockID("soul_soil"), SoulTerrainType.SOUL_NYLIUM.getMeta());
	private BlockState air = new BlockState(0);
	private BlockState[] soilLayers = new BlockState[] {
		new BlockState(BlockListener.getBlockID("soul_layered_block"), 0),
		new BlockState(BlockListener.getBlockID("soul_layered_block"), 1),
		new BlockState(BlockListener.getBlockID("soul_layered_block"), 2)
	};
	
	public SoulGrassland(String name) {
		super(name);
		this.setFogColor("0a9ea2");
		this.setTopBlock(new BlockState(BlockListener.getBlockID("soul_soil")));
		//this.addTree(NetherStructures.SOUL_SPIRE);
		//this.addPlant(NetherStructures.SOUL_BULBITE, 1.0F);
		//this.addPlant(NetherStructures.BONE_PEAKS, 0.6F);
		//this.addPlant(NetherStructures.SOUL_HEART, 0.3F);
		this.setMaxPlantCount(2);
		this.setMaxTreeCount(7);
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
	
	@Override
	public BlockState getTopBlock(Level level, int x, int y, int z) {
		float noise = getNoiseValue(x, z);
		
		if (noise > 0.32) {
			topBlock.setBlockFast(level, x, y + 1, z);
			topBlock.setBlockFast(level, x, y + 2, z);
			soulNylium.setBlockFast(level, x, y + 3, z);
		}
		else if (noise > 0.3) {
			topBlock.setBlockFast(level, x, y + 1, z);
			soulNylium.setBlockFast(level, x, y + 2, z);
		}
		
		return topBlock;
	}
	
	public BlockState getBottomBlock(Level level, int x, int y, int z) {
		return topBlock;
	}
	
	private float getNoiseValue(int x, int z) {
		float value = MHelper.getNoiseValue(x * 0.1, z * 0.1);
		value = MathHelper.abs(value);
		return value;
	}
}
