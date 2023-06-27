package paulevs.bnb.world.structures;

import net.minecraft.level.structure.Structure;
import paulevs.bnb.registries.BNBBlocks;
import paulevs.bnb.world.structures.placers.StructurePlacer;
import paulevs.bnb.world.structures.scatters.DoublePlantScatterStructure;
import paulevs.bnb.world.structures.scatters.SimpleScatterStructure;

public class BNBStructures {
	public static final Structure FLAME_BULBS_STRUCTURE = new SimpleScatterStructure(3, 15, BNBBlocks.FLAME_BULBS);
	public static final Structure CRIMSON_ROOTS_STRUCTURE = new SimpleScatterStructure(3, 5, BNBBlocks.CRIMSON_ROOTS);
	public static final Structure NETHER_DAISY_STRUCTURE = new SimpleScatterStructure(3, 5, BNBBlocks.NETHER_DAISY);
	public static final Structure FIREWEED_STRUCTURE = new DoublePlantScatterStructure(2, 3, BNBBlocks.FIREWEED);
	
	public static final StructurePlacer FLAME_BULBS_PLACER = new StructurePlacer(FLAME_BULBS_STRUCTURE, 5);
	public static final StructurePlacer CRIMSON_ROOTS_PLACER = new StructurePlacer(CRIMSON_ROOTS_STRUCTURE, 1);
	public static final StructurePlacer NETHER_DAISY_PLACER = new StructurePlacer(NETHER_DAISY_STRUCTURE, 1);
	public static final StructurePlacer FIREWEED_STRUCTURE_PLACER = new StructurePlacer(FIREWEED_STRUCTURE, 1);
}
