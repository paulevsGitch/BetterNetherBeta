package paulevs.bnb.util;

public enum BlockDirection {
	NEG_X(4, -1, 0, 0),
	NEG_Y(0, 0, -1, 0),
	NEG_Z(2, 0, 0, -1),
	POS_X(5, 1, 0, 0),
	POS_Y(1, 0, 1, 0),
	POS_Z(3, 0, 0, 1);
	
	private final int facing;
	private final int x;
	private final int y;
	private final int z;
	
	BlockDirection(int facing, int dx, int dy, int dz) {
		this.facing = facing;
		this.x = dx;
		this.y = dy;
		this.z = dz;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
	public BlockDirection invert() {
		switch (this) {
			case NEG_X: return POS_X;
			case NEG_Y: return POS_Y;
			case NEG_Z: return POS_Z;
			case POS_X: return NEG_X;
			case POS_Y: return NEG_Y;
			case POS_Z: return NEG_Z;
			default: return POS_Y;
		}
	}
	
	public static BlockDirection fromFacing(int facing) {
		switch (facing) {
			case 0: return NEG_Y;
			case 1: return POS_Y;
			case 2: return NEG_Z;
			case 3: return POS_Z;
			case 4: return NEG_X;
			case 5: return POS_X;
		}
		return POS_Y;
	}

	public int getFacing() {
		return facing;
	}
	
	public BlockAxis getAxis() {
		switch (this) {
			case POS_X:
			case NEG_X:
				return BlockAxis.AXIS_X;
			case POS_Y:
			case NEG_Y:
				return BlockAxis.AXIS_Y;
			case POS_Z:
			case NEG_Z:
				return BlockAxis.AXIS_Z;
			default: return BlockAxis.AXIS_Y;
		}
	}
}
