package paulevs.bnb.world.generator.biome;

import net.minecraft.level.biome.BaseBiome;
import net.minecraft.level.dimension.DimensionData;
import net.minecraft.level.gen.BiomeSource;
import paulevs.bnb.world.biome.BNBBiomes;
import paulevs.bnb.world.biome.NetherBiome;

import java.util.Arrays;
import java.util.Random;

public class BNBBiomeSource extends BiomeSource {
	private final BiomeMap map;
	
	public BNBBiomeSource(long seed, DimensionData data) {
		map = new BiomeMap(BNBBiomes.getBiomes(), seed, data);
		temperatureNoises = new double[256];
		rainfallNoises = new double[256];
		detailNoises = new double[256];
		Arrays.fill(temperatureNoises, 1.0);
	}
	
	@Override
	public double getTemperature(int x, int z) {
		return 1.0;
	}
	
	@Override
	public double[] getTemperatures(double[] data, int x, int z, int dx, int dz) {
		int size = dx * dz;
		if (data == null || data.length != size) {
			data = new double[size];
		}
		Arrays.fill(data, 1.0);
		return data;
	}
	
	@Override
	public BaseBiome[] getBiomes(BaseBiome[] biomes, int x, int z, int dx, int dz) {
		int size = dx * dz;
		if (biomes == null || biomes.length != size) {
			biomes = new BaseBiome[size];
		}
		
		int index = 0;
		for (int i = 0; i < dx; i++) {
			for (int j = 0; j < dz; j++) {
				biomes[index++] = map.getBiome(x + i, z + j);
			}
		}
		
		return biomes;
	}
	
	public NetherBiome[] fillBiomes(NetherBiome[] biomes, int x, int z, Random random) {
		for (byte i = 0; i < 16; i++) {
			for (byte j = 0; j < 16; j++) {
				biomes[i << 4 | j] = map.getBiome(x + i + random.nextInt(7) - 3, z + j + random.nextInt(7) - 3);
			}
		}
		return biomes;
	}
}
