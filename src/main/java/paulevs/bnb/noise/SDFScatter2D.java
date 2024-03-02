package paulevs.bnb.noise;

import net.minecraft.util.maths.MathHelper;
import net.minecraft.util.maths.Vec3D;

import java.util.function.BiFunction;

public class SDFScatter2D extends FloatNoise {
	private final Vec3D pos = Vec3D.make(0, 0, 0);
	private final BiFunction<Integer, Vec3D, Float> sdf;
	private int seed;
	
	public SDFScatter2D(BiFunction<Integer, Vec3D, Float> sdf) {
		this.sdf = sdf;
	}
	
	@Override
	public float get(double x, double y) {
		return 0;
	}
	
	@Override
	public float get(double x, double y, double z) {
		int x1 = MathHelper.floor(x);
		int z1 = MathHelper.floor(z);
		
		float sdx = (float) (x - x1);
		float sdz = (float) (z - z1);
		
		float distance = -1000;
		
		for (byte i = -1; i < 2; i++) {
			for (byte k = -1; k < 2; k++) {
				pos.x = wrap(hash(x1 + i, z1 + k, seed), 3607) / 3607.0F * 0.7F + i - sdx;
				pos.z = wrap(hash(x1 + i, z1 + k, seed + 23), 3607) / 3607.0F * 0.7F + k - sdz;
				pos.y = y;
				int featureSeed = wrap(hash(x1 + i, z1 + k, seed + 157), 378632);
				float d = sdf.apply(featureSeed, pos);
				if (d > distance) distance = d;
			}
		}
		
		return distance;
	}
	
	@Override
	public void setSeed(int seed) {
		this.seed = seed;
	}
}
