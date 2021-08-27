package paulevs.bnb.util;

import net.minecraft.util.maths.Vec3f;
import paulevs.bnb.noise.OpenSimplexNoise;

import java.util.Random;

public class MHelper {
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(0);
	private static final Random RANDOM = new Random();
	
	public static int getRandomHash(int x, int z) {
		int h = x * 374761393 + z * 668265263;
		h = (h ^ (h >> 13)) * 1274126177;
		return h ^ (h >> 16);
	}
	
	public static int getRandomHash(int offset, int x, int y) {
		int h = offset + x * 374761393 + y * 668265263;
		h = (h ^ (h >> 13)) * 1274126177;
		return h ^ (h >> 16);
	}
	
	public static float getNoiseValue(double x, double y) {
		return (float) NOISE.eval(x, y);
	}
	
	public static float lerp(float start, float end, float delta) {
		return start + delta * (end - start);
	}
	
	public static void lerp(Vec3f result, Vec3f a, Vec3f b, float delta) {
		result.x = lerp((float) a.x, (float) b.x, delta);
		result.y = lerp((float) a.y, (float) b.y, delta);
		result.z = lerp((float) a.z, (float) b.z, delta);
	}
	
	public static int clamp(int val, int min, int max) {
		return val < min ? min : val > max ? max : val;
	}
	
	public static float clamp(float val, float min, float max) {
		return val < min ? min : val > max ? max : val;
	}
	
	public static float min(float a, float b) {
		return a < b ? a : b;
	}
	
	public static int min(int a, int b) {
		return a < b ? a : b;
	}
	
	public static int max(int a, int b) {
		return a > b ? a : b;
	}
	
	public static float max(float a, float b) {
		return a > b ? a : b;
	}
	
	public static float max(float a, float b, float c) {
		return max(a, max(b, c));
	}

	public static int abs(int i) {
		return i < 0 ? -i : i;
	}
	
	public static int randRange(int min, int max, Random random) {
		return min + random.nextInt(max - min + 1);
	}
	
	public static double randRange(double min, double max, Random random) {
		return min + random.nextDouble() * (max - min);
	}

	public static float randRange(float min, float max, Random random) {
		return min + random.nextFloat() * (max - min);
	}
	
	public static void shuffle(Object[] array, Random random) {
		Object obj;
		for (int i = 0; i < array.length; i++) {
			int i2 = random.nextInt(array.length);
			obj = array[i];
			array[i] = array[i2];
			array[i2] = obj;
		}
	}

	public static Random getRandom() {
		return RANDOM;
	}
	
	public static float smoothUnion(float a, float b, float delta) {
		float h = clamp(0.5F + 0.5F * (b - a) / delta, 0, 1);
		return lerp(b, a, h) - delta * h * (1 - h);
	}
	
	public static float smoothUnionInverted(float a, float b, float delta) {
		return -smoothUnion(-a, -b, delta);
	}
}
