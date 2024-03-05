package paulevs.bnb.world.structure.placer;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.api.util.math.MutableBlockPos;
import paulevs.bnb.noise.PerlinNoise;

import java.util.Random;
import java.util.function.Function;

public abstract class StructurePlacer extends Structure {
	protected static final Function<BlockPos, Boolean> DEFAULT_DENSITY = pos -> true;
	protected static final MutableBlockPos POS = new MutableBlockPos(0, 0, 0);
	
	protected Function<BlockPos, Boolean> densityFunction;
	protected final Structure structure;
	
	public StructurePlacer(Structure structure) {
		this.densityFunction = DEFAULT_DENSITY;
		this.structure = structure;
	}
	
	public abstract void place(Level level, Random random, int wx, int wy, int wz);
	
	@Override
	public boolean generate(Level level, Random random, int wx, int wy, int wz) {
		for (int y = level.getBottomSectionCoord(); y < level.getTopSectionCoord(); y++) {
			place(level, random, wx, y << 4, wz);
		}
		return true;
	}
	
	public StructurePlacer setDensityFunction(Function<BlockPos, Boolean> densityFunction) {
		this.densityFunction = densityFunction;
		return this;
	}
	
	public StructurePlacer setNoiseDensityFunction(int seed) {
		PerlinNoise noise = new PerlinNoise();
		noise.setSeed(seed);
		return setDensityFunction(pos -> noise.get(pos.x * 0.03, pos.y * 0.03, pos.z * 0.03) > 0.5F);
	}
	
	public StructurePlacer setRandomDensityFunction(int seed, int chance) {
		return setDensityFunction(pos ->
			Math.abs((int) MathHelper.hashCode(pos.x, pos.y + seed, pos.z)) % chance == 0
		);
	}
}
