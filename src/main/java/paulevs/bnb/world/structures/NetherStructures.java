package paulevs.bnb.world.structures;

import net.minecraft.level.structure.Structure;
import paulevs.bnb.block.types.NetherLantern;
import paulevs.bnb.block.types.NetherLeaves;
import paulevs.bnb.block.types.NetherPlants;
import paulevs.bnb.block.types.NetherTreeFur;
import paulevs.bnb.block.types.NetherVines;
import paulevs.bnb.block.types.NetherWood;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;

public class NetherStructures {
	public static final Structure CRIMSON_TREE = new NetherTree(
		new BlockState(BlockListener.getBlock("nether_wood"), NetherWood.CRIMSON_WOOD),
		new BlockState(BlockListener.getBlock("nether_leaves"), NetherLeaves.CRIMSON_LEAVES),
		new BlockState(BlockListener.getBlock("nether_tree_fur"), NetherTreeFur.CRIMSON_GLOWING_FUR),
		new BlockState(BlockListener.getBlock("nether_lantern"), NetherLantern.CRIMSON_LANTERN),
		1.5F, 0.7F
	);
	public static final Structure WARPED_TREE = new NetherTree(
		new BlockState(BlockListener.getBlock("nether_wood"), NetherWood.WARPED_WOOD),
		new BlockState(BlockListener.getBlock("nether_leaves"), NetherLeaves.WARPED_LEAVES),
		new BlockState(BlockListener.getBlock("nether_tree_fur"), NetherTreeFur.WARPED_GLOWING_FUR),
		new BlockState(BlockListener.getBlock("nether_lantern"), NetherLantern.WARPED_LANTERN),
		0.5F, 1F
	);
	public static final Structure POISON_TREE = new NetherTreeSpherical(
		new BlockState(BlockListener.getBlock("nether_wood"), NetherWood.POISON_WOOD),
		new BlockState(BlockListener.getBlock("nether_leaves"), NetherLeaves.POISON_LEAVES),
		new BlockState(BlockListener.getBlock("nether_tree_fur"), NetherTreeFur.POISON_GLOWING_FUR),
		new BlockState(BlockListener.getBlock("nether_lantern"), NetherLantern.POISON_LANTERN),
		0.9F
	);
	
	public static final Structure CRIMSON_ROOTS = makeGrass(NetherPlants.CRIMSON_ROOTS);
	public static final Structure LAMELLARIUM = makeGrass(NetherPlants.LAMELLARIUM);
	public static final Structure LANTERN_GRASS = makeGrass(NetherPlants.LANTERN_GRASS);
	public static final Structure CRIMSON_BUSH = makeGrass(NetherPlants.CRIMSON_BUSH);
	
	public static final Structure WARPED_ROOTS = makeGrass(NetherPlants.WARPED_ROOTS);
	public static final Structure GLOWTAIL = makeGrass(NetherPlants.GLOWTAIL);
	public static final Structure WARPED_CORAL = makeGrass(NetherPlants.WARPED_CORAL);
	public static final Structure WARPED_MOSS = makeGrass(NetherPlants.WARPED_MOSS);
	
	public static final Structure BUBBLE_GRASS = makeGrass(NetherPlants.BUBBLE_GRASS);
	public static final Structure LONGWEED = makeGrass(NetherPlants.LONGWEED);
	public static final Structure JELLYSHROOM = makeGrass(NetherPlants.JELLYSHROOM);
	public static final Structure TAILGRASS = makeGrass(NetherPlants.TAILGRASS);
	
	public static final Structure CRIMSON_COCOON = new BlockScatter(new BlockState(BlockListener.getBlock("spider_cocoon"), 0), 1F);
	public static final Structure WARPED_COCOON = new BlockScatter(new BlockState(BlockListener.getBlock("spider_cocoon"), 1), 1F);
	public static final Structure POISON_COCOON = new BlockScatter(new BlockState(BlockListener.getBlock("spider_cocoon"), 2), 1F);
	
	public static final Structure CRIMSON_VINE = new NetherVine(
		new BlockState(BlockListener.getBlock("nether_vine"), NetherVines.CRIMSON_VINE_TOP),
		new BlockState(BlockListener.getBlock("nether_vine"), NetherVines.CRIMSON_VINE_BOTTOM)
	);
	public static final Structure WARPED_VINE = new NetherVine(
		new BlockState(BlockListener.getBlock("nether_vine"), NetherVines.WARPED_VINE_TOP),
		new BlockState(BlockListener.getBlock("nether_vine"), NetherVines.WARPED_VINE_BOTTOM)
	);
	
	public static final NetherOre ORICHALCUM_ORE = makeOre(paulevs.bnb.block.types.NetherOre.ORICHALCUM_ORE, 8);
	
	private static Structure makeGrass(NetherPlants variant) {
		return new BlockScatter(new BlockState(BlockListener.getBlock("nether_plant"), variant), 3F);
	}
	
	private static NetherOre makeOre(paulevs.bnb.block.types.NetherOre variant, int size) {
		return new NetherOre(new BlockState(BlockListener.getBlock("nether_ore"), variant), size);
	}
}
