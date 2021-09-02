package paulevs.bnb.world.structures;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class LargeStructure extends Structure {
	private Map<Point, int[]> structureMap = new HashMap<>();
	private int distance;
	
	@Override
	public boolean generate(Level level, Random rand, int x, int y, int z) {
		Point pos = new Point(x >> 8, z >> 8);
		int[] chunkType = structureMap.get(pos);
		if (chunkType == null) {
			return false;
		}
		return true;
	}
	
	protected void generateNear() {
	
	}
	
	protected abstract boolean getChunkType(Level level, int x, int z);
}
