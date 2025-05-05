package paulevs.bnb.world.biome;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.dimension.DimensionData;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNBClient;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.noise.VoronoiNoise;
import paulevs.bnb.packet.BiomeRequestPacket;
import paulevs.bnb.packet.BiomeUpdatePacket;
import paulevs.bnb.world.BNBWorldGenerator;
import paulevs.bnb.world.map.DataMap;
import paulevs.bnb.world.map.MapChunk;
import paulevs.bnb.world.terrain.TerrainMap;
import paulevs.bnb.world.terrain.TerrainRegion;
import paulevs.bnb.world.terrain.features.OceanPillarsFeature;

import java.util.List;
import java.util.Map;

public class BiomeMap extends DataMap<Biome> {
	private static final LongList PACKET_POSITIONS = new LongArrayList();
	private final Object2ObjectMap<String, Biome> nameToBiome = new Object2ObjectOpenHashMap<>();
	private final VoronoiNoise cellNoise = new VoronoiNoise();
	private final PerlinNoise soulBiomeNoise = new PerlinNoise();
	private final PerlinNoise densityBiomeNoise = new PerlinNoise();
	private TerrainMap map = new TerrainMap();
	private boolean isUpdating;
	
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
		if (region == TerrainRegion.RIVERS) {
			region = TerrainRegion.PLAINS;
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
	
	@Override
	@Environment(EnvType.CLIENT)
	protected void onRemoteDataGen(long position) {
		if (isUpdating) return;
		Level level = BNBClient.getMinecraft().level;
		if (level == null || !level.isRemote) return;
		// TODO implement better fix or wait for StAPI #189 issue resolve
		if (BNBClient.getMinecraft().getNetworkHandler() == null) {
			PACKET_POSITIONS.add(position);
			return;
		}
		PacketHelper.send(new BiomeRequestPacket(position));
		PACKET_POSITIONS.forEach(BiomeRequestPacket::new);
		PACKET_POSITIONS.clear();
	}
	
	@Environment(EnvType.CLIENT)
	public void setSeed(int seed) {
		super.setSeed(seed);
		cellNoise.setSeed(random.nextInt());
		soulBiomeNoise.setSeed(random.nextInt());
		densityBiomeNoise.setSeed(random.nextInt());
		map = BNBWorldGenerator.getMapCopy();
	}
	
	@Environment(EnvType.SERVER)
	public void requestUpdate(PlayerEntity player, long position) {
		int x = (int) (position >> 32);
		int y = (int) position;
		MapChunk<Biome> chunk = getChunk(x, y);
		PacketHelper.sendTo(player, new BiomeUpdatePacket(position, chunk, this::serialize));
	}
	
	@Environment(EnvType.CLIENT)
	public void updateData(long position, CompoundTag data) {
		int x = (int) (position >> 32);
		int y = (int) position;
		isUpdating = true;
		MapChunk<Biome> chunk = getChunk(x, y);
		isUpdating = false;
		chunk.load(data, this::deserialize);
	}
}
