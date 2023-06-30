package paulevs.bnb.world.generator;

@FunctionalInterface
public interface TerrainSDF {
	float getDensity(int x, int y, int z);
}
