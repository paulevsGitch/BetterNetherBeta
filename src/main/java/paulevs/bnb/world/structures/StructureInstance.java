package paulevs.bnb.world.structures;

import net.minecraft.level.structure.Structure;

public class StructureInstance {
	private final Structure structure;
	private final float chance;
	private final int count;
	
	public StructureInstance(Structure structure, float chance, int count) {
		this.structure = structure;
		this.chance = chance;
		this.count = count;
	}
	
	public Structure getStructure() {
		return structure;
	}
	
	public float getChance() {
		return chance;
	}
	
	public int getCount() {
		return count;
	}
}
