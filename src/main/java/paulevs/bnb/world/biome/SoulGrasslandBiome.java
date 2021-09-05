package paulevs.bnb.world.biome;

import net.minecraft.entity.monster.Creeper;
import net.minecraft.entity.monster.ZombiePigman;
import net.minecraft.level.Level;
import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.block.types.SoulTerrainType;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.sound.NetherSounds;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.MHelper;
import paulevs.bnb.world.structures.NetherStructures;

import java.util.Random;

public class SoulGrasslandBiome extends NetherBiome {
	private BlockState soulNylium = new BlockState(BlockListener.getBlockID("soul_soil"), SoulTerrainType.SOUL_NYLIUM.getMeta());
	private BlockState air = new BlockState(0);
	private BlockState[] soilLayers = new BlockState[] {
		new BlockState(BlockListener.getBlockID("soul_layered_block"), 0),
		new BlockState(BlockListener.getBlockID("soul_layered_block"), 1),
		new BlockState(BlockListener.getBlockID("soul_layered_block"), 2)
	};
	
	public SoulGrasslandBiome(String name) {
		super(name);
		this.setFogColor("0a9ea2");
		this.setTopBlock(new BlockState(BlockListener.getBlockID("soul_soil")));
		this.addStructure(NetherStructures.SOUL_SPIRE_CONDITIONAL, 1.0F, 15);
		this.addStructure(NetherStructures.SOUL_CORAL, 1.0F, 32);
		this.addStructure(NetherStructures.SOUL_BULBITE_CONDITIONAL, 1.0F, 20);
		this.addStructure(NetherStructures.BONE_PEAKS_CONDITIONAL, 1.0F, 10);
		this.addStructure(NetherStructures.SOUL_HEART_CONDITIONAL, 0.3F, 8);
		this.setAmbientSound(NetherSounds.GRASSLANDS);
		this.addMonsterSpawn(Creeper.class, 5);
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
		float value = MHelper.getNoiseValue(x * 0.07, z * 0.07);
		value = MathHelper.abs(value);
		return value;
	}
}
