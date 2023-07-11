package paulevs.bnb.world.structures.placers;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;

import java.util.Random;

public class VolumetricPlacer extends StructurePlacer {
	public VolumetricPlacer(Structure structure, int count) {
		super(structure, count);
	}
	
	@Override
	public void place(Level level, Random random, int wx, int wy, int wz) {
		for (byte i = 0; i < count; i++) {
			int px = wx + random.nextInt(16);
			int py = wy + random.nextInt(16);
			int pz = wz + random.nextInt(16);
			if (!densityFunction.apply(POS.set(px, py, pz))) continue;
			structure.generate(level, random, px, py, pz);
		}
	}
}
