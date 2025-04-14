package paulevs.bnb.world.terrain;

@FunctionalInterface
public interface TerrainSDF {
	float getDensity(int x, int y, int z);
	
	default float blendDensity(float density, int x, int y, int z, float scale) {
		return Math.max(density, getDensity(x, y, z) * scale);
	}
}
