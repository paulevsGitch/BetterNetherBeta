package paulevs.bnb.world.structures;

import net.minecraft.level.structure.Structure;
import paulevs.bnb.block.types.GlowingFur;
import paulevs.bnb.block.types.NetherLeaves;
import paulevs.bnb.block.types.NetherPlants;
import paulevs.bnb.block.types.NetherWood;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;

public class NetherStructures {
	public static final Structure CRIMSON_TREE = new NetherTree(
		new BlockState(BlockListener.getBlock("nether_wood"), NetherWood.CRIMSON_WOOD),
		new BlockState(BlockListener.getBlock("nether_leaves"), NetherLeaves.CRIMSON_LEAVES),
		new BlockState(BlockListener.getBlock("glowing_fur"), GlowingFur.CRIMSON_GLOWING_FUR),
		1.5F, 0.7F
	);
	public static final Structure WARPED_TREE = new NetherTree(
		new BlockState(BlockListener.getBlock("nether_wood"), NetherWood.WARPED_WOOD),
		new BlockState(BlockListener.getBlock("nether_leaves"), NetherLeaves.WARPED_LEAVES),
		new BlockState(BlockListener.getBlock("glowing_fur"), GlowingFur.WARPED_GLOWING_FUR),
		0.5F, 1F
	);
	
	public static final Structure CRIMSON_ROOTS = new NetherGrass(new BlockState(BlockListener.getBlock("nether_plant"), NetherPlants.CRIMSON_ROOTS));
	public static final Structure LAMELLARIUM = new NetherGrass(new BlockState(BlockListener.getBlock("nether_plant"), NetherPlants.LAMELLARIUM));
	public static final Structure LANTERN_GRASS = new NetherGrass(new BlockState(BlockListener.getBlock("nether_plant"), NetherPlants.LANTERN_GRASS));
	public static final Structure CRIMSON_BUSH = new NetherGrass(new BlockState(BlockListener.getBlock("nether_plant"), NetherPlants.CRIMSON_BUSH));
	
	public static final Structure WARPED_ROOTS = new NetherGrass(new BlockState(BlockListener.getBlock("nether_plant"), NetherPlants.WARPED_ROOTS));
	public static final Structure GLOWTAIL = new NetherGrass(new BlockState(BlockListener.getBlock("nether_plant"), NetherPlants.GLOWTAIL));
	public static final Structure WARPED_CORAL = new NetherGrass(new BlockState(BlockListener.getBlock("nether_plant"), NetherPlants.WARPED_CORAL));
	public static final Structure WARPED_MOSS = new NetherGrass(new BlockState(BlockListener.getBlock("nether_plant"), NetherPlants.WARPED_MOSS));
	
	public static final Structure CRIMSON_VINE = new NetherVine(
		new BlockState(BlockListener.getBlockID("nether_vine"), 1),
		new BlockState(BlockListener.getBlockID("nether_vine"), 0)
	);
	public static final Structure WARPED_VINE = new NetherVine(
		new BlockState(BlockListener.getBlockID("nether_vine"), 3),
		new BlockState(BlockListener.getBlockID("nether_vine"), 2)
	);
}
