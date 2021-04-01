package paulevs.bnb.world.structures;

import java.util.Random;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.BlockUtil;

public class NetherGrass extends Structure {
	private final BlockState grass;
	
	public NetherGrass(BlockState grass) {
		this.grass = grass;
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		int count = 5 + random.nextInt(10);
		for (int i = 0; i < count; i++) {
			int px = MathHelper.floor(x + random.nextGaussian() * 3 + 0.5);
			int pz = MathHelper.floor(z + random.nextGaussian() * 3 + 0.5);
			for (int py = y + 5; py > y - 5; py --) {
				if (level.getTileId(px, py, pz) == 0 && BlockUtil.isTerrain(level.getTileId(px, py - 1, pz))) {
					grass.setBlock(level, px, py, pz);
					break;
				}
			}
		}
		
		return true;
	}
}
