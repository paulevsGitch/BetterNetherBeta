package paulevs.bnb.world.generator;

public enum BNBChunkStatus {
	EMPTY((byte) 0),
	TERRAIN((byte) 1),
	FINISHED((byte) 2);
	
	private static final BNBChunkStatus[] VALUES = values();
	public final byte id;
	
	BNBChunkStatus(byte id) {
		this.id = id;
	}
	
	public static BNBChunkStatus max(BNBChunkStatus a, BNBChunkStatus b) {
		return a.id >= b.id ? a : b;
	}
	
	public static BNBChunkStatus fromID(byte id) {
		return VALUES[id];
	}
}
