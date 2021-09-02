package paulevs.bnb.world.biome;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.MHelper;
import paulevs.bnb.world.structures.NetherStructures;

import java.util.Random;

public class SoulValleyBiome extends NetherBiome {
	private BlockState air = new BlockState(0);
	private BlockState soilBlock = new BlockState(BlockListener.getBlockID("soul_soil"));
	private BlockState[] soilLayers = new BlockState[] {
		new BlockState(BlockListener.getBlockID("soul_layered_block"), 0),
		new BlockState(BlockListener.getBlockID("soul_layered_block"), 1),
		new BlockState(BlockListener.getBlockID("soul_layered_block"), 2)
	};
	
	public SoulValleyBiome(String name) {
		super(name);
		this.setFogColor("0a9ea2");
		this.setTopBlock(new BlockState(BlockBase.SOUL_SAND));
		this.addStructure(NetherStructures.SOUL_SPIRE, 1.0F, 7);
		this.addStructure(NetherStructures.SOUL_BULBITE, 1.0F, 2);
		this.addStructure(NetherStructures.BONE_PEAKS, 0.6F, 2);
		this.addStructure(NetherStructures.SOUL_HEART, 0.3F, 2);
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
		/*for (int i = 0; i < 3; i++) {
			for (BlockDirection dir : BlockDirection.HORIZONTAL) {
				int dist = i + 1;
				if (!isSolidBlock(level.getTileId(x + dir.getX() * dist, y + dir.getY() * dist, z + dir.getZ() * dist))) {
					return soilLayers[i];
				}
			}
		}
		return getBottomBlock(level, x, y, z);*/
		return getNoiseValue(x, z) > 0 ? topBlock : soilBlock;
	}
	
	/*@Override
	public BlockState getBottomBlock(Level level, int x, int y, int z) {
		return getNoiseValue(x, z) > 0 ? topBlock : soilBlock;
	}*/
	
	private float getNoiseValue(int x, int z) {
		float value = MHelper.getNoiseValue(x * 0.1, z * 0.1);
		value += MHelper.getNoiseValue(x * 0.5, z * 0.5) * 0.3F;
		value += (MHelper.getRandomHash(x, z) & 7) / 28.0F - 0.125F;
		return value;
	}
	
	/*private float getHeight(Level level, int x, int y, int z) {
		int height = 0;
		for (int i = -2; i < 3; i++) {
			int px = x + i;
			for (int j = -2; j < 3; j++) {
				int pz = z + j;
				height += getColumnHeight(level, px, y, pz);
			}
		}
		return height / 25F;
	}
	
	private int getColumnHeight(Level level, int x, int y, int z) {
		if (!isSolidBlock(level.getTileId(x, y, z))) {
			for (int i = 1; i < 8; i++) {
				if (isSolidBlock(level.getTileId(x, y - i, z))) {
					return -(i + 1);
				}
			}
		}
		else {
			for (int i = 1; i < 8; i++) {
				if (!isSolidBlock(level.getTileId(x, y + i, z))) {
					return i - 1;
				}
			}
		}
		return 0;
	}
	
	private boolean isSolidBlock(int id) {
		return id == BlockBase.NETHERRACK.id || id == BlockBase.SOUL_SAND.id || id == soilBlock.getBlockID() || id == soilLayers[0].getBlockID();
	}*/
}
