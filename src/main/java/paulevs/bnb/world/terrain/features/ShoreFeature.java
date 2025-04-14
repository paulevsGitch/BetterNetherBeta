package paulevs.bnb.world.terrain.features;

public class ShoreFeature extends TerrainFeature {
	@Override
	public float getDensity(int x, int y, int z) {
		float density = gradient(y, 96, 100, 1, -1);
		density = Math.max(density, gradient(y, 224, 256, -1, 1));
		return density;
	}
	
	@Override
	public void setSeed(int seed) {}
}
