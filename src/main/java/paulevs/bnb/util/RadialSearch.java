package paulevs.bnb.util;

import net.minecraft.util.Vec3i;
import net.minecraft.util.maths.MathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RadialSearch {
	private static final float[] DISTANCES;
	private static final Vec3i[] OFFSETS;
	private static final Vec3i SEARCH;
	
	public static void init() {}
	
	public static float search(int x, int y, int z, Function<Vec3i, Boolean> testFunction) {
		for (int i = 0; i < OFFSETS.length; i++) {
			Vec3i p = OFFSETS[i];
			SEARCH.x = x + p.x;
			SEARCH.y = y + p.y;
			SEARCH.z = x + p.z;
			if (testFunction.apply(SEARCH)) {
				return DISTANCES[i];
			}
		}
		return Float.MAX_VALUE;
	}
	
	public static float search(int x, int y, int z, Function<Vec3i, Boolean> testFunction, int maxDistance) {
		for (int i = 0; i < OFFSETS.length && DISTANCES[i] <= maxDistance; i++) {
			Vec3i p = OFFSETS[i];
			SEARCH.x = x + p.x;
			SEARCH.y = y + p.y;
			SEARCH.z = z + p.z;
			if (testFunction.apply(SEARCH)) {
				return DISTANCES[i];
			}
		}
		return Float.MAX_VALUE;
	}
	
	static {
		List<Vec3i> offsets = new ArrayList<>();
		for (int x = -16; x < 17; x++) {
			int x2 = x * x;
			for (int y = -16; y < 17; y++) {
				int y2 = y * y;
				for (int z = -16; z < 17; z++) {
					int z2 = z * z;
					if (x2 + y2 + z2 <= 256) {
						offsets.add(new Vec3i(x, y, z));
					}
				}
			}
		}
		offsets.sort((p1, p2) -> {
			int l1 = p1.x * p1.x + p1.y * p1.y + p1.z * p1.z;
			int l2 = p2.x * p2.x + p2.y * p2.y + p2.z * p2.z;
			return Long.compare(l1, l2);
		});
		OFFSETS = offsets.toArray(new Vec3i[offsets.size()]);
		DISTANCES = new float[OFFSETS.length];
		for (int i = 0; i < OFFSETS.length; i++) {
			Vec3i p = OFFSETS[i];
			DISTANCES[i] = MathHelper.sqrt(p.x * p.x + p.y * p.y + p.z * p.z);
		}
		SEARCH = new Vec3i();
	}
}
