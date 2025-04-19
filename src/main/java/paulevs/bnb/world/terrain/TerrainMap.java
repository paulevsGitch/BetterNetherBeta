package paulevs.bnb.world.terrain;

import it.unimi.dsi.fastutil.objects.Reference2FloatMap;
import net.minecraft.level.dimension.DimensionData;
import net.minecraft.util.maths.Vec2I;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNB;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.VoronoiNoise;
import paulevs.bnb.world.map.DataMap;
import paulevs.bnb.world.terrain.features.RiversFeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

public class TerrainMap extends DataMap<Identifier> {
	private static final Identifier DEFAULT_TERRAIN = BNB.id("plains");
	private static final Vec2I[] OFFSETS;
	private static final float MULTIPLIER;
	
	private final EnumMap<TerrainRegion, List<Identifier>> regionTerrain = new EnumMap<>(TerrainRegion.class);
	private final FractalNoise oceanNoise = new FractalNoise(PerlinNoise::new);
	private final FractalNoise mountainNoise = new FractalNoise(PerlinNoise::new);
	private final VoronoiNoise bridgesNoise = new VoronoiNoise();
	private final VoronoiNoise cellNoise = new VoronoiNoise();
	private final Random random = new Random(0);
	
	public TerrainMap() {
		super("bnb_terrain");
		oceanNoise.setOctaves(3);
		mountainNoise.setOctaves(2);
		Arrays.stream(TerrainRegion.values()).forEach(
			region -> regionTerrain.put(region, new ArrayList<>()
		));
	}
	
	@Override
	protected String serialize(Identifier value) {
		return value.toString();
	}
	
	@Override
	protected Identifier deserialize(String name) {
		return Identifier.of(name);
	}
	
	@Override
	public Identifier generateData(int x, int z) {
		TerrainRegion region = getRegionInternal(x, z);
		List<Identifier> list = regionTerrain.get(region);
		if (list.isEmpty()) return DEFAULT_TERRAIN;
		int index = (int) Math.floor(cellNoise.getID(x * 0.1, z * 0.1) * list.size());
		return list.get(index);
	}
	
	@Override
	public void setData(DimensionData data, int seed) {
		super.setData(data, seed);
		oceanNoise.setSeed(random.nextInt());
		mountainNoise.setSeed(random.nextInt());
		bridgesNoise.setSeed(random.nextInt());
		cellNoise.setSeed(random.nextInt());
	}
	
	public void addTerrain(Identifier terrainID, TerrainRegion region) {
		regionTerrain.get(region).add(terrainID);
	}
	
	public void getDensity(int x, int z, Reference2FloatMap<Identifier> data) {
		data.clear();
		for (Vec2I offset : OFFSETS) {
			Identifier sdf = getData(x + offset.x, z + offset.z);
			float value = data.getOrDefault(sdf, 0.0F) + MULTIPLIER;
			data.put(sdf, value);
		}
	}
	
	public TerrainRegion getRegion(int x, int z) {
		double preX = (COS * x - SIN * z) / 16.0 + distortionX.get(x * 0.03, z * 0.03) * 1.5F;
		double preZ = (SIN * x + COS * z) / 16.0 + distortionX.get(x * 0.03, z * 0.03) * 1.5F;
		
		int px = (int) Math.floor(preX);
		int pz = (int) Math.floor(preZ);
		
		float dx = (float) (preX - px);
		float dz = (float) (preZ - pz);
		
		TerrainRegion a = getRegionInternal(px, pz);
		
		if (dx < 0.333F && dz < 0.333F) {
			TerrainRegion b = getRegionInternal(px - 1, pz - 1);
			TerrainRegion c = getRegionInternal(px - 1, pz);
			TerrainRegion d = getRegionInternal(px, pz - 1);
			if (b == c && c == d) {
				float v = dx + dz;
				return v < 0.333F ? c : a;
			}
		}
		
		if (dx > 0.666F && dz < 0.333F) {
			TerrainRegion b = getRegionInternal(px + 1, pz - 1);
			TerrainRegion c = getRegionInternal(px + 1, pz);
			TerrainRegion d = getRegionInternal(px, pz - 1);
			if (b == c && c == d) {
				float v = (1.0F - dx) + dz;
				return v < 0.333F ? c : a;
			}
		}
		
		if (dx < 0.333F && dz > 0.666F) {
			TerrainRegion b = getRegionInternal(px - 1, pz + 1);
			TerrainRegion c = getRegionInternal(px - 1, pz);
			TerrainRegion d = getRegionInternal(px, pz + 1);
			if (b == c && c == d) {
				float v = dx + (1.0F - dz);
				return v < 0.333F ? c : a;
			}
		}
		
		if (dx > 0.666F && dz > 0.666F) {
			TerrainRegion b = getRegionInternal(px + 1, pz + 1);
			TerrainRegion c = getRegionInternal(px + 1, pz);
			TerrainRegion d = getRegionInternal(px, pz + 1);
			if (b == c && c == d) {
				float v = (1.0F - dx) + (1.0F - dz);
				return v < 0.333F ? c : a;
			}
		}
		
		return a;
	}
	
