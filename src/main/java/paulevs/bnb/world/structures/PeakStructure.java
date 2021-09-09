package paulevs.bnb.world.structures;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.MHelper;

import java.util.Random;
import java.util.function.Function;

public class PeakStructure extends Structure {
	protected final Function<BlockState, Boolean> placeFunction;
	protected final Function<BlockState, Boolean> airFunction;
	private final BlockState check = new BlockState(0);
	protected final BlockState block;
	protected final int height;
	protected final int radius;
	
	public PeakStructure(BlockState block, int height, int radius, Function<BlockState, Boolean> placeFunction) {
		this(block, height, radius, placeFunction, (state) -> true);
	}
	
	public PeakStructure(BlockState block, int height, int radius, Function<BlockState, Boolean> placeFunction, Function<BlockState, Boolean> airFunction) {
		this.placeFunction = placeFunction;
		this.airFunction = airFunction;
		this.height = height;
		this.radius = radius;
		this.block = block;
	}
	
	@Override
	public boolean generate(Level level, Random rand, int x, int y, int z) {
		BlockState check = new BlockState(level.getTileId(x, y - 1, z), level.getTileMeta(x, y - 1, z));
		if (!placeFunction.apply(check)) {
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
			check.setBlockID(level.getTileId(px, minY, pz));
			check.setBlockMeta(level.getTileMeta(px, minY, pz));
			if (airFunction.apply(check)) {
				check.setBlockID(level.getTileId(px, minY - 1, pz));
				check.setBlockMeta(level.getTileMeta(px, minY - 1, pz));
				if (placeFunction.apply(check)) {
					generatePillar(level, rand, px, pz, minY, maxY);
				}
			}
		}
		
		return false;
	}
	
	protected void generatePillar(Level level, Random random, int x, int z, int minY, int maxY) {
		for (int y = minY; y <= maxY; y++) {
			block.setBlockFast(level, x, y, z);
		}
	}
}
