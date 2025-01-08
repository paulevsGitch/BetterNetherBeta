package paulevs.bnb.world.generator;

public interface BNBWorldChunk {
	void bnb_setStatus(BNBChunkStatus status);
	BNBChunkStatus bnb_getStatus();
	
	static BNBWorldChunk cast(Object object) {
		return (BNBWorldChunk) object;
	}
}
