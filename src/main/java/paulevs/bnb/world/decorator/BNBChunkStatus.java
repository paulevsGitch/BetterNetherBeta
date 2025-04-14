package paulevs.bnb.world.decorator;

public enum BNBChunkStatus {
	EMPTY((byte) 0),
	TERRAIN((byte) 1),
	POPULATION_BIG((byte) 2),
	FINISHED((byte) 3);
	
	private static final BNBChunkStatus[] VALUES = values();
	public final byte id;
	
	BNBChunkStatus(byte id) {
		this.id = id;
	}
	
	public BNBChunkStatus increment() {
		if (this == FINISHED) return this;
		return VALUES[id + 1];
	}
	
	public BNBChunkStatus decrement() {
		if (this == EMPTY) return this;
		return VALUES[id - 1];
	}
	
	public boolean isLessThan(BNBChunkStatus status) {
		return id < status.id;
	}
	
	public boolean isGreaterThan(BNBChunkStatus status) {
		return id > status.id;
	}
	
	public static BNBChunkStatus max(BNBChunkStatus a, BNBChunkStatus b) {
		return a.id >= b.id ? a : b;
	}
	
	public static BNBChunkStatus fromID(byte id) {
		return VALUES[id];
	}
}
