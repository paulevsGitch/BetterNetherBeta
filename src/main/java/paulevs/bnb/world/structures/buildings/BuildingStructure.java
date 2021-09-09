package paulevs.bnb.world.structures.buildings;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.Vec3i;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.BlockUtil;

import java.util.Random;

public abstract class BuildingStructure extends Structure {
	private final Vec3i offset = new Vec3i();
	private final BlockState[] data;
	private final int side2;
	private final int side;
	
	public BuildingStructure(int side, int height) {
		this.data = new BlockState[side * side * height];
		this.side2 = side * side;
		this.side = side;
		init();
	}
	
	protected abstract void init();
	
	protected int getIndex(int x, int y, int z) {
		return y * side2 + z * side + x;
	}
	
	protected void setBlockState(int x, int y, int z, BlockState state) {
		data[getIndex(x, y, z)] = state;
	}
	
	protected void setBlockState(int index, BlockState state) {
		data[index] = state;
	}
	
	protected void setOffset(int x, int y, int z) {
		offset.x = x;
		offset.y = y;
		offset.z = z;
	}
	
	@Override
	public boolean generate(Level level, Random rand, int x, int y, int z) {
		if (level.getTileId(x, y, z) != 0 || !BlockUtil.isTerrain(level.getTileId(x, y - 1, z))) {
			return false;
		}
		for (int i = 0; i < data.length; i++) {
			if (data[i] != null) {
				int px = (i % side) + x + offset.x;
				int pz = ((i / side) % side) + z + offset.z;
				int py = i / side2 + y + offset.y;
				data[i].setBlockFast(level, px, py, pz);
			}
		}
		return true;
	}
}
