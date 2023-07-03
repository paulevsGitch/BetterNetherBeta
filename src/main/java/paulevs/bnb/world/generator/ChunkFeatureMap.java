package paulevs.bnb.world.generator;

import net.minecraft.level.dimension.DimensionData;
import paulevs.bnb.world.generator.terrain.TerrainFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class ChunkFeatureMap implements TerrainSDF {
	private static final List<Supplier<TerrainFeature>> CONSTRUCTORS = new ArrayList<>();
	private static final TerrainFeature[][] FEATURES = new TerrainFeature[16][];
	private static final float[][] FEATURE_DENSITY = new float[72][];
	private static final TerrainMap TERRAIN_MAP = new TerrainMap();
	private static final Random RANDOM = new Random(0);
	private final int sectionIndex;
	private static int posX;
	private static int posZ;
	
	public ChunkFeatureMap(int sectionIndex) {
		this.sectionIndex = sectionIndex;
	}
	
	public static void setData(DimensionData dimensionData, int seed) {
		TERRAIN_MAP.setData(dimensionData, seed);
		final int count = CONSTRUCTORS.size();
		
		if (FEATURE_DENSITY[0] == null || FEATURE_DENSITY[0].length != count) {
			for (short i = 0; i < FEATURE_DENSITY.length; i++) {
				FEATURE_DENSITY[i] = new float[count];
			}
		}
		
		if (FEATURES[0] == null || FEATURES[0].length != count) {
			for (byte i = 0; i < 16; i++) {
				FEATURES[i] = new TerrainFeature[count];
				for (short j = 0; j < count; j++) {
					FEATURES[i][j] = CONSTRUCTORS.get(j).get();
				}
			}
		}
		
		for (TerrainFeature[] features : FEATURES) {
			RANDOM.setSeed(seed);
			for (TerrainFeature feature : features) {
				feature.setSeed(RANDOM.nextInt());
			}
		}
	}
	
	public static void addFeature(Supplier<TerrainFeature> constructor) {
		CONSTRUCTORS.add(constructor);
	}
	
	public static void prepare(int cx, int cz) {
		posX = cx << 4;
		posZ = cz << 4;
		
		int count = CONSTRUCTORS.size();
		
		for (short i = 0; i < 144; i++) {
			byte dx = (byte) (i / 12);
			byte dz = (byte) (i % 12);
			if ((dx + dz & 1) == 1) continue;
			dx = (byte) ((dx << 1) - 2);
			dz = (byte) ((dz << 1) - 2);
			TERRAIN_MAP.getDensity(dx + posX, dz + posZ, FEATURE_DENSITY[i >> 1], count);
		}
		
		/*for (byte x = -2; x < 22; x += 2) {
			for (byte z = -2; z < 22; z += 2) {
				short index = (short) ((x & 31) << 5 | (z & 31));
				TERRAIN_MAP.getDensity(x + posX, z + posZ, FEATURE_DENSITY[index], count);
			}
		}*/
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		short index = getIndex(x, z);
		float[] density = FEATURE_DENSITY[index];
		
		TerrainFeature[] sectionFeatures = FEATURES[sectionIndex];
		
		float result = 0;
		for (int i = 0; i < density.length; i++) {
			if (density[i] == 0) continue;
			result += sectionFeatures[i].getDensity(x, y, z) * density[i];
		}
		
		return result;
	}
	
	private short getIndex(int x, int z) {
		byte dx = (byte) ((x - posX + 2) >> 1);
		byte dz = (byte) ((z - posZ + 2) >> 1);
		return (short) ((dx * 12 + dz) >> 1);
	}
}
