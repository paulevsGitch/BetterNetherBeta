package paulevs.bnb.world.structures;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.MHelper;

import java.util.Random;

public class TallPlant extends Structure {
	private final BlockState block;
	private final float radius;
	private final int height;
	private final int count;
	
	public TallPlant(BlockState block, int height, float radius) {
		this.count = MathHelper.floor(radius * 10);
		this.height = height;
		this.radius = radius;
		this.block = block;
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		int count = MHelper.randRange(this.count >> 1, this.count, random);
		for (int i = 0; i < count; i++) {
			int px = x + MHelper.clamp(MathHelper.floor(random.nextGaussian() * radius + 0.5), -8, 8);
			int pz = z + MHelper.clamp(MathHelper.floor(random.nextGaussian() * radius + 0.5), -8, 8);
			for (int py = y + 5; py > y - 5; py --) {
				if (level.getTileId(px, py, pz) == 0 && BlockUtil.isTerrain(level.getTileId(px, py - 1, pz))) {
					generatePlant(level, random, px, py, pz);
					break;
				}
			}
		}
		
		return true;
	}
	
	private void generatePlant(Level level, Random random, int x, int y, int z) {
		int maxY = y + MHelper.randRange(1, height, random);
		for (int py = y; py < maxY; py++) {
			if (level.getTileId(x, py, z) == 0) {
				block.setBlockFast(level, x, py, z);
			}
			else {
				return;
			}
		}
	}
}
