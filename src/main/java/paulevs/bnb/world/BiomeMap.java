package paulevs.bnb.world;

import com.google.common.collect.Maps;
import net.minecraft.level.biome.Biome;
import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.noise.OpenSimplexNoise;
import paulevs.bnb.util.MHelper;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BiomeMap {
	private static final Random RANDOM = new Random();
	
	private final Map<Point, BiomeChunk> maps = Maps.newHashMap();
	private final OpenSimplexNoise noiseX;
	private final OpenSimplexNoise noiseZ;
	private final Point pos = new Point();
	private final List<Biome> picker;
	private final int sizeXZ;
	private final int depth;
	private final int seed;
	private final int size;
	
	public BiomeMap(long seed, int size, List<Biome> picker) {
		maps.clear();
		RANDOM.setSeed(seed);
		this.seed = RANDOM.nextInt();
		noiseX = new OpenSimplexNoise(RANDOM.nextLong());
		noiseZ = new OpenSimplexNoise(RANDOM.nextLong());
		this.sizeXZ = size;
		depth = (int) Math.ceil(Math.log(size) / Math.log(2));
		this.size = 1 << depth;
		this.picker = picker;
	}
	
	public void clearCache() {
		if (maps.size() > 32) {
			maps.clear();
		}
	}
	
	public Biome getBiome(int bx, int bz) {
		double x = (double) bx * size / sizeXZ;
		double z = (double) bz * size / sizeXZ;
		double nx = x;
		double nz = z;
		
		double px = bx * 0.2;
		double pz = bz * 0.2;
		
		for (int i = 0; i < depth; i++) {
			nx = (x + noiseX.eval(px, pz)) / 2F;
			nz = (z + noiseZ.eval(px, pz)) / 2F;
			
			x = nx;
			z = nz;
			
			px = px / 2 + i;
			pz = pz / 2 + i;
		}
		
		bx = MathHelper.floor(x);
		bz = MathHelper.floor(z);
		if ((bx & BiomeChunk.MASK_WIDTH) == BiomeChunk.MASK_WIDTH) {
			x += (bz / 2) & 1;
		}
		if ((bz & BiomeChunk.MASK_WIDTH) == BiomeChunk.MASK_WIDTH) {
			z += (bx / 2) & 1;
		}
		
		pos.x = MathHelper.floor(x / BiomeChunk.WIDTH);
		pos.y = MathHelper.floor(z / BiomeChunk.WIDTH);
		
		BiomeChunk chunk = maps.get(pos);
		if (chunk == null) {
			RANDOM.setSeed(MHelper.getRandomHash(seed, pos.x, pos.y));
			chunk = new BiomeChunk(this, RANDOM, picker);
			maps.put(new Point(pos.x, pos.y), chunk);
		}
		
		return chunk.getBiome(MathHelper.floor(x), MathHelper.floor(z));
	}
}
