package paulevs.bnb.world.structure.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;

import java.util.Arrays;
import java.util.Random;

public class NetherLake extends Structure {
	private static final BlockState LAVA = Block.STILL_LAVA.getDefaultState();
	private static final BlockState AIR = States.AIR.get();
	private final boolean[] mask = new boolean[2048];
	
	public boolean generate(Level level, Random random, int x, int y, int z) {
		x -= 8;
		y -= 4;
		z -= 8;
		
		Arrays.fill(mask, false);
		
		int count = random.nextInt(4) + 4;
		
		for (int i = 0; i < count; ++i) {
			float rx1 = random.nextFloat() * 6.0F + 3.0F;
			float ry1 = random.nextFloat() * 4.0F + 2.0F;
			float rz1 = random.nextFloat() * 6.0F + 3.0F;
			float rx2 = random.nextFloat() * (16.0F - rx1 - 2.0F) + 1.0F + rx1 / 2.0F;
			float ry2 = random.nextFloat() * ( 8.0F - ry1 - 4.0F) + 2.0F + ry1 / 2.0F;
			float rz2 = random.nextFloat() * (16.0F - rz1 - 2.0F) + 1.0F + rz1 / 2.0F;
			for (byte dx = 1; dx < 15; ++dx) {
				for (byte dz = 1; dz < 15; ++dz) {
					for (byte dy = 1; dy < 7; ++dy) {
						float px = (dx - rx2) / (rx1 / 2.0F);
						float py = (dy - ry2) / (ry1 / 2.0F);
						float pz = (dz - rz2) / (rz1 / 2.0F);
						float dist = px * px + py * py + pz * pz;
						if (!(dist < 1.0)) continue;
						mask[(dx * 16 + dz) * 8 + dy] = true;
					}
				}
			}
		}
		
		for (byte dx = 0; dx < 16; ++dx) {
			for (byte dz = 0; dz < 16; ++dz) {
				for (byte dy = 0; dy < 8; ++dy) {
					boolean flag = !mask[(dx * 16 + dz) * 8 + dy] && (
						dx < 15 && mask[((dx + 1) * 16 + dz) * 8 + dy] ||
						dx >  0 && mask[((dx - 1) * 16 + dz) * 8 + dy] ||
						dz < 15 && mask[(dx * 16 + (dz + 1)) * 8 + dy] ||
						dz >  0 && mask[(dx * 16 + (dz - 1)) * 8 + dy] ||
						dy <  7 && mask[(dx * 16 + dz) * 8 + (dy + 1)] ||
						dy >  0 && mask[(dx * 16 + dz) * 8 + (dy - 1)]
					);
					if (!flag) continue;
					
					Material material = level.getMaterial(x + dx, y + dy, z + dz);
					if (dy >= 4 && material.isLiquid()) return false;
					
					if (dy >= 4 || material.isSolid() || material == Material.LAVA) continue;
					return false;
				}
			}
		}
		
		for (byte dx = 0; dx < 16; ++dx) {
			for (byte dz = 0; dz < 16; ++dz) {
				for (byte dy = 0; dy < 8; ++dy) {
					if (!mask[(dx * 16 + dz) * 8 + dy]) continue;
					level.setBlockState(x + dx, y + dy, z + dz, dy >= 4 ? AIR : LAVA);
				}
			}
		}
		
		return true;
	}
}
