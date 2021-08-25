package paulevs.bnb.world.structures;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.util.BlockState;

import java.util.Random;

public class NetherOre extends Structure {
	private final BlockState ore;
	private final int size;
	
	public NetherOre(BlockState ore, int size) {
		this.size = size;
		this.ore = ore;
	}

	@Override
	public boolean generate(Level level, Random rand, int x, int y, int z) {
		float var6 = rand.nextFloat() * (float) Math.PI;
		double var7 = (double) ((float) (x + 8) + MathHelper.sin(var6) * (float) size / 8.0F);
		double var9 = (double) ((float) (x + 8) - MathHelper.sin(var6) * (float) size / 8.0F);
		double var11 = (double) ((float) (z + 8) + MathHelper.cos(var6) * (float) size / 8.0F);
		double var13 = (double) ((float) (z + 8) - MathHelper.cos(var6) * (float) size / 8.0F);
		double var15 = (double) (y + rand.nextInt(3) + 2);
		double var17 = (double) (y + rand.nextInt(3) + 2);

		for (int var19 = 0; var19 <= size; ++var19) {
			double var20 = var7 + (var9 - var7) * (double) var19 / (double) size;
			double var22 = var15 + (var17 - var15) * (double) var19 / (double) size;
			double var24 = var11 + (var13 - var11) * (double) var19 / (double) size;
			double var26 = rand.nextDouble() * (double) size / 16.0;
			double var28 = (double) (MathHelper.sin((float) var19 * (float) Math.PI / (float) size) + 1.0F) * var26 + 1.0;
			double var30 = (double) (MathHelper.sin((float) var19 * (float) Math.PI / (float) size) + 1.0F) * var26 + 1.0;
			int mixX = MathHelper.floor(var20 - var28 / 2.0);
			int minY = MathHelper.floor(var22 - var30 / 2.0);
			int minZ = MathHelper.floor(var24 - var28 / 2.0);
			int maxX = MathHelper.floor(var20 + var28 / 2.0);
			int maxY = MathHelper.floor(var22 + var30 / 2.0);
			int maxZ = MathHelper.floor(var24 + var28 / 2.0);

			for (int px = mixX; px <= maxX; ++px) {
				double px2 = ((double) px + 0.5 - var20) / (var28 / 2.0);
				px2 *= px2;
				if (px2 < 1.0D) {
					for (int py = minY; py <= maxY; ++py) {
						double py2 = ((double) py + 0.5 - var22) / (var30 / 2.0);
						py2 *= 2;
						if (px2 + py2 < 1.0D) {
							for (int pz = minZ; pz <= maxZ; ++pz) {
								double pz2 = ((double) pz + 0.5D - var24) / (var28 / 2.0);
								if (px2 + py2 + pz2 < 1.0 && level.getTileId(px, py, pz) == BlockBase.NETHERRACK.id) {
									ore.setBlock(level, px, py, pz);
								}
							}
						}
					}
				}
			}
		}

		return true;
	}
}
