package paulevs.bnb.noise;

import net.modificationstation.stationapi.api.util.math.MathHelper;

public class PerlinNoise extends FloatNoise {
	private final float[][] cell = new float[8][3];
	private byte usageType;
	private int lastX;
	private int lastY;
	private int lastZ;
	private int seed;
	
	@Override
	public void setSeed(int seed) {
		this.seed = seed;
	}
	
	@Override
	public float get(double x, double y) {
		int ix = (int) Math.floor(x);
		int iy = (int) Math.floor(y);
		
		if (usageType != 1 || lastX != ix || lastY != iy) {
			lastX = ix;
			lastY = iy;
			usageType = 1;
			fillVector(cell[0], ix, iy);
			fillVector(cell[1], ix + 1, iy);
			fillVector(cell[2], ix, iy + 1);
			fillVector(cell[3], ix + 1, iy + 1);
		}
		
		float dx = (float) (x - ix);
		float dy = (float) (y - iy);
		
		float a = dot(cell[0], dx, dy);
		float b = dot(cell[1], dx - 1, dy);
		float c = dot(cell[2], dx, dy - 1);
		float d = dot(cell[3], dx - 1, dy - 1);
		
		dx = smoothStep(dx);
		dy = smoothStep(dy);
		
		a = MathHelper.lerp(dx, a, b);
		b = MathHelper.lerp(dx, c, d);
		a = MathHelper.lerp(dy, a, b);
		
		return MathHelper.clamp(a / 1.414F + 0.5F, 0.0F, 1.0F);
	}
	
	@Override
	public float get(double x, double y, double z) {
		int ix = (int) Math.floor(x);
		int iy = (int) Math.floor(y);
		int iz = (int) Math.floor(z);
		
		if (usageType != 2 || lastX != ix || lastY != iy || lastZ != iz) {
			lastX = ix;
			lastY = iy;
			lastZ = iz;
			usageType = 2;
			fillVector(cell[0], ix, iy, iz);
			fillVector(cell[1], ix + 1, iy, iz);
			fillVector(cell[2], ix, iy + 1, iz);
			fillVector(cell[3], ix + 1, iy + 1, iz);
			fillVector(cell[4], ix, iy, iz + 1);
			fillVector(cell[5], ix + 1, iy, iz + 1);
			fillVector(cell[6], ix, iy + 1, iz + 1);
			fillVector(cell[7], ix + 1, iy + 1, iz + 1);
		}
		
		float dx = (float) (x - ix);
		float dy = (float) (y - iy);
		float dz = (float) (z - iz);
		
		float a = dot(cell[0], dx, dy, dz);
		float b = dot(cell[1], dx - 1, dy, dz);
		float c = dot(cell[2], dx, dy - 1, dz);
		float d = dot(cell[3], dx - 1, dy - 1, dz);
		float e = dot(cell[4], dx, dy, dz - 1);
		float f = dot(cell[5], dx - 1, dy, dz - 1);
		float g = dot(cell[6], dx, dy - 1, dz - 1);
		float h = dot(cell[7], dx - 1, dy - 1, dz - 1);
		
		dx = smoothStep(dx);
		dy = smoothStep(dy);
		dz = smoothStep(dz);
		
		a = MathHelper.lerp(dx, a, b);
		b = MathHelper.lerp(dx, c, d);
		c = MathHelper.lerp(dx, e, f);
		d = MathHelper.lerp(dx, g, h);
		
		a = MathHelper.lerp(dy, a, b);
		b = MathHelper.lerp(dy, c, d);
		
		a = MathHelper.lerp(dz, a, b);
		
		return MathHelper.clamp(a / 1.732F + 0.5F, 0.0F, 1.0F);
	}
	
	private void fillVector(float[] vector, int x, int y, int z) {
		vector[0] = wrap(hash(x, y + seed +  5, z), 3607) / 3607.0F - 0.5F;
		vector[1] = wrap(hash(x, y + seed + 13, z), 3607) / 3607.0F - 0.5F;
		vector[2] = wrap(hash(x, y + seed + 23, z), 3607) / 3607.0F - 0.5F;
		float length = vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2];
		if (length > 0) {
			length = MathHelper.sqrt(length);
			vector[0] /= length;
			vector[1] /= length;
			vector[2] /= length;
		}
	}
	
	private void fillVector(float[] vector, int x, int y) {
		float angle = wrap(hash(x, seed, y), 3607) / 1803.5F * (float) Math.PI;
		vector[0] = net.minecraft.util.maths.MathHelper.sin(angle);
		vector[1] = net.minecraft.util.maths.MathHelper.cos(angle);
	}
	
	private float smoothStep(float x) {
		return x * x * x * (x * (x * 6 - 15) + 10);
	}
	
	private float dot(float[] a, float x, float y, float z) {
		return a[0] * x + a[1] * y + a[2] * z;
	}
	
	private float dot(float[] a, float x, float y) {
		return a[0] * x + a[1] * y;
	}
}
