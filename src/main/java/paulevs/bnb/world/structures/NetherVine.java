package paulevs.bnb.world.structures;

import java.util.Random;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import paulevs.bnb.util.BlockState;

public class NetherVine extends Structure {
	private final BlockState vineMiddle;
	private final BlockState vineBottom;
	
	public NetherVine(BlockState vineMiddle, BlockState vineBottom) {
		this.vineMiddle = vineMiddle;
		this.vineBottom = vineBottom;
	}
	
	@Override
	public boolean generate(Level level, Random rand, int x, int y, int z) {
		if (!vineMiddle.getBlock().canPlaceAt(level, x, y, z)) {
			return false;
		}
		
		int length = 3 + rand.nextInt(15);
		int last = length - 1;
		for (int i = 0; i < length; i++) {
			int py = y - i;
			if (level.getTileId(x, py, z) != 0) {
				break;
			}
			
			if (level.getTileId(x, py - 1, z) == 0 && i < last) {
				vineMiddle.setBlock(level, x, py, z);
			}
			else {
				vineBottom.setBlock(level, x, py, z);
				break;
			}
		}
		
		return true;
	}
}
