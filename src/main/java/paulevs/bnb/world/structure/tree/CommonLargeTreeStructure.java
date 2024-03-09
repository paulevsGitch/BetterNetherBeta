package paulevs.bnb.world.structure.tree;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bnb.block.BNBBlockTags;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.property.BNBBlockProperties;
import paulevs.bnb.block.property.BNBBlockProperties.VineShape;

import java.util.Random;

public class CommonLargeTreeStructure extends Structure {
	private final BlockState trunk;
	private final BlockState leaves;
	private final BlockState stem;
	private final BlockState branch;
	private final BlockState vine;
	private final int minHeight;
	private final int dHeight;
	private final float capWAspect;
	private final float capHAspect;
	private final float noise;
	
	public CommonLargeTreeStructure(Block trunk, Block leaves, Block stem, Block branch, Block vine, int minHeight, int maxHeight, float capWAspect, float capHAspect, float noise) {
		this.trunk = trunk.getDefaultState();
		this.leaves = leaves.getDefaultState();
		this.stem = stem.getDefaultState();
		this.branch = branch.getDefaultState();
		this.vine = vine.getDefaultState();
		this.minHeight = minHeight;
		this.dHeight = maxHeight - minHeight + 1;
		this.capWAspect = capWAspect;
		this.capHAspect = capHAspect;
		this.noise = noise;
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		int height = random.nextInt(dHeight) + minHeight;
		
		if (!level.getBlockState(x, y - 1, z).isIn(BNBBlockTags.NETHERRACK_TERRAIN)) {
			return false;
		}
		
		if (level.getBlockState(x, y, z).getMaterial() == Material.LAVA) return false;
		
		short maxHeight = (short) (height << 1);
		for (byte i = 1; i < maxHeight; i++) {
			if (!canReplace(level.getBlockState(x, y + i, z))) {
				height = (i - 1) >> 1;
				break;
			}
		}
		
		if (height < minHeight) return false;
		
		growTrunk(level, x, y, z, height);
		growRoots(level, random, x, y, z, height);
		growBranches(level, random, x, y, z, height);
		growCap(level, random, x, y + height, z, height * capWAspect, height * capHAspect);
		
		y--;
		for (byte j = 0; j < 9; j++) {
			int px = x + (j % 3) - 1;
			int pz = z + (j / 3) - 1;
			if (!level.getBlockState(px, y, pz).isIn(BNBBlockTags.NETHERRACK_TERRAIN)) continue;
			level.setBlockState(px, y, pz, Block.NETHERRACK.getDefaultState());
		}
		
		return true;
	}
	
	private void growTrunk(Level level, int x, int y, int z, int height) {
		for (byte i = -2; i < height; i++) {
			for (byte j = 0; j < 9; j++) {
				int px = x + (j % 3) - 1;
				int py = y + i;
				int pz = z + (j / 3) - 1;
				if (!canReplace(level.getBlockState(px, py, pz))) continue;
				level.setBlockState(px, py, pz, trunk);
			}
		}
	}
	
