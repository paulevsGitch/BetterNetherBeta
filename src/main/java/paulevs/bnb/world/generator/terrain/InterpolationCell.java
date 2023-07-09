package paulevs.bnb.world.generator.terrain;

import net.modificationstation.stationapi.api.util.math.MathHelper;

public class InterpolationCell {
	private final float[] cell = new float[8];
	private final int[] offsets = new int[8];
	private final float[] data;
	private final int lastIndex;
	private final int cellSide;
	private final int side2;
	private final int side;
	private boolean empty;
	private float dx;
	private float dy;
	private float dz;
	private int x1;
	private int y1;
	private int z1;
	
	public InterpolationCell(int cellSide) {
		this(cellSide, 16 / cellSide + 1);
	}
	
	public InterpolationCell(int cellSide, int side) {
		this.cellSide = cellSide;
		this.side = side;
		side2 = side * side;
		lastIndex = side - 2;
		data = new float[side2 * side];
		for (byte i = 0; i < 8; i++) {
			int z = i & 1;
			int y = (i >> 1) & 1;
			int x = i >> 2;
			offsets[i] = getIndex(x, y, z);
		}
	}
	
	public void setX(int x) {
		dx = (float) x / cellSide;
		x1 = MathHelper.clamp((int) dx, 0, lastIndex);
		dx -= x1;
	}
	
	public void setY(int y) {
		dy = (float) y / cellSide;
		y1 = MathHelper.clamp((int) dy, 0, lastIndex);
		dy -= y1;
	}
	
	public void setZ(int z) {
		dz = (float) z / cellSide;
		z1 = MathHelper.clamp((int) dz, 0, lastIndex);
		dz -= z1;
	}
	
	public float get() {
		byte i;
		int index = getIndex(x1, y1, z1);
		for (i = 0; i < 8; i++) {
			cell[i] = data[index + offsets[i]];
		}
		for (i = 0; i < 4; i++) {
			index = i << 1;
			cell[i] = MathHelper.lerp(dz, cell[index], cell[index | 1]);
		}
		for (i = 0; i < 2; i++) {
			index = i << 1;
			cell[i] = MathHelper.lerp(dy, cell[index], cell[index | 1]);
		}
		return MathHelper.lerp(dx, cell[0], cell[1]);
	}
	
	public void fill(int x, int y, int z, TerrainSDF sdf) {
		int count = 0;
		for (byte dx = 0; dx < side; dx++) {
			int indexX = dx * side2;
			int px = x + dx * cellSide;
			for (byte dy = 0; dy < side; dy++) {
				int indexXY = indexX + dy * side;
				int py = y + dy * cellSide;
				for (byte dz = 0; dz < side; dz++) {
					int pz = z + dz * cellSide;
					float density = sdf.getDensity(px, py, pz);
					data[indexXY + dz] = density;
					if (density > 0.5F) count++;
				}
			}
		}
		empty = count == 0;
	}
	
	public boolean isEmpty() {
		return empty;
	}
	
	private int getIndex(int x, int y, int z) {
		return x * side2 + y * side + z;
	}
}
