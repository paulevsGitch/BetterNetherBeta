package paulevs.bnb.world.structures;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import paulevs.bnb.util.BlockState;

import java.util.Random;

@Deprecated
public class NetherGroupedVine extends Structure {
	private final BlockState vine;
	
	public NetherGroupedVine(BlockState vine) {
		this.vine = vine;
	}
	
	@Override
	public boolean generate(Level level, Random rand, int x, int y, int z) {
		int count = 3 + rand.nextInt(5);
		for (int i = 0; i < count; i++) {
			int px = x + rand.nextInt(5) - 2;
			int pz = z + rand.nextInt(5) - 2;
			for (int py = y - 3; py < y + 3; py++) {
				if (vine.getBlock().canPlaceAt(level, px, py, pz)) {
					generateVine(level, rand, px, py, pz);
					break;
				}
			}
		}
		
		return true;
	}
	
	private void generateVine(Level level, Random rand, int x, int y, int z) {
		int length = 3 + rand.nextInt(8);
		for (int i = 0; i < length; i++) {
			int py = y - i;
			if (level.getTileId(x, py, z) != 0) {
				break;
			}
			
			if (level.getTileId(x, py - 1, z) == 0) {
				vine.setBlockFast(level, x, py, z);
			}
			else {
				return;
			}
		}
	}
}