	private void growRoots(Level level, Random random, int x, int y, int z, int height) {
		for (byte i = 0; i < 12; i++) {
			Direction side = Direction.fromHorizontal(i / 3);
			Direction right = side.rotateYClockwise();
			byte offset = (byte) (3 - Math.abs(i % 3 - 1));
			int px = x + side.getOffsetX() * offset + right.getOffsetX() * (i % 3 - 1);
			int pz = z + side.getOffsetZ() * offset + right.getOffsetZ() * (i % 3 - 1);
			if (canReplace(level.getBlockState(px, y - 1, pz)) && canReplace(level.getBlockState(px, y - 2, pz))) {
				continue;
			}
			byte length = height > 3 ? (byte) random.nextInt(height / 3) : 1;
			int py = y + length;
			if (!canReplace(level.getBlockState(px, py, pz))) continue;
			BlockState stemSide = this.stem.with(BNBBlockProperties.AXIS, side.getAxis());
			int sx = px;
			int sz = pz;
			for (byte j = 2; j < offset; j++) {
				sx -= side.getOffsetX();
				sz -= side.getOffsetZ();
				if (!canReplace(level.getBlockState(sx, py, sz))) continue;
				level.setBlockState(sx, py, sz, stemSide);
			}
			BlockState branch = this.branch
				.with(BNBBlockProperties.getByFace(side.getOpposite()), true)
				.with(BNBBlockProperties.getByFace(Direction.DOWN), true);
			level.setBlockState(px, py, pz, branch);
			for (byte j = (byte) (length - 1); j >= -1; j--) {
				py = y + j;
				if (!canReplace(level.getBlockState(px, py, pz))) break;
				level.setBlockState(px, py, pz, stem);
				if (random.nextBoolean() || j == length - 1) continue;
				sx = px + side.getOffsetX();
				sz = pz + side.getOffsetZ();
				if (!canReplace(level.getBlockState(sx, py, sz))) continue;
				branch = this.branch
					.with(BNBBlockProperties.getByFace(Direction.UP), true)
					.with(BNBBlockProperties.getByFace(Direction.DOWN), true);
				level.setBlockState(px, py, pz, branch.with(BNBBlockProperties.getByFace(side), true));
				level.setBlockState(sx, py, sz, this.branch
					.with(BNBBlockProperties.getByFace(side.getOpposite()), true)
					.with(BNBBlockProperties.getByFace(Direction.DOWN), true)
				);
				int sy = py;
				for (byte k = j; k >= -1; k--) {
					if (!canReplace(level.getBlockState(sx, --sy, sz))) break;
					level.setBlockState(sx, sy, sz, stem);
				}
			}
		}
	}
	
	private void growBranches(Level level, Random random, int x, int y, int z, int height) {
		for (byte i = 0; i < 12; i++) {
			Direction side = Direction.fromHorizontal(i / 3);
			Direction right = side.rotateYClockwise();
			byte offset = (byte) (3 - Math.abs(i % 3 - 1));
			int px = x + side.getOffsetX() * offset + right.getOffsetX() * (i % 3 - 1);
			int pz = z + side.getOffsetZ() * offset + right.getOffsetZ() * (i % 3 - 1);
			int start = height / 3;
			byte length = start > 1 ? (byte) (random.nextInt(start) + start) : 1;
			int py = y + height - length;
			if (!canReplace(level.getBlockState(px, py, pz))) continue;
			BlockState stemSide = this.stem.with(BNBBlockProperties.AXIS, side.getAxis());
			int sx = px;
			int sz = pz;
			for (byte j = 2; j < offset; j++) {
				sx -= side.getOffsetX();
				sz -= side.getOffsetZ();
				if (!canReplace(level.getBlockState(sx, py, sz))) continue;
				level.setBlockState(sx, py, sz, stemSide);
			}
			BlockState branch = this.branch
				.with(BNBBlockProperties.getByFace(side.getOpposite()), true)
				.with(BNBBlockProperties.getByFace(Direction.UP), true);
			level.setBlockState(px, py, pz, branch);
			for (byte j = 1; j < length; j++) {
				int by = py + j;
				if (!canReplace(level.getBlockState(px, by, pz))) break;
				level.setBlockState(px, by, pz, stem);
				if (random.nextBoolean() || j == length - 1) continue;
				sx = px + side.getOffsetX();
				sz = pz + side.getOffsetZ();
				if (!canReplace(level.getBlockState(sx, by, sz))) continue;
				branch = this.branch
					.with(BNBBlockProperties.getByFace(Direction.UP), true)
					.with(BNBBlockProperties.getByFace(Direction.DOWN), true);
				level.setBlockState(px, by, pz, branch.with(BNBBlockProperties.getByFace(side), true));
				level.setBlockState(sx, by, sz, this.branch
					.with(BNBBlockProperties.getByFace(side.getOpposite()), true)
					.with(BNBBlockProperties.getByFace(Direction.UP), true)
				);
				int sy = by;
				for (byte k = (byte) (j + 1); k < length; k++) {
					if (!canReplace(level.getBlockState(sx, ++sy, sz))) break;
					level.setBlockState(sx, sy, sz, stem);
				}
			}
		}
	}
	
