package paulevs.bnb.world.terrain.features;

public class CavesFeature extends TerrainFeature {
	public CavesFeature() {}
	
	@Override
	public float getDensity(int x, int y, int z) {
		return y < 70 ? -1.0F : 2.0F;
	}
	
	@Override
	public void setSeed(int seed) {}
	
	@Override
	public float blendDensity(float density, int x, int y, int z, float scale) {
		return density < 0.75F ? density : Math.min(density, getDensity(x, y, z));
	}
}
