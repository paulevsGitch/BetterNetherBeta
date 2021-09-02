package paulevs.bnb.world.structures;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import paulevs.bnb.block.types.DarkshroomType;
import paulevs.bnb.block.types.NetherStemType;
import paulevs.bnb.block.types.UmbralithType;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.MHelper;

import java.util.Random;

public class DarkshroomStructure extends Structure {
	private static final BlockState CENTER = new BlockState(BlockListener.getBlock("darkshroom"), DarkshroomType.CENTER);
	private static final BlockState STEM = new BlockState(BlockListener.getBlock("nether_stem"), NetherStemType.DARKSHROOM);
	private static final BlockState SIDE = new BlockState(BlockListener.getBlock("darkshroom"), DarkshroomType.SIDE);
	
	@Override
	public boolean generate(Level level, Random rand, int x, int y, int z) {
		if (level.getTileId(x, y - 1, z) != BlockListener.getBlockID("umbralith") && level.getTileMeta(x, y - 1, z) != UmbralithType.DARK_NYLIUM.getMeta()) {
			return false;
		}
		
		if (level.getTileId(x, y + 1, z) != 0 || level.getTileId(x, y + 2, z) != 0) {
			return false;
		}
		
		int height = MHelper.randRange(3, 6, rand);
		for (int i = 0; i < height; i++) {
			if (level.getTileId(x, y, z) == 0) {
				STEM.setBlockFast(level, x, y, z);
			}
			else {
				y--;
				break;
			}
			y++;
		}
		buildHead(level, x, y, z);
		
		return true;
	}
	
	private void buildHead(Level level, int x, int y, int z) {
		for (int i = -1; i < 2; i++) {
			int px = x + i;
			for (int j = -1; j < 2; j++) {
				int pz = z + j;
				if (level.getTileId(px, y, pz) == 0) {
					SIDE.setBlockFast(level, px, y, pz);
				}
			}
		}
		CENTER.setBlockFast(level, x, y, z);
	}
}
