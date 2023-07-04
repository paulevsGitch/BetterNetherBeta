package paulevs.bnb.world.generator.terrain;

import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.VoronoiNoise;

public class PancakesFeature extends TerrainFeature {
	private final FractalNoise ceilingSpikes = new FractalNoise(PerlinNoise::new);
	private final FractalNoise floorSpikes = new FractalNoise(PerlinNoise::new);
	private final FractalNoise distortionX = new FractalNoise(PerlinNoise::new);
	private final FractalNoise distortionZ = new FractalNoise(PerlinNoise::new);
	private final FractalNoise terrain = new FractalNoise(PerlinNoise::new);
	private final VoronoiNoise cells = new VoronoiNoise();
	
	public PancakesFeature() {
		ceilingSpikes.setOctaves(3);
		floorSpikes.setOctaves(3);
		distortionX.setOctaves(3);
		distortionZ.setOctaves(3);
		terrain.setOctaves(3);
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float dx = distortionX.get(x * 0.01, z * 0.01) * 0.5F;
		float dz = distortionZ.get(x * 0.01, z * 0.01) * 0.5F;
		
		/*float density = gradient(y, 0, 200, 0, 31.4F);
		density = MathHelper.sin(density);
		density -= terrain.get(x * 0.01F, y * 0.01F, z * 0.01F) * 0.1F;
		
		float cellSize = gradient(y, 0, 200, 0, 1.0F);
		cellSize = cellSize * cellSize * 0.25F;
		cellSize += cells.get(x * 0.01F + dx, z * 0.01F + dz) + cellSize;
		density -= cellSize * 0.7F;*/
		
		/*float connections = cells.getF1F3(x * 0.1F, y * 0.1F, z * 0.1F) - 0.2F;
		connections -= cellSize;
		density = smoothMax(density, connections, 1);*/
		
		//float connections = gradient(y, 0, 200, 0, 31.4F);
		//connections = MathHelper.sin(connections) * 0.1F + 0.5F;
		//if (connections > 0.5F) connections = 0;
		//else connections += 1.5F;
		
		//float connections = 0.6F - cellSize * 0.2F;
		//connections *= cells.getF1F3(x * 0.04F, y * 0.04F, z * 0.04F) + 0.2F;
		//if (connections < 0.5F) connections = (connections - 0.5F) * 5F + 0.5F;
		
		/*float connections = cells.getF1F3(x * 0.04F, y * 0.04F, z * 0.04F);
		//cellSize = 0.6F - cellSize * 0.3F;
		//if (cellSize < 0.5F) connections *= 0;
		//connections *= 1.0F - cellSize;
		connections -= cellSize * 0.5F;
		density = smoothMax(density, connections, 0.5F);
		
		float ceil = MathHelper.cos(ceilingSpikes.get(x * 0.02, z * 0.02) * PI_HALF) * 0.5F + 0.5F;
		ceil = ceil * ceil * ceil;
		ceil += gradient(y, 200, 256, -1.0F, 0.5F);
		density = Math.max(density, ceil);
		
		return density;*/
		
		float height = cells.getID(x * 0.01F + dx, z * 0.01F + dz);
		int count = (int) (height * 5 + 3);
		height = count * 25.4F;
		
		float density = gradient(y, 0, height, 0.0F, 1.0F);
		float sin = MathHelper.sin(density * count * PI);
		sin = (float) Math.pow(sin, 8);
		density *= density;
		float cellSize = 0.8F - cells.get(x * 0.01F + dx, z * 0.01F + dz);
		float central = cellSize * 0.9F - density * 0.2F;// + 0.5F;
		density = sin * 0.7F + cellSize - density * 0.5F - 0.3F;
		
		float connections = cells.getF1F3(x * 0.05F, y * 0.05F, z * 0.05F) - 0.1F;
		if (central < 0.5F) connections -= (0.5F - central) * 8F;
		density = smoothMax(density, connections, 1F);
		
		central = gradient(y, height, height + 10, 0.0F, 1.0F);
		central *= central;
		central = cellSize * 0.7F - central;
		density = smoothMax(density, central, 0.5F);
		
		density -= gradient(y, height, height + 20, 0, 10.0F);
		
		float ceil = MathHelper.cos(ceilingSpikes.get(x * 0.02, z * 0.02) * PI_HALF) * 0.5F + 0.5F;
		ceil = ceil * ceil * ceil;
		ceil += gradient(y, 200, 256, -1.0F, 0.5F);
		density = Math.max(density, ceil);
		
		float floor = MathHelper.cos(floorSpikes.get(x * 0.02, z * 0.02) * PI_HALF) * 0.5F + 0.5F;
		floor = floor * floor * floor;
		floor += gradient(y, 0, 60, 1.0F, -1.0F);
		density = smoothMax(density, floor, 0.5F);
		
		return density;
	}
	
	@Override
	public void setSeed(int seed) {
		RANDOM.setSeed(seed);
		ceilingSpikes.setSeed(RANDOM.nextInt());
		floorSpikes.setSeed(RANDOM.nextInt());
		distortionX.setSeed(RANDOM.nextInt());
		distortionZ.setSeed(RANDOM.nextInt());
		terrain.setSeed(RANDOM.nextInt());
		cells.setSeed(RANDOM.nextInt());
	}
}
