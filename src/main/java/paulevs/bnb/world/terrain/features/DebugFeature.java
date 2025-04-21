package paulevs.bnb.world.terrain.features;

public class DebugFeature extends TerrainFeature {
	public DebugFeature() {}
	
	@Override
	public float getDensity(int x, int y, int z) {
		return y < 70 ? 1.0F : -1.0F;
	}
	
	@Override
	public void setSeed(int seed) {}
}
