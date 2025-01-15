package paulevs.bnb.util;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.level.Level;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bnb.world.generator.BNBChunkStatus;
import paulevs.bnb.world.generator.BNBWorldChunk;

import java.util.ArrayList;
import java.util.List;

public class BNBPortalManager {
	private static final List<BlockState> PORTAL_STATES = new ArrayList<>();
	private static final IntList BLOCK_POSITIONS = new IntArrayList();
	private static final IntList[] BUFFERS = new IntList[] {
		new IntArrayList(),
		new IntArrayList()
	};
	private static final Direction[] OFFSETS = new Direction[4];
	
	private static byte bufferIndex;
	private static int centerX;
	private static int centerY;
	private static int centerZ;
	
	public static boolean makePortal(Level level, Entity entity) {
		if (level.dimension.id == -1) {
			for (int i = 0; i < 9; i++) {
				BNBWorldChunk chunk = BNBWorldChunk.cast(level.getChunk(
					entity.chunkX + (i % 3) - 1,
					entity.chunkZ + i / 3 - 1
				));
				while (chunk.bnb_getStatus() == BNBChunkStatus.EMPTY) {
					try {
						//noinspection BusyWait
						Thread.sleep(10);
					}
					catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		
		Level originLevel = entity.bnb_getOriginLevel();
		if (originLevel == null) return false;
		
		BlockPos originPos = entity.bnb_getOriginPos();
		copyPortal(originLevel, originPos);
		
		int portalY = level.dimension.id == -1 ? 96 : 63;
		for (int y = level.getTopY(); y >= level.getBottomY(); y--) {
			if (!canPlace(level, originPos.x, y, originPos.z)) continue;
			portalY = y;
			break;
		}
		
		placePortal(level, originPos.x, portalY, originPos.z);
		entity.setPosition(originPos.x + 0.5, portalY, originPos.z + 0.5);
		
		return true;
	}
	
	public static boolean tryCreatePortal(Level level, int x, int y, int z) {
		if (noPortalFrame(level, x, y, z, Direction.NORTH) && noPortalFrame(level, x, y, z, Direction.EAST)) return false;
		
		BlockState portal = Block.PORTAL.getDefaultState();
		
		int minX = x;
		int minY = y;
		int minZ = z;
		int maxX = x;
		int maxY = y;
		int maxZ = z;
		
		for (int index : BLOCK_POSITIONS) {
			int px = x + getLocalX(index);
			int py = y + getLocalY(index);
			int pz = z + getLocalZ(index);
			level.setBlockState(px, py, pz, portal);
			minX = Math.min(minX, px);
			minY = Math.min(minY, py);
			minZ = Math.min(minZ, pz);
			maxX = Math.max(maxX, px);
			maxY = Math.max(maxY, py);
			maxZ = Math.max(maxZ, pz);
		}
		
		level.updateArea(minX, minY, minZ, maxX, maxY, maxZ);
		
		return true;
	}
	
	public static boolean portalCanExist(Level level, int x, int y, int z) {
		if (!isValid(level.getBlockState(x, y + 1, z)) || !isValid(level.getBlockState(x, y - 1, z))) return false;
		boolean a = isValid(level.getBlockState(x - 1, y, z)) && isValid(level.getBlockState(x + 1, y, z));
		boolean b = isValid(level.getBlockState(x, y, z - 1)) && isValid(level.getBlockState(x, y, z + 1));
		return a || b;
	}
	
	private static boolean isValid(BlockState state) {
		return state.isOf(Block.PORTAL) || state.isOf(Block.OBSIDIAN);
	}
	
	private static boolean canPlace(Level level, int x, int y, int z) {
		boolean hasGround = false;
		for (int index : BLOCK_POSITIONS) {
			int py = y + getLocalY(index);
			if (py > level.getTopY() || py < level.getBottomY()) return false;
			int px = x + getLocalX(index);
			int pz = z + getLocalZ(index);
			if (!level.getBlockState(px, py, pz).getMaterial().isReplaceable()) return false;
			hasGround |= level.getBlockState(px, py - 1, pz).getMaterial().blocksMovement();
		}
		return hasGround;
	}
	
	private static Direction getPortalDirection(Level level, BlockPos pos) {
		for (byte i = 0; i < 4; i++) {
			Direction dir = Direction.fromHorizontal(i);
			int px = pos.x + dir.getOffsetX();
			int pz = pos.z + dir.getOffsetZ();
			BlockState state = level.getBlockState(px, pos.y, pz);
			if (state.isOf(Block.PORTAL) || state.isOf(Block.OBSIDIAN)) {
				return dir;
			}
		}
		return Direction.NORTH;
	}
	
	private static void placePortal(Level level, int x, int y, int z) {
		for (int i = 0; i < BLOCK_POSITIONS.size(); i++) {
			int index = BLOCK_POSITIONS.getInt(i);
			BlockState state = PORTAL_STATES.get(i);
			int px = x + getLocalX(index);
			int py = y + getLocalY(index);
			int pz = z + getLocalZ(index);
			level.setBlockState(px, py, pz, state);
		}
	}
	
	private static void copyPortal(Level level, BlockPos pos) {
		Direction dir = getPortalDirection(level, pos);
		
		centerX = pos.x - 512;
		centerY = pos.y - 512;
		centerZ = pos.z - 512;
		
		IntList startPositions = BUFFERS[bufferIndex];
		startPositions.clear();
		startPositions.add(getIndex(pos.x, pos.y, pos.z));
		
		BLOCK_POSITIONS.clear();
		PORTAL_STATES.clear();
		
		OFFSETS[0] = dir;
		OFFSETS[1] = dir.getOpposite();
		
		BLOCK_POSITIONS.add(startPositions.getInt(0));
		PORTAL_STATES.add(level.getBlockState(pos));
		
		while (!startPositions.isEmpty()) {
			bufferIndex = (byte) ((bufferIndex + 1) & 1);
			IntList endPositions = BUFFERS[bufferIndex];
			endPositions.clear();
			
			for (int index : startPositions) {
				int sx = getX(index);
				int sy = getY(index);
				int sz = getZ(index);
				
				for (Direction side : OFFSETS) {
					int px = sx - side.getOffsetX();
					int py = sy - side.getOffsetY();
					int pz = sz - side.getOffsetZ();
					int sideIndex = getIndex(px, py, pz);
					if (BLOCK_POSITIONS.contains(sideIndex)) continue;
					BlockState state = level.getBlockState(px, py, pz);
					if (!state.isOf(Block.PORTAL) && !state.isOf(Block.OBSIDIAN)) continue;
					endPositions.add(sideIndex);
					BLOCK_POSITIONS.add(sideIndex);
					PORTAL_STATES.add(state);
				}
			}
			
			startPositions = endPositions;
		}
	}
	
	private static boolean noPortalFrame(Level level, int x, int y, int z, Direction side) {
		centerX = x - 512;
		centerY = y - 512;
		centerZ = z - 512;
		
		IntList startPositions = BUFFERS[bufferIndex];
		startPositions.clear();
		startPositions.add(getIndex(x, y, z));
		
		BLOCK_POSITIONS.clear();
		
		OFFSETS[0] = side.getOpposite();
		OFFSETS[1] = side;
		
		while (!startPositions.isEmpty()) {
			bufferIndex = (byte) ((bufferIndex + 1) & 1);
			IntList endPositions = BUFFERS[bufferIndex];
			endPositions.clear();
			
			for (int index : startPositions) {
				int sx = getX(index);
				int sy = getY(index);
				int sz = getZ(index);
				
				for (Direction offset : OFFSETS) {
					int px = sx - offset.getOffsetX();
					if (Math.abs(px - x) > 64) return true;
					int py = sy - offset.getOffsetY();
					if (Math.abs(py - y) > 64) return true;
					int pz = sz - offset.getOffsetZ();
					if (Math.abs(pz - z) > 64) return true;
					int sideIndex = getIndex(px, py, pz);
					if (BLOCK_POSITIONS.contains(sideIndex)) continue;
					BlockState state = level.getBlockState(px, py, pz);
					if (state.isOf(Block.OBSIDIAN)) continue;
					if (!state.isAir() && !state.isOf(Block.FIRE)) return true;
					endPositions.add(sideIndex);
					BLOCK_POSITIONS.add(sideIndex);
				}
			}
			
			startPositions = endPositions;
		}
		
		return false;
	}
	
	private static int getIndex(int x, int y, int z) {
		return ((x - centerX) & 1023) << 20 | ((y - centerY) & 1023) << 10 | (z - centerZ) & 1023;
	}
	
	private static int getLocalX(int index) {
		return (index >> 20) - 512;
	}
	
	private static int getLocalY(int index) {
		return ((index >> 10) & 1023) - 512;
	}
	
	private static int getLocalZ(int index) {
		return (index & 1023) - 512;
	}
	
	private static int getX(int index) {
		return (index >> 20) + centerX;
	}
	
	private static int getY(int index) {
		return ((index >> 10) & 1023) + centerY;
	}
	
	private static int getZ(int index) {
		return (index & 1023) + centerZ;
	}
	
	static {
		OFFSETS[2] = Direction.UP;
		OFFSETS[3] = Direction.DOWN;
	}
}
