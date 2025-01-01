package paulevs.bnb.world.generator.terrain;

import it.unimi.dsi.fastutil.objects.Reference2FloatMap;
import it.unimi.dsi.fastutil.objects.Reference2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.world.generator.BNBWorldGenerator;
import paulevs.bnb.world.generator.terrain.features.TerrainFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ChunkTerrainMap implements TerrainSDF {
	private static final Reference2ObjectMap<Identifier, Supplier<TerrainFeature>> CONSTRUCTORS = new Reference2ObjectOpenHashMap<>();
	private static final List<Supplier<TerrainFeature>> COMMON_CONSTRUCTORS = new ArrayList<>();
	
	private final Reference2ObjectMap<Identifier, TerrainFeature> features = new Reference2ObjectOpenHashMap<>();
	private final List<TerrainFeature> commonFeatures = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	private static final Reference2FloatMap<Identifier>[] FEATURE_DENSITY = new Reference2FloatMap[32];
	
	private static int posX;
	private static int posZ;
	
	public ChunkTerrainMap() {
		CONSTRUCTORS.forEach((id, constructor) ->
			features.put(id, constructor.get())
		);
		
		COMMON_CONSTRUCTORS.forEach((constructor) ->
			commonFeatures.add(constructor.get())
		);
	}
	
	public void setSeed(int seed) {
		features.forEach((id, feature) ->
			feature.setSeed(seed + id.hashCode())
		);
		
		commonFeatures.forEach((feature) ->
			feature.setSeed(seed)
		);
	}
	
	public static void prepare(int x, int z) {
		posX = x;
		posZ = z;
		TerrainMap map = BNBWorldGenerator.getMapCopy();
		for (byte i = 0; i < 64; i++) {
			byte dx = (byte) (i / 8);
			byte dz = (byte) (i % 8);
			if ((dx + dz & 1) == 1) continue;
			dx = (byte) ((dx << 2) - 4);
			dz = (byte) ((dz << 2) - 4);
			Reference2FloatMap<Identifier> density = FEATURE_DENSITY[i >> 1];
			map.getDensity(dx + x, dz + z, density);
		}
	}
	
	@Override
	public float getDensity(int x, int y, int z) {
		float result = -100.0F;
		
		Reference2FloatMap<Identifier> density = FEATURE_DENSITY[getIndex(x, z)];
		for (Identifier id : density.keySet()) {
			result = features.get(id).getAndMixDensity(result, x, y, z, density.getFloat(id));
		}
		
		for (TerrainFeature feature : commonFeatures) {
			result = feature.getAndMixDensity(result, x, y, z, 1.0F);
		}
		
		return result;
	}
	
	public static void addFeature(Identifier id, Supplier<TerrainFeature> constructor) {
		CONSTRUCTORS.put(id, constructor);
	}
	
	public static void addCommonFeature(Supplier<TerrainFeature> constructor) {
		COMMON_CONSTRUCTORS.add(constructor);
	}
	
	private static int getIndex(int x, int z) {
		int dx = ((x - posX + 4) >> 2);
		int dz = ((z - posZ + 4) >> 2);
		return ((dx * 8 + dz) >> 1);
	}
	
	static {
		for (byte i = 0; i < FEATURE_DENSITY.length; i++) {
			FEATURE_DENSITY[i] = new Reference2FloatOpenHashMap<>();
		}
	}
}
