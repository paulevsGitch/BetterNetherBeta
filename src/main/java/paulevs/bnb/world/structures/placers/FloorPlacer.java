package paulevs.bnb.world.structures.placers;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.util.maths.MutableBlockPos;

import java.util.Random;
import java.util.function.Function;

public class FloorPlacer extends StructurePlacer {
	protected final int count;
	
	public FloorPlacer(Structure structure, int count) {
		super(structure);
		this.count = count;
	}
	
	public void place(Level level, Random random, int wx, int wy, int wz) {
		for (byte i = 0; i < count; i++) {
			int px = wx + random.nextInt(16);
			int pz = wz + random.nextInt(16);
			for (byte dy = 15; dy >= 0; dy--) {
				int py = wy | dy;
				if (!canPlace(level, px, py, pz)) continue;
				if (!densityFunction.apply(POS.set(px, py, pz))) continue;
				structure.generate(level, random, px, py, pz);
				break;
			}
		}
	}
	
	protected boolean canPlace(Level level, int x, int y, int z) {
		return level.getBlockState(x, y, z).getMaterial().isReplaceable() && !level.getBlockState(x, y - 1, z).getMaterial().isReplaceable();
	}
}
