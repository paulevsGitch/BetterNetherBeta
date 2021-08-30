package paulevs.bnb.world.structures;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.MHelper;

import java.util.Random;
import java.util.function.Function;

public class PeakStructure extends Structure {
	private final Function<Integer, Boolean> placeFunction;
	private final BlockState block;
	private final int height;
	private final int radius;
	
	public PeakStructure(BlockState block, int height, int radius, Function<Integer, Boolean> placeFunction) {
		this.placeFunction = placeFunction;
		this.height = height;
		this.radius = radius;
		this.block = block;
	}
	
	@Override
	public boolean generate(Level level, Random rand, int x, int y, int z) {
		if (!placeFunction.apply(level.getTileId(x, y - 1, z))) {
			return false;
		}
		
		float scale = MHelper.randRange(0.75F, 1F, rand);
		int count = MathHelper.floor((radius * scale * 4) * (radius * scale * 4));
		count = MHelper.randRange(count, count << 1, rand);
		
		for (int i = 0; i < count; i++) {
			int dx = MHelper.clamp(MathHelper.floor(rand.nextGaussian() * radius * 0.75F * scale + 0.5F), -radius, +radius);
			int dz = MHelper.clamp(MathHelper.floor(rand.nextGaussian() * radius * 0.75F * scale + 0.5F), -radius, +radius);
			float distance = radius - MathHelper.sqrt(dx * dx + dz * dz);
			int px = x + dx;
			int pz = z + dz;
			int minY = y + MathHelper.floor(distance * 0.3F * radius) - rand.nextInt(2);
			int maxY = minY + MathHelper.floor(height * scale * (distance / radius + rand.nextFloat() * 0.2F));
			if (placeFunction.apply(level.getTileId(px, minY - 1, pz))) {
				for (int py = minY; py <= maxY; py++) {
					block.setBlockFast(level, px, py, pz);
				}
			}
		}
		
		return false;
	}
}
