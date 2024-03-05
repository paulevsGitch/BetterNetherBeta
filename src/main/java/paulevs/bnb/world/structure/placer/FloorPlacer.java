package paulevs.bnb.world.structure.placer;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;

import java.util.Random;

public class FloorPlacer extends StructurePlacer {
	protected final int count;
	protected boolean centered;
	
	public FloorPlacer(Structure structure, int count) {
		super(structure);
		this.count = count;
	}
	
	public FloorPlacer setCentered(boolean centered) {
		this.centered = centered;
		return this;
	}
	
	public void place(Level level, Random random, int wx, int wy, int wz) {
		for (byte i = 0; i < count; i++) {
			int px = wx + (centered ? 8 : random.nextInt(16));
			int pz = wz + (centered ? 8 : random.nextInt(16));
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
