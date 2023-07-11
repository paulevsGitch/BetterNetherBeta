package paulevs.bnb.rendering;

import net.minecraft.level.biome.BaseBiome;
import net.minecraft.level.gen.BiomeSource;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.util.math.Vec3f;
import paulevs.bnb.world.biome.NetherBiome;

public class FogInfo {
	private static final Vec3f DEFAULT_COLOR = new Vec3f(0.2F, 0.03F, 0.03F);
	public static final float[] COLOR = new float[3];
	
	public static void setColor(float r, float g, float b) {
		COLOR[0] = r;
		COLOR[1] = g;
		COLOR[2] = b;
	}
	
	public static net.minecraft.util.maths.Vec3f getVector() {
		return net.minecraft.util.maths.Vec3f.getFromCacheAndSet(COLOR[0], COLOR[1], COLOR[2]);
	}
	
	private static Vec3f getFogColor(BaseBiome biome) {
		if (biome instanceof NetherBiome netherBiome) {
			return netherBiome.getFogColor();
		}
		return DEFAULT_COLOR;
	}
	
	public static void setColor(double x, double z, BiomeSource source) {
		double posX = x / 8.0;
		double posZ = z / 8.0;
		
		int x1 = MathHelper.floor(posX) << 3;
		int z1 = MathHelper.floor(posZ) << 3;
		int x2 = x1 + 8;
		int z2 = z1 + 8;
		
		float dx = (float) (x - x1) / 8.0F;
		float dz = (float) (z - z1) / 8.0F;
		
		Vec3f color1 = getFogColor(source.getBiome(x1, z1));
		Vec3f color2 = getFogColor(source.getBiome(x2, z1));
		Vec3f color3 = getFogColor(source.getBiome(x1, z2));
		Vec3f color4 = getFogColor(source.getBiome(x2, z2));
		
		COLOR[0] = interpolate2D(dx, dz, color1.getX(), color2.getX(), color3.getX(), color4.getX());
		COLOR[1] = interpolate2D(dx, dz, color1.getY(), color2.getY(), color3.getY(), color4.getY());
		COLOR[2] = interpolate2D(dx, dz, color1.getZ(), color2.getZ(), color3.getZ(), color4.getZ());
	}
	
	private static float interpolate2D(float dx, float dy, float v1, float v2, float v3, float v4) {
		return net.modificationstation.stationapi.api.util.math.MathHelper.interpolate2D(dx, dy, v1, v2, v3, v4);
	}
}
