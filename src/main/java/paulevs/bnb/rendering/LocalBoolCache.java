package paulevs.bnb.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class LocalBoolCache {
	private final boolean[] data = new boolean[4096];
	
	public final void setData(int x, int y, boolean value) {
		data[getIndex(x, y)] = value;
	}
	
	public final boolean getData(int x, int y) {
		return data[getIndex(x, y)];
	}
	
	private static int getIndex(int x, int y) {
		return (x & 63) << 6 | y & 63;
	}
}
