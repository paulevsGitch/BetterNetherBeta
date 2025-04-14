package paulevs.bnb.world.biome;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.dimension.DimensionData;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.VoronoiNoise;
import paulevs.bnb.world.BNBWorldGenerator;
import paulevs.bnb.world.map.DataMap;
import paulevs.bnb.world.terrain.TerrainMap;
import paulevs.bnb.world.terrain.TerrainRegion;
import paulevs.bnb.world.terrain.features.OceanPillarsFeature;

import java.util.List;
import java.util.Map;

public class BiomeMap extends DataMap<Biome> {
	private final Object2ObjectMap<String, Biome> nameToBiome = new Object2ObjectOpenHashMap<>();
	private final VoronoiNoise cellNoise = new VoronoiNoise();
	private final PerlinNoise soulBiomeNoise = new PerlinNoise();
	private final PerlinNoise densityBiomeNoise = new PerlinNoise();
	private TerrainMap map;
	
	public BiomeMap() {
		super("bnb_biomes");
		BNBBiomes.BIOME_BY_TERRAIN.values().forEach(map -> map.values().forEach(list -> list.forEach(
			biome -> nameToBiome.put(biome.name, biome)
		)));
	}
	
	@Override
	protected String serialize(Biome value) {
		return value.name;
	}
	
	@Override
	protected Biome deserialize(String name) {
		return nameToBiome.getOrDefault(name, Biome.NETHER);
	}
	
	@Override
	public Biome generateData(int x, int z) {
		TerrainRegion region = map.getRegionInternal(x, z);
		if (region == TerrainRegion.OCEAN_MOUNTAINS) {
			Identifier id = map.generateData(x, z);
			if (id == OceanPillarsFeature.FEATURE_ID) {
				region = TerrainRegion.MOUNTAINS;
			}
		}
		Map<BiomeArea, List<Biome>> areaMap = BNBBiomes.BIOME_BY_TERRAIN.get(region);
		if (areaMap == null || areaMap.isEmpty()) return Biome.NETHER;
		float soul = soulBiomeNoise.get(x * 0.05, z * 0.05);
		float density = densityBiomeNoise.get(x * 0.1, z * 0.1);
		BiomeArea area = BiomeArea.getArea(soul, density);
		List<Biome> biomes = areaMap.get(area);
		if (biomes == null || biomes.isEmpty()) return Biome.NETHER;
		double px = x * 0.05 + distortionX.get(x * 0.1, z * 0.1);
		double pz = z * 0.05 + distortionZ.get(x * 0.1, z * 0.1);
		int index = (int) Math.floor(cellNoise.getID(px, pz) * biomes.size());
		return biomes.get(index);
	}
	
	@Override
	public void setData(DimensionData data, int seed) {
		super.setData(data, seed);
		cellNoise.setSeed(random.nextInt());
		soulBiomeNoise.setSeed(random.nextInt());
		densityBiomeNoise.setSeed(random.nextInt());
		map = BNBWorldGenerator.getMapCopy();
	}
	
	@Environment(EnvType.CLIENT)
	public void setSeed(int seed) {
		super.setSeed(seed);
		cellNoise.setSeed(random.nextInt());
		soulBiomeNoise.setSeed(random.nextInt());
		densityBiomeNoise.setSeed(random.nextInt());
		map = BNBWorldGenerator.getMapCopy();
	}
}
