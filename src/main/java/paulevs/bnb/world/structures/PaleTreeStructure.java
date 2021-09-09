package paulevs.bnb.world.structures;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.block.types.NetherLeavesType;
import paulevs.bnb.block.types.NetherTreeFurType;
import paulevs.bnb.block.types.NetherWoodType;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.MHelper;

import java.util.Random;

public class PaleTreeStructure extends Structure {
	private static final BlockState LEAVES = new BlockState(BlockListener.getBlock("nether_leaves"), NetherLeavesType.PALE_LEAVES);
	private static final BlockState WOOD = new BlockState(BlockListener.getBlock("nether_wood"), NetherWoodType.PALE_WOOD);
	private static final BlockState FUR = new BlockState(BlockListener.getBlock("nether_tree_fur"), NetherTreeFurType.PALE_FUR);
	private static final int SAPLING = BlockListener.getBlockID("nether_sapling");
	
	@Override
	public boolean generate(Level level, Random rand, int x, int y, int z) {
		if (!BlockUtil.isTerrain(level.getTileId(x, y - 1, z))) {
			return false;
		}
		
		int h2 = y + MHelper.randRange(7, 11, rand);
		
		int count = MHelper.randRange(4, 6, rand);
		float offset = rand.nextFloat() * (float) Math.PI * 2;
		for (int i = 0; i < count; i++) {
			float angle = i / (float) count * (float) Math.PI * 2 + offset + MHelper.randRange(-0.1F, 0.1F, rand);
			float dist = MHelper.randRange(0.1F, 0.3F, rand);
			float dx = (float) Math.sin(angle) * dist;
			float dz = (float) Math.cos(angle) * dist;
			lineDown(level, rand, x, y + rand.nextInt(4), z, dx, dz, 4);
		}
		
		for (int py = y; py < h2; py++) {
			int id = level.getTileId(x, py, z);
			if (id == 0 || id == WOOD.getBlockID() || id == LEAVES.getBlockID() || id == FUR.getBlockID() || id == SAPLING) {
				WOOD.setBlockFast(level, x, py, z);
			}
			else {
				System.out.println(id);
				return true;
			}
		}
		
		count = MHelper.randRange(4, 6, rand);
		offset = rand.nextFloat() * (float) Math.PI * 2;
		for (int i = 0; i < count; i++) {
			float angle = i / (float) count * (float) Math.PI * 2 + offset + MHelper.randRange(-0.1F, 0.1F, rand);
			float dist = MHelper.randRange(0.5F, 0.75F, rand);
			float dx = (float) Math.sin(angle) * dist;
			float dz = (float) Math.cos(angle) * dist;
			int length = MHelper.randRange(3, 6, rand);
			lineUp(level, rand, x, h2 - length + rand.nextInt(3), z, dx, dz, length + 2);
		}
		
		makeCanopy(level, rand, x, h2, z);
		
		return true;
	}
	
	private void lineDown(Level level, Random rand, int x, int y, int z, float dx, float dz, int length) {
		for (int i = 0; i < length; i++) {
			int px = MathHelper.floor(x + dx * i);
			int pz = MathHelper.floor(z + dz * i);
			int py = y - i;
			int id = level.getTileId(px, py, pz);
			if (id == WOOD.getBlockID() || id == 0 || BlockUtil.isLava(id)) {
				WOOD.setBlockFast(level, px, py, pz);
			}
			else {
				return;
			}
		}
	}
	
	private void lineUp(Level level, Random rand, int x, int y, int z, float dx, float dz, int length) {
		int last = length - 1;
		for (int i = 0; i < length; i++) {
			int px = MathHelper.floor(x + dx * i + MHelper.randRange(-0.1F, 0.1F, rand));
			int pz = MathHelper.floor(z + dz * i + MHelper.randRange(-0.1F, 0.1F, rand));
			int py = y + i;
			int id = level.getTileId(px, py, pz);
			if (id == WOOD.getBlockID() || id == LEAVES.getBlockID() || id == 0 || BlockUtil.isLava(id)) {
				WOOD.setBlockFast(level, px, py, pz);
			}
			else {
				return;
			}
			if (i == last) {
				makeCanopy(level, rand, px, py, pz);
			}
		}
	}
	
	private void makeCanopy(Level level, Random rand, int x, int y, int z) {
		int radius = MHelper.randRange(4, 5, rand);
		makeCircle(level, rand, x, y, z, radius);
		makeCircle(level, rand, x, y + 1, z, radius - 1);
		makeCircle(level, rand, x, y + 2, z, radius - 1);
		makeSparceCircle(level, rand, x, y + 3, z, radius - 2);
		makeSparceCircle(level, rand, x, y - 1, z, radius - 1);
	}
	
	private void makeFurLine(Level level, Random rand, int x, int y, int z) {
		int h = MHelper.randRange(3, 9, rand);
		for (int i = 0; i < h; i++) {
			if (level.getTileId(x, --y, z) == 0) {
				FUR.setBlockFast(level, x, y, z);
			}
			else {
				return;
			}
		}
	}
	
	private void makeCircle(Level level, Random rand, int x, int y, int z, int radius) {
		int r2 = radius * radius;
		for (int i = -radius; i <= radius; i++) {
			int i2 = i * i;
			for (int j = -radius; j <= radius; j++) {
				int j2 = j * j;
				if (i2 + j2 + rand.nextInt(2) <= r2) {
					int px = x + i;
					int pz = z + j;
					if (level.getTileId(px, y, pz) == 0) {
						LEAVES.setBlockFast(level, px, y, pz);
						if (rand.nextInt(5) == 0) {
							makeFurLine(level, rand, px, y, pz);
						}
					}
				}
			}
		}
	}
	
	private void makeSparceCircle(Level level, Random rand, int x, int y, int z, int radius) {
		int r2 = radius * radius;
		for (int i = -radius; i <= radius; i++) {
			int i2 = i * i;
			for (int j = -radius; j <= radius; j++) {
				int j2 = j * j;
				if (i2 + j2 + rand.nextInt(2) <= r2 && rand.nextInt(3) == 0) {
					int px = x + i;
					int pz = z + j;
					if (level.getTileId(px, y, pz) == 0) {
						LEAVES.setBlockFast(level, px, y, pz);
						if (rand.nextInt(5) == 0) {
							makeFurLine(level, rand, px, y, pz);
						}
					}
				}
			}
		}
	}
}
