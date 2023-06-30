package paulevs.bnb.world.generator.terrain;

import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.world.generator.TerrainSDF;

import java.util.Random;

public abstract class TerrainFeature implements TerrainSDF {
	protected static final Random RANDOM = new Random(0);
	
	public abstract void setSeed(int seed);
	
	protected float gradient(float y, float minY, float maxY, float minValue, float maxValue) {
		if (y <= minY) return minValue;
		if (y >= maxY) return maxValue;
		return MathHelper.lerp((y - minY) / (maxY - minY), minValue, maxValue);
	}
}