	public TerrainRegion getRegionInternal(int x, int z) {
		// if (RiversFeature.isRiverRegion(x, z)) return TerrainRegion.RIVERS;
		float ocean = oceanNoise.get(x * 0.0375, z * 0.0375);
		float mountains = mountainNoise.get(x * 0.075, z * 0.075);
		if (ocean > 0.5F) {
			//double px = x * 0.03 * 0.75 + distortionX.get(x * 0.015, z * 0.015);
			//double pz = z * 0.03 * 0.75 + distortionZ.get(x * 0.015, z * 0.015);
			//float bridges = bridgesNoise.getF1F2(px, pz);
			//if (bridges > 0.9) return TerrainRegion.BRIDGES;
			if (
				oceanNoise.get((x + 1) * 0.0375, z * 0.0375) < 0.5F ||
				oceanNoise.get((x - 1) * 0.0375, z * 0.0375) < 0.5F ||
				oceanNoise.get(x * 0.0375, (z + 1) * 0.0375) < 0.5F ||
				oceanNoise.get(x * 0.0375, (z - 1) * 0.0375) < 0.5F
			) {
				double px = 16.0 * (x * COS + z * SIN);
				double pz = 16.0 * (z * COS - x * SIN);
				float dx = distortionX.get(px * 0.03, pz * 0.03) * 1.5F;
				float dz = distortionZ.get(px * 0.03, pz * 0.03) * 1.5F;
				px -= dx;
				pz -= dz;
				if (RiversFeature.isRiverRegion(px, pz)) return TerrainRegion.RIVERS;
				return mountains > 0.6F ? TerrainRegion.SHORE_MOUNTAINS : TerrainRegion.SHORE_NORMAL;
			}
			if (ocean < 0.63F) return TerrainRegion.OCEAN_NORMAL;
			return mountains > 0.4F ? TerrainRegion.OCEAN_MOUNTAINS : TerrainRegion.OCEAN_NORMAL;
		}
		double px = 16.0 * (x * COS + z * SIN);
		double pz = 16.0 * (z * COS - x * SIN);
		float dx = distortionX.get(px * 0.03, pz * 0.03) * 1.5F;
		float dz = distortionZ.get(px * 0.03, pz * 0.03) * 1.5F;
		px -= dx;
		pz -= dz;
		if (RiversFeature.isRiverRegion(px, pz)) return TerrainRegion.RIVERS;
		return mountains > 0.6F ? TerrainRegion.MOUNTAINS : mountains > 0.53F ? TerrainRegion.HILLS : TerrainRegion.PLAINS;
	}
	
	static {
		int radius = 5;
		List<Vec2I> offsets = new ArrayList<>();
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				if (x * x + z * z <= radius) {
					offsets.add(new Vec2I(x << 2, z << 2));
				}
			}
		}
		OFFSETS = offsets.toArray(Vec2I[]::new);
		MULTIPLIER = 1F / OFFSETS.length;
	}
}
