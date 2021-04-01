package paulevs.bnb.world;

import java.util.List;
import java.util.Random;

import net.minecraft.level.biome.Biome;

public class BiomeChunk {
	protected static final int WIDTH = 16;
	private static final int SM_WIDTH = WIDTH >> 1;
	private static final int MASK_OFFSET = SM_WIDTH - 1;
	protected static final int MASK_WIDTH = WIDTH - 1;

	private final Biome[][] biomes;

	public BiomeChunk(BiomeMap map, Random random, List<Biome> picker) {
		Biome[][] PreBio = new Biome[SM_WIDTH][SM_WIDTH];
		biomes = new Biome[WIDTH][WIDTH];

		for (int x = 0; x < SM_WIDTH; x++)
			for (int z = 0; z < SM_WIDTH; z++)
				PreBio[x][z] = picker.get(random.nextInt(picker.size()));

		for (int x = 0; x < WIDTH; x++)
			for (int z = 0; z < WIDTH; z++)
				biomes[x][z] = PreBio[offsetXZ(x, random)][offsetXZ(z, random)];
	}

	public Biome getBiome(int x, int z) {
		return biomes[x & MASK_WIDTH][z & MASK_WIDTH];
	}

	private int offsetXZ(int x, Random random) {
		return ((x + random.nextInt(2)) >> 1) & MASK_OFFSET;
	}
}
