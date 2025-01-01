package paulevs.bnb.world.generator.terrain;

@FunctionalInterface
public interface TerrainSDF {
	float getDensity(int x, int y, int z);
	
	default float getAndMixDensity(float density, int x, int y, int z, float scale) {
		return Math.max(density, getDensity(x, y, z) * scale);
	}
}
