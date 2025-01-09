package paulevs.bnb.world.structure.tree;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bnb.block.BNBLeavesBlock;

public class LeavesDistributor {
	private final IntSet leavesPositions = new IntOpenHashSet();
	private final IntList logsPositions = new IntArrayList();
	private final IntList[] buffers = new IntList[] {
		new IntArrayList(),
		new IntArrayList()
	};
	
	private byte bufferIndex;
	private int centerX;
	private int centerY;
	private int centerZ;
	
	public void setCenter(int x, int y, int z) {
		centerX = x - 512;
		centerY = y - 512;
		centerZ = z - 512;
		logsPositions.clear();
		leavesPositions.clear();
	}
	
	public void addLog(int x, int y, int z) {
		logsPositions.add(getIndex(x, y, z));
	}
	
	public void addLeaves(int x, int y, int z) {
		leavesPositions.add(getIndex(x, y, z));
	}
	
	public void updateLeaves(Level level, BNBLeavesBlock leaves) {
		IntList startPositions = buffers[bufferIndex];
		startPositions.clear();
		startPositions.addAll(logsPositions);
		
		while (!startPositions.isEmpty()) {
			bufferIndex = (byte) ((bufferIndex + 1) & 1);
			IntList endPositions = buffers[bufferIndex];
			endPositions.clear();
			
			for (int index : startPositions) {
				int sx = getX(index);
				int sy = getY(index);
				int sz = getZ(index);
				
				for (byte id = 0; id < 6; id++) {
					Direction side = Direction.byId(id);
					int px = sx - side.getOffsetX();
					int py = sy - side.getOffsetY();
					int pz = sz - side.getOffsetZ();
					int leafIndex = getIndex(px, py, pz);
					if (!leavesPositions.contains(leafIndex)) continue;
					level.setBlockState(px, py, pz, leaves.getState(side));
					endPositions.add(leafIndex);
					leavesPositions.remove(leafIndex);
				}
			}
			
			startPositions = endPositions;
		}
		
		for (int index : leavesPositions) {
			int sx = getX(index);
			int sy = getY(index);
			int sz = getZ(index);
			level.setBlockState(sx, sy, sz, States.AIR.get());
		}
		
		logsPositions.clear();
		leavesPositions.clear();
	}
	
	private int getIndex(int x, int y, int z) {
		return ((x - centerX) & 1023) << 20 | ((y - centerY) & 1023) << 10 | (z - centerZ) & 1023;
	}
	
	private int getX(int index) {
		return (index >> 20) + centerX;
	}
	
	private int getY(int index) {
		return ((index >> 10) & 1023) + centerY;
	}
	
	private int getZ(int index) {
		return (index & 1023) + centerZ;
	}
}
