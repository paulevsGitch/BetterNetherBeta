package paulevs.bnb.world.structures;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import paulevs.bnb.util.BlockState;

import java.util.Random;

public class NetherVine extends Structure {
	private final BlockState vine;
	
	public NetherVine(BlockState vine) {
		this.vine = vine;
	}
	
	@Override
	public boolean generate(Level level, Random rand, int x, int y, int z) {
		if (!vine.getBlock().canPlaceAt(level, x, y, z)) {
			return false;
		}
		
		int length = 3 + rand.nextInt(15);
		for (int i = 0; i < length; i++) {
			int py = y - i;
			if (level.getTileId(x, py, z) != 0) {
				break;
			}
			
			if (level.getTileId(x, py - 1, z) == 0) {
				vine.setBlockFast(level, x, py, z);
			}
			else {
				return true;
			}
		}
		
		return true;
	}
}