	private void growCap(Level level, Random random, int x, int y, int z, float radius, float height) {
		float sqrt = MathHelper.sqrt(radius);
		byte minXZ = (byte) MathHelper.floor(-sqrt);
		byte maxXZ = (byte) MathHelper.floor(sqrt + 2);
		
		sqrt = MathHelper.sqrt(height);
		byte minY = (byte) MathHelper.floor(-sqrt);
		byte maxY = (byte) MathHelper.floor(sqrt + 2);
		
		float aspect = radius / height;
		float angle = random.nextFloat() * (float) Math.PI * 2;
		int wx, wy, wz;
		float px, py, pz;
		
		for (byte dx = minXZ; dx < maxXZ; dx++) {
			wx = x + dx;
			px = dx * dx;
			for (byte dz = minXZ; dz < maxXZ; dz++) {
				wz = z + dz;
				pz = dz * dz;
				float distance = MathHelper.sqrt(px + pz) * 0.3F;
				float noise = (float) Math.sin(Math.atan2(dx, dz) + angle) * distance * 0.5F + distance;
				noise *= this.noise;
				for (byte dy = minY; dy < maxY; dy++) {
					py = dy * aspect + noise;
					if (py < 0) continue;
					if (px + pz + py * py > radius) continue;
					wy = y + dy;
					
					if (py < 1 && random.nextBoolean()) {
						if (!canReplace(level.getBlockState(wx, wy + 1, wz))) continue;
						level.setBlockState(wx, wy + 1, wz, leaves);
						
						if (level.getBlockState(wx, wy, wz).getMaterial().isReplaceable()) {
							int length = random.nextInt(3) + 2;
							placeVine(level, wx, wy, wz, length);
						}
						
						continue;
					}
					
					if (!canReplace(level.getBlockState(wx, wy, wz))) continue;
					level.setBlockState(wx, wy, wz, leaves);
					
					if (level.getBlockState(wx, wy - 1, wz).getMaterial().isReplaceable()) {
						int length = random.nextInt(3) + 2;
						placeVine(level, wx, wy - 1, wz, length);
					}
					
					if (!canReplace(level.getBlockState(wx, ++wy, wz))) continue;
					level.setBlockState(wx, wy, wz, leaves);
				}
			}
		}
		
		placeLantern(level, random, x + 2, y, z - 2);
		placeLantern(level, random, x + 2, y, z + 2);
		placeLantern(level, random, x - 2, y, z - 2);
		placeLantern(level, random, x - 2, y, z + 2);
		placeLantern(level, random, x + 2, y, z - 1);
		placeLantern(level, random, x + 2, y, z + 1);
		placeLantern(level, random, x - 2, y, z - 1);
		placeLantern(level, random, x - 2, y, z + 1);
		placeLantern(level, random, x + 1, y, z - 2);
		placeLantern(level, random, x + 1, y, z + 2);
		placeLantern(level, random, x - 1, y, z - 2);
		placeLantern(level, random, x - 1, y, z + 2);
	}
	
	private void placeLantern(Level level, Random random, int x, int y, int z) {
		while (level.getBlockState(x, y - 1, z) == leaves) y--;
		BlockState lamp = BNBBlocks.TREE_LANTERN.getDefaultState();
		if (canReplace(level.getBlockState(x, y, z))) level.setBlockState(x, y, z, lamp);
		if (random.nextBoolean() && canReplace(level.getBlockState(x, --y, z))) level.setBlockState(x, y, z, lamp);
	}
	
	private void placeVine(Level level, int x, int y, int z, int length) {
		BlockState middle = vine.with(BNBBlockProperties.VINE_SHAPE, VineShape.NORMAL);
		BlockState bottom = vine.with(BNBBlockProperties.VINE_SHAPE, VineShape.BOTTOM);
		for (byte i = 1; i <= length; i++) {
			BlockState state = level.getBlockState(x, y - 1, z);
			state = canReplace(state) && i < length ? middle : bottom;
			level.setBlockState(x, y--, z, state);
			if (state == bottom) return;
		}
	}
	
	private boolean canReplace(BlockState state) {
		if (state.isAir() || state == leaves) return true;
		Material material = state.getMaterial();
		return material.isReplaceable() || material == Material.PLANT;
	}
}
