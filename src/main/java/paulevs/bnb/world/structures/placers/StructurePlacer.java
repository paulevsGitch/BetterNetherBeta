package paulevs.bnb.world.structures.placers;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.util.math.MutableBlockPos;

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
}
