package paulevs.bnb.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class LocalHeightCache {
	private static final short[] OUTPUT = new short[2];
	private final short[] heightData = new short[8192];
	
	public final void setData(int x, int y, short min, short max) {
		int index = getIndex(x, y);
		heightData[index] = min;
		heightData[index | 1] = max;
	}
	
	public final short[] getData(int x, int y) {
		int index = getIndex(x, y);
		OUTPUT[0] = heightData[index];
		OUTPUT[1] = heightData[index | 1];
		return OUTPUT;
	}
	
	private static int getIndex(int x, int y) {
		return ((x & 63) << 6 | y & 63) << 1;
	}
}
