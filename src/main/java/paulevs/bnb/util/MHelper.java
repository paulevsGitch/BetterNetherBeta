package paulevs.bnb.util;

import net.minecraft.util.maths.Vec3f;

public class MHelper {
	public static int getSeed(int x, int z) {
		int h = x * 374761393 + z * 668265263;
		h = (h ^ (h >> 13)) * 1274126177;
		return h ^ (h >> 16);
	}
	
	public static int getSeed(int seed, int x, int y) {
		int h = seed + x * 374761393 + y * 668265263;
		h = (h ^ (h >> 13)) * 1274126177;
		return h ^ (h >> 16);
	}
	
	public static float lerp(float start, float end, float delta) {
		return start + delta * (end - start);
	}
	
	public static void lerp(Vec3f result, Vec3f a, Vec3f b, float delta) {
		result.x = lerp((float) a.x, (float) b.x, delta);
		result.y = lerp((float) a.y, (float) b.y, delta);
		result.z = lerp((float) a.z, (float) b.z, delta);
	}
}
