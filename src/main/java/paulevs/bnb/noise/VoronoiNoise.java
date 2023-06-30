package paulevs.bnb.noise;

import net.minecraft.util.maths.MathHelper;

import java.util.Arrays;

public class VoronoiNoise extends FloatNoise {
	private final float[] buffer = new float[27];
	private int seed;
	
	@Override
	public void setSeed(int seed) {
		this.seed = seed;
	}
	
	public float getF1F3(double x, double y, double z) {
		get(x, y, z);
		Arrays.sort(buffer);
		return MathHelper.sqrt(buffer[0] / buffer[2]);
	}
	
	@Override
	public float get(double x, double y, double z) {
		int x1 = MathHelper.floor(x);
		int y1 = MathHelper.floor(y);
		int z1 = MathHelper.floor(z);
		
		float sdx = (float) (x - x1);
		float sdy = (float) (y - y1);
		float sdz = (float) (z - z1);
		
		byte index = 0;
		float distance = 1000;
		
		for (byte i = -1; i < 2; i++) {
			for (byte j = -1; j < 2; j++) {
				for (byte k = -1; k < 2; k++) {
					float dx = wrap(hash(x1 + i, y1 + j + seed +  5, z1 + k), 3607) / 3607.0F * 0.7F + i - sdx;
					float dy = wrap(hash(x1 + i, y1 + j + seed + 13, z1 + k), 3607) / 3607.0F * 0.7F + j - sdy;
					float dz = wrap(hash(x1 + i, y1 + j + seed + 23, z1 + k), 3607) / 3607.0F * 0.7F + k - sdz;
					float d = dx * dx + dy * dy + dz * dz;
					if (d < distance) distance = d;
					buffer[index++] = d;
				}
			}
		}
		
		distance = MathHelper.sqrt(distance);
		return net.modificationstation.stationapi.api.util.math.MathHelper.clamp(distance, 0, 1);
	}
	
	@Override
	public float get(double x, double y) {
		int x1 = MathHelper.floor(x);
		int y1 = MathHelper.floor(y);
		
		float sdx = (float) (x - x1);
		float sdy = (float) (y - y1);
		
		// byte index = 0;
		float distance = 1000;
		
		for (byte i = -1; i < 2; i++) {
			for (byte j = -1; j < 2; j++) {
				float dx = wrap(hash(x1 + i, y1 + j, seed), 3607) / 3607.0F * 0.8F + i - sdx;
				float dy = wrap(hash(x1 + i, y1 + j, seed + 13), 3607) / 3607.0F * 0.8F + j - sdy;
				float d = dx * dx + dy * dy;
				if (d < distance) distance = d;
				// buffer[index++] = d;
			}
		}
		
		distance = MathHelper.sqrt(distance);
		return net.modificationstation.stationapi.api.util.math.MathHelper.clamp(distance, 0, 1);
	}
	
	public float getID(double x, double y) {
		int x1 = MathHelper.floor(x);
		int y1 = MathHelper.floor(y);
		
		float sdx = (float) (x - x1);
		float sdy = (float) (y - y1);
		
		float distance = 1000;
		
		int di = 0;
		int dj = 0;
		
		for (byte i = -1; i < 2; i++) {
			for (byte j = -1; j < 2; j++) {
				float dx = wrap(hash(x1 + i, y1 + j, seed), 3607) / 3607.0F * 0.8F + i - sdx;
				float dy = wrap(hash(x1 + i, y1 + j, seed + 13), 3607) / 3607.0F * 0.8F + j - sdy;
				float d = dx * dx + dy * dy;
				if (d < distance) {
					distance = d;
					di = i;
					dj = j;
				}
			}
		}
		
		return wrap(hash(x1 + di, y1 + dj, seed + 27), 3607) / 3607.0F;
	}
}
