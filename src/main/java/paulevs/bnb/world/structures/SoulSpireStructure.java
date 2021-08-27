package paulevs.bnb.world.structures;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.MHelper;

import java.util.Random;

public class SoulSpireStructure extends Structure {
	private static final int BLOCK_ID;
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		if (!BlockListener.getBlock("soul_spire").canPlaceAt(level, x, y, z)) {
			return false;
		}
		
		int length = MHelper.randRange(1, 4, random);
		for (int i = 0; i < length; i++) {
			if (!BlockUtil.isNonSolidNoLava(level.getTileId(x, y + i, z))) {
				return i > 0;
			}
			BlockUtil.fastTilePlace(level, x, y + i, z, BLOCK_ID, 0);
		}
		return true;
	}
	
	static {
		BLOCK_ID = BlockListener.getBlockID("soul_spire");
	}
}
