package paulevs.bnb.world.structures;

import net.minecraft.level.structure.Structure;

public class StructureInstance {
	private final Structure structure;
	private final int count;
	
	public StructureInstance(Structure structure, int count) {
		this.structure = structure;
		this.count = count;
	}
}
