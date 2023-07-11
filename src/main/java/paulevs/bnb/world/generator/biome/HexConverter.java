package paulevs.bnb.world.generator.biome;

public class HexConverter {
	private int cellX;
	private int cellZ;
	
	public void update(double x, double z) {
		x *= 0.866025;
		cellZ = net.minecraft.util.maths.MathHelper.floor(z);
		boolean offset = (cellZ & 1) == 0;
		if (offset) x += 0.5;
		cellX = net.minecraft.util.maths.MathHelper.floor(x);
		
		float dz = (float) (z - cellZ);
		if (dz < 0.1666F) {
			float dx = (float) (x - cellX);
			float corner = Math.abs(dx - 0.5F) * 0.6666F - 0.1666F;
			if (dz < corner) {
				if (offset && dx < 0.5F) cellX--;
				if (!offset && dx > 0.5F) cellX++;
				cellZ--;
			}
		}
		else if (dz > 0.8333F) {
			float dx = (float) (x - cellX);
			float corner = 1F - Math.abs(dx - 0.5F) * 0.6666F + 0.1666F;
			if (dz > corner) {
				if (offset && dx < 0.5F) cellX--;
				if (!offset && dx > 0.5F) cellX++;
				cellZ++;
			}
		}
	}
	
	public int getCellX() {
		return cellX;
	}
	
	public int getCellZ() {
		return cellZ;
	}
}
