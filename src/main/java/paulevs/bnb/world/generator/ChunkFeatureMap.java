package paulevs.bnb.world.generator;

import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.world.generator.terrain.TerrainFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class ChunkFeatureMap implements TerrainSDF {
	private static final List<Supplier<TerrainFeature>> CONSTRUCTORS = new ArrayList<>();
	private static final List<List<TerrainFeature>> FEATURES = new ArrayList<>();
	private static final Map<Short, float[]> FEATURE_DENSITY = new HashMap<>();
	private static final Map<Short, float[]> DENSITY_CACHE = new HashMap<>();
	private static final TerrainMap TERRAIN_MAP = new TerrainMap();
	private static final Random RANDOM = new Random(0);
	private static int featureSeed;
	
	public static void setSeed(int seed) {
		TERRAIN_MAP.setSeed(seed);
		final int count = CONSTRUCTORS.size();
		FEATURE_DENSITY.keySet().forEach(index -> {
			float[] density = FEATURE_DENSITY.get(index);
			if (density.length != count) {
				density = new float[count];
				FEATURE_DENSITY.put(index, density);
			}
		});
		featureSeed = seed;
		FEATURES.forEach(list -> {
			RANDOM.setSeed(featureSeed);
			list.forEach(feature -> feature.setSeed(RANDOM.nextInt()));
		});
	}
	
	public static void addFeature(Supplier<TerrainFeature> constructor) {
		CONSTRUCTORS.add(constructor);
	}
	
	public static void prepare() {
		FEATURE_DENSITY.clear();
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float[] density;
		short index = (short) ((x & 31) << 5 | (z & 31));
		
		synchronized (FEATURE_DENSITY) {
			density = FEATURE_DENSITY.get(index);
			if (density == null) {
				synchronized (DENSITY_CACHE) {
					density = DENSITY_CACHE.computeIfAbsent(index, k -> new float[CONSTRUCTORS.size()]);
				}
				FEATURE_DENSITY.put(index, density);
				synchronized (TERRAIN_MAP) {
					TERRAIN_MAP.getDensity(x, z, density, CONSTRUCTORS.size());
				}
			}
		}
		
		int section = MathHelper.clamp(y >> 4, 0, 15);
		List<TerrainFeature> sectionFeatures;
		synchronized (FEATURES) {
			if (section >= FEATURES.size())	{
				synchronized (CONSTRUCTORS) {
					sectionFeatures = CONSTRUCTORS.stream().map(Supplier::get).toList();
					FEATURES.add(sectionFeatures);
					RANDOM.setSeed(featureSeed);
					sectionFeatures.forEach(feature -> feature.setSeed(RANDOM.nextInt()));
				}
			}
			else {
				sectionFeatures = FEATURES.get(section);
			}
		}
		
		float result = 0;
		for (int i = 0; i < density.length; i++) {
			if (density[i] == 0) continue;
			result += sectionFeatures.get(i).getDensity(x, y, z) * density[i];
		}
		
		return result;
	}
}
