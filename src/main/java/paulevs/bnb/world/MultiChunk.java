package paulevs.bnb.world;

import net.minecraft.class_257;
import net.minecraft.level.chunk.Chunk;

public class MultiChunk {
	private static final int CAPACITY = 16 * 16 * 128;
	private final byte blocks[] = new byte[CAPACITY];
	private final byte meta[] = new byte[CAPACITY];
	
	public MultiChunk(Chunk chunk) {
		for (int i = 0; i < CAPACITY; i++) {
			blocks[i] = chunk.tiles[i];
			meta[i] = fastMetaGet(chunk.field_957, i);
		}
	}
	
	public void fillChunk(Chunk chunk) {
		for (int i = 0; i < CAPACITY; i++) {
			if (blocks[i] > 0) {
				chunk.tiles[i] = blocks[i];
				fastMetaSet(chunk.field_957, i, meta[i]);
			}
		}
	}
	
	private byte fastMetaGet(class_257 metaSource, int index) {
		int fIndex = index >> 1;
		int cIndex = index & 1;
		return (byte) (cIndex == 0 ? metaSource.field_2103[fIndex] & 15 : metaSource.field_2103[fIndex] >> 4 & 15);
	}
	
	private void fastMetaSet(class_257 metaSource, int index, byte meta) {
		int fIndex = index >> 1;
		int cIndex = index & 1;
		if (cIndex == 0) {
			metaSource.field_2103[fIndex] = (byte) (metaSource.field_2103[fIndex] & 240 | meta & 15);
		}
		else {
			metaSource.field_2103[fIndex] = (byte) (metaSource.field_2103[fIndex] & 15 | (meta & 15) << 4);
		}
	}
}
