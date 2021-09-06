package paulevs.bnb.world.biome;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import paulevs.bnb.block.types.NetherTerrainType;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.sound.NetherSounds;
import paulevs.bnb.util.BlockDirection;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.MHelper;
import paulevs.bnb.world.structures.NetherStructures;

import java.util.Random;

public class EmberBogBiome extends NetherBiome {
	private static final BlockState LAVA = new BlockState(BlockBase.STILL_LAVA);
	
	public EmberBogBiome(String name) {
		super(name);
		this.setFogColor("473241");
		this.setTopBlock(new BlockState(BlockListener.getBlock("nether_terrain"), NetherTerrainType.CORRUPTED_NYLIUM));
		this.addStructure(NetherStructures.EMBER_TREE, 1.0F, 8);
		this.addStructure(NetherStructures.RED_CATTAIL, 1.0F, 2);
		this.addStructure(NetherStructures.BULBINE, 0.1F, 5);
		this.addStructure(NetherStructures.VIOLEUM, 0.4F, 8);
		this.addStructure(NetherStructures.SHATTERED_GRASS, 1F, 8);
		this.setAmbientSound(NetherSounds.SWAMPLAND_AMBIENCE);
		this.setFire(false);
	}
	
	@Override
	public float getParticleChance() {
		return 0.2F;
	}
	
	@Override
	public int getParticleID(Random random) {
		return random.nextInt(3) + 9;
	}
	
	@Override
	public BlockState getTopBlock(Level level, int x, int y, int z) {
		if (getNoiseValue(x, z) > 0.3F && isWall(level, x, y, z)) {
			for (int i = 1; i < 8; i++) {
				BlockBase block = BlockUtil.getBlock(level, x, y + i, z);
				if (block != null && block.material == Material.PLANT) {
					BlockUtil.fastTilePlace(level, x, y + i, z, 0, 0);
				}
				else {
					break;
				}
			}
			return LAVA;
		}
		return super.getTopBlock(level, x, y, z);
	}
	
	private boolean isWall(Level level, int x, int y, int z) {
		for (BlockDirection dir: BlockDirection.HORIZONTAL) {
			int id = level.getTileId(x + dir.getX(), y, z + dir.getZ());
			if (!BlockUtil.isLava(id) && !BlockBase.FULL_OPAQUE[id]) {
				return false;
			}
		}
		return true;
	}
	
	private float getNoiseValue(int x, int z) {
		float value = MHelper.getNoiseValue(x * 0.05, z * 0.05);
		value += MHelper.getNoiseValue(x * 0.5, z * 0.5) * 0.3F;
		value += (MHelper.getRandomHash(x, z) & 7) / 28.0F - 0.125F;
		return value;
	}
}
