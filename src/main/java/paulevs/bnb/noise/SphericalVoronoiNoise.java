package paulevs.bnb.noise;

import net.minecraft.util.maths.MathHelper;

public class SphericalVoronoiNoise extends VoronoiNoise {
	@Override
	public float get(double x, double y, double z) {
		float value = super.get(x, y, z);
		value = 1.0F - value * value;
		return MathHelper.sqrt(value);
	}
	
	@Override
	public float get(double x, double y) {
		float value = super.get(x, y);
		value = 1.0F - value * value;
		return MathHelper.sqrt(value);
	}
}
