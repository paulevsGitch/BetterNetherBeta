package paulevs.bnb.world;

import net.minecraft.block.BlockBase;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.noise.OpenSimplexNoise;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.MHelper;

import java.util.Random;
import java.util.stream.IntStream;

public class NetherGenerator {
	private static OpenSimplexNoise noiseH;
	private static OpenSimplexNoise noiseV;
	
	public static void init(long seed) {
		Random random = new Random(seed);
		noiseH = new OpenSimplexNoise(random.nextInt());
		noiseV = new OpenSimplexNoise(random.nextInt());
	}
	
	public static void generateChunk(Chunk chunk) {
		IntStream.range(0, 256).parallel().forEach(index -> {
			int x = index & 15;
			int z = index >> 4;
			int px = x | chunk.x << 4;
			int pz = z | chunk.z << 4;
			float noiseHor1 = (float) noiseV.eval(px * 0.01, pz * 0.01);
			float noiseHor2 = (float) noiseH.eval(px * 0.03, pz * 0.03);
			float noiseHor3 = (float) noiseV.eval(px * 0.001, pz * 0.010);
			float ceiling = (float) noiseH.eval(px * 0.02, pz * 0.02) * 12 + (float) noiseH.eval(px * 0.1, pz * 0.1) * 5;
			BlockUtil.fastTilePlace(chunk, x, 0, z, BlockBase.BEDROCK.id);
			BlockUtil.fastTilePlace(chunk, x, 127, z, BlockBase.BEDROCK.id);
			for (int y = 1; y < 127; y++) {
				if ((y == 1 || y == 126) && noiseH.eval(px, pz) > 0) {
					BlockUtil.fastTilePlace(chunk, x, y, z, BlockBase.BEDROCK.id);
					continue;
				}
				
				// Big pillars and ceiling/floor
				float gradient = 55 - MathHelper.abs(y - 63.5F);
				float pillars = (float) noiseV.eval(px * 0.01, y * 0.01, pz * 0.01) * 80 + 40F;
				pillars += (float) noiseV.eval(px * 0.03, y * 0.03, pz * 0.03) * 10;
				float val = MHelper.smoothUnion(gradient, pillars, 40F);
				
				// Tubes
				float noise = MathHelper.abs((float) noiseH.eval(px * 0.02, y * 0.02, pz * 0.02));
				float vert = MathHelper.sin((y + noiseHor1 * 20) * 0.1F) * 0.9F;
				noise += MHelper.clamp((pillars - 40), 0, 2) + vert * vert;
				val = MHelper.smoothUnion(val, (noise - 0.2F) * 40, 20F);
				
				// Plateau
				float height = noiseHor1;
				float variation = noiseHor2 * 5;
				variation += noiseHor3 * 10 + 40;
				height = (float) Math.atan(height * 30) / 1.5374F * variation;
				gradient = (y - height);
				val = MHelper.smoothUnion(val, gradient, 100F);
				
				// Shore
				height = (float) Math.atan(noiseHor1 + 0.2) / 1.5374F * variation;
				gradient = (y - height + 10);
				val = MHelper.smoothUnion(val, gradient, 100F);
				
				// Ceiling
				val = MHelper.min(val, ceiling + 120 - y);
				
				// Some variations
				val += noiseH.eval(px * 0.03, pz * 0.03) * 4;
				val += noiseV.eval(px * 0.1, pz * 0.1) * 2;
				
				if (val < 0) {
					BlockUtil.fastTilePlace(chunk, x, y, z, BlockBase.NETHERRACK.id);
				}
				else if (y < 32) {
					BlockUtil.fastTilePlace(chunk, x, y, z, BlockBase.STILL_LAVA.id);
				}
			}
		});
	}
}
