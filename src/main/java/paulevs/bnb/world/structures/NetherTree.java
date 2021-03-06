package paulevs.bnb.world.structures;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.block.NetherPlantBlock;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.BlockUtil;

import java.util.Random;

public class NetherTree extends Structure {
	private final BlockState lantern;
	private final BlockState leaves;
	private final BlockState log;
	private final BlockState fur;
	private final float radiusScale;
	private final float aspect;
	
	public NetherTree(BlockState log, BlockState leaves, BlockState fur, BlockState lantern, float aspect, float radiusScale) {
		this.lantern = lantern;
		this.leaves = leaves;
		this.log = log;
		this.fur = fur;
		this.aspect = aspect;
		this.radiusScale = radiusScale;
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		if (!BlockUtil.isTerrain(level.getTileId(x, y - 1, z))) {
			return false;
		}
		
		int height = 5 + random.nextInt(10);
		
		if (!canGenerate(level, x, y, z, height)) {
			return false;
		}
		
		makeTrunk(level, x, y, z, height, random);
		makeLeaves(level, x, y + height, z, (height / 4 + 3) * radiusScale, random);
		
		return true;
	}
	
	private boolean canGenerate(Level level, int x, int y, int z, int height) {
		for (int i = 0; i < height; i++) {
			int tile = level.getTileId(x, y + i, z);
			if (!canReplaceRoot(tile)) {
				return false;
			}
		}
		return true;
	}
	
	private void makeTrunk(Level level, int x, int y, int z, int height, Random random) {
		for (int i = 0; i < height; i++) {
			int py = y + i;
			if (canReplaceRoot(level.getTileId(x, py, z))) {
				log.setBlockFast(level, x, py, z);
			}
		}
		
		for (int i = 0; i < 9; i++) {
			if (random.nextBoolean()) {
				int px = (i % 3) - 1 + x;
				int pz = (i / 3) - 1 + z;
				int max = (i & 1) == 1 ? height / 2 : height / 3;
				int h = random.nextInt(max);
				for (int j = -1; j < h; j++) {
					int py = y + j;
					if (canReplaceRoot(level.getTileId(px, py, pz))) {
						log.setBlockFast(level, px, py, pz);
					}
				}
			}
		}
		
		for (int i = 0; i < 9; i++) {
			if (random.nextBoolean()) {
				int px = (i % 3) - 1 + x;
				int pz = (i / 3) - 1 + z;
				int max = (i & 1) == 1 ? height / 3 : height / 4;
				int h = random.nextInt(max);
				for (int j = -1; j < h; j++) {
					int py = y - j + height;
					if (canReplaceRoot(level.getTileId(px, py, pz))) {
						log.setBlockFast(level, px, py, pz);
					}
				}
			}
		}
		
		if (height > 7) {
			for (int i = 0; i < 25; i++) {
				if (random.nextBoolean()) {
					int px = (i % 5) - 2 + x;
					int pz = (i / 5) - 2 + z;
					int h = random.nextInt(2);
					for (int j = 0; j < h; j++) {
						int py = y + j;
						if (canReplaceRoot(level.getTileId(px, py, pz))) {
							int tile = level.getTileId(px, py - 1, pz);
							if ((tile == log.getBlockID() || BlockUtil.isTerrain(tile)) && isLogNear(level, px, py, pz)) {
								log.setBlockFast(level, px, py, pz);
							}
						}
					}
				}
			}
		}
	}
	
	private boolean canReplaceRoot(int tile) {
		return tile == 0 || BlockUtil.blockByID(tile).material.isReplaceable() || BlockUtil.isTerrain(tile) || BlockUtil.blockByID(tile) instanceof NetherPlantBlock;
	}
	
	private boolean canReplaceLeaves(int tile) {
		return tile == 0 || BlockUtil.blockByID(tile).material.isReplaceable();
	}
	
	private boolean isLogNear(Level level, int x, int y, int z) {
		for (int i = 0; i < 9; i++) {
			int px = (i % 3) - 1 + x;
			int pz = (i / 3) - 1 + z;
			if (level.getTileId(px, y, pz) == log.getBlockID()) {
				return true;
			}
		}
		return false;
	}
	
	private void makeLeaves(Level level, int x, int y, int z, float radius, Random random) {
		double r2 = radius * radius;
		int largeR = MathHelper.floor(radius * aspect + 1);
		for (int i = (int) -radius; i <= radius; i++) {
			int i2 = i * i;
			int px = x + i;
			for (int j = (int) -radius; j <= radius; j++) {
				int j2 = j * j;
				int d = i2 + j2;
				int pz = z + j;
				if (d < r2) {
					for (int k = -largeR; k <= largeR; k++) {
						float k2 = k / aspect;
						k2 -= random.nextInt(2);
						k2 *= k2;
						if (d + k2 < r2) {
							k2 = k + largeR + random.nextInt(2);
							k2 *= k2;
							if (d + k2 > r2) {
								int py = y + k;
								if (canReplaceLeaves(level.getTileId(px, py, pz))) {
									if (random.nextInt(16) == 0) {
										lantern.setBlockFast(level, px, py, pz);
									}
									else {
										leaves.setBlockFast(level, px, py, pz);
									}
									if (random.nextBoolean() && canReplaceLeaves(level.getTileId(px, py - 1, pz))) {
										int length = 1 + random.nextInt(3);
										for (int n = 1; n <= length && canReplaceLeaves(level.getTileId(px, py - n, pz)); n++) {
											fur.setBlockFast(level, px, py - n, pz);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
