package paulevs.bnb.noise;

import net.minecraft.util.maths.MathHelper;

import java.util.Arrays;

public class VoronoiNoise {
	private final int seed;
	
	public VoronoiNoise(int seed) {
		this.seed = seed;
	}
	
	public float getF1F3(float[] buffer, double x, double y, double z) {
		get(buffer, x, y, z);
		return buffer[0] / buffer[2];
	}
	
	public float get(float[] buffer, double x, double y, double z) {
		int x1 = MathHelper.floor(x);
		int y1 = MathHelper.floor(y);
		int z1 = MathHelper.floor(z);
		
		float sdx = (float) (x - x1);
		float sdy = (float) (y - y1);
		float sdz = (float) (z - z1);
		
		byte index = 0;
		
		for (byte i = -1; i < 2; i++) {
			for (byte j = -1; j < 2; j++) {
				for (byte k = -1; k < 2; k++) {
					float dx = (hash(x1 + i, y1 + j + seed +  5, z1 + k) % 3607) / 3607.0F * 0.5F + i - sdx;
					float dy = (hash(x1 + i, y1 + j + seed + 13, z1 + k) % 3607) / 3607.0F * 0.5F + j - sdy;
					float dz = (hash(x1 + i, y1 + j + seed + 23, z1 + k) % 3607) / 3607.0F * 0.5F + k - sdz;
					float distance = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
					buffer[index++] = distance;
				}
			}
		}
		
		Arrays.sort(buffer);
		return buffer[0];
	}
	
	private long hash(int x, int y, int z) {
		return net.modificationstation.stationapi.api.util.math.MathHelper.hashCode(x, y, z);
	}
}
