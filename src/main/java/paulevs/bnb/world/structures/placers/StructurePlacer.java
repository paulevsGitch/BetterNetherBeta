package paulevs.bnb.world.structures.placers;

import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.util.math.BlockPos;

import java.util.Random;
import java.util.function.Function;

public class StructurePlacer {
	private static final Function<BlockPos, Boolean> DEFAULT_DENSITY = pos -> true;
	private static final BlockPos.Mutable POS = new BlockPos.Mutable();
	
	private final Structure structure;
	private final int count;
	
	private Function<BlockPos, Boolean> densityFunction;
	
	public StructurePlacer(Structure structure, int count) {
		this.densityFunction = DEFAULT_DENSITY;
		this.structure = structure;
		this.count = count;
	}
	
	public void place(Level level, Chunk chunk, Random random, int wx, int wy, int wz) {
		for (byte i = 0; i < count; i++) {
			int px = wx | random.nextInt(16);
			int pz = wz | random.nextInt(16);
			for (byte dy = 15; dy >= 0; dy--) {
				int py = wy | dy;
				if (!densityFunction.apply(POS.set(px, py, pz))) continue;
				if (!canPlace(level, chunk, random, px, py, pz)) continue;
				structure.generate(level, random, px, py, pz);
			}
		}
	}
	
	protected boolean canPlace(Level level, Chunk chunk, Random random, int x, int y, int z) {
		return chunk.getBlockState(x & 15, y, z & 15).getMaterial().isReplaceable() &&
			!chunk.getBlockState(x & 15, y - 1, z & 15).getMaterial().isReplaceable();
	}
	
	public StructurePlacer setDensityFunction(Function<BlockPos, Boolean> densityFunction) {
		this.densityFunction = densityFunction;
		return this;
	}
}
