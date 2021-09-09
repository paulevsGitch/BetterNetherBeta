package paulevs.bnb.world.structures.buildings;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;

import java.util.Random;

public class ListStructure extends Structure {
	private final Structure[] structures;
	
	public ListStructure(Structure... structures) {
		this.structures = structures;
	}
	
	@Override
	public boolean generate(Level level, Random rand, int x, int y, int z) {
		Structure structure = structures[rand.nextInt(structures.length)];
		return structure.generate(level, rand, x, y, z);
	}
}
