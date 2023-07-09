package paulevs.bnb.world.generator.terrain;

@FunctionalInterface
public interface TerrainSDF {
	float getDensity(int x, int y, int z);
}
