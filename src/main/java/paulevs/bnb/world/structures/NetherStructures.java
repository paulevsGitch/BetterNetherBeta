package paulevs.bnb.world.structures;

import net.minecraft.level.structure.Structure;
import paulevs.bnb.block.types.BasaltBlockType;
import paulevs.bnb.block.types.NetherFungusType;
import paulevs.bnb.block.types.NetherPlantType;
import paulevs.bnb.block.types.NetherLanternType;
import paulevs.bnb.block.types.NetherLeavesType;
import paulevs.bnb.block.types.NetherOreType;
import paulevs.bnb.block.types.NetherTreeFurType;
import paulevs.bnb.block.types.NetherVineType;
import paulevs.bnb.block.types.NetherWoodType;
import paulevs.bnb.block.types.SoulPlantType;
import paulevs.bnb.block.types.TallGlowNetherPlantType;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.RangedBlockState;

public class NetherStructures {
	public static final Structure CRIMSON_TREE = new NetherTree(
		new BlockState(BlockListener.getBlock("nether_wood"), NetherWoodType.CRIMSON_WOOD),
		new BlockState(BlockListener.getBlock("nether_leaves"), NetherLeavesType.CRIMSON_LEAVES),
		new BlockState(BlockListener.getBlock("nether_tree_fur"), NetherTreeFurType.CRIMSON_GLOWING_FUR),
		new BlockState(BlockListener.getBlock("nether_lantern"), NetherLanternType.CRIMSON_LANTERN),
		1.5F, 0.7F
	);
	public static final Structure WARPED_TREE = new NetherTree(
		new BlockState(BlockListener.getBlock("nether_wood"), NetherWoodType.WARPED_WOOD),
		new BlockState(BlockListener.getBlock("nether_leaves"), NetherLeavesType.WARPED_LEAVES),
		new BlockState(BlockListener.getBlock("nether_tree_fur"), NetherTreeFurType.WARPED_GLOWING_FUR),
		new BlockState(BlockListener.getBlock("nether_lantern"), NetherLanternType.WARPED_LANTERN),
		0.5F, 1F
	);
	public static final Structure POISON_TREE = new NetherTreeSpherical(
		new BlockState(BlockListener.getBlock("nether_wood"), NetherWoodType.POISON_WOOD),
		new BlockState(BlockListener.getBlock("nether_leaves"), NetherLeavesType.POISON_LEAVES),
		new BlockState(BlockListener.getBlock("nether_tree_fur"), NetherTreeFurType.POISON_GLOWING_FUR),
		new BlockState(BlockListener.getBlock("nether_lantern"), NetherLanternType.POISON_LANTERN),
		0.9F
	);
	public static final Structure SOUL_SPIRE = new SoulSpireStructure();
	
	public static final Structure CRIMSON_ROOTS = makeGrass(NetherPlantType.CRIMSON_ROOTS);
	public static final Structure LAMELLARIUM = makeGrass(NetherPlantType.LAMELLARIUM);
	public static final Structure LANTERN_GRASS = makeGrass(NetherPlantType.LANTERN_GRASS);
	public static final Structure CRIMSON_BUSH = makeGrass(NetherPlantType.CRIMSON_BUSH);
	
	public static final Structure WARPED_ROOTS = makeGrass(NetherPlantType.WARPED_ROOTS);
	public static final Structure GLOWTAIL = makeGrass(NetherPlantType.GLOWTAIL);
	public static final Structure WARPED_CORAL = makeGrass(NetherPlantType.WARPED_CORAL);
	public static final Structure WARPED_MOSS = makeGrass(NetherPlantType.WARPED_MOSS);
	
	public static final Structure BUBBLE_GRASS = makeGrass(NetherPlantType.BUBBLE_GRASS);
	public static final Structure LONGWEED = makeGrass(NetherPlantType.LONGWEED);
	public static final Structure JELLYSHROOM = makeGrass(NetherPlantType.JELLYSHROOM);
	public static final Structure TAILGRASS = makeGrass(NetherPlantType.TAILGRASS);
	
	public static final Structure VIOLEUM = makeGrass(NetherPlantType.VIOLEUM);
	public static final Structure SHATTERED_GRASS = makeGrass(NetherPlantType.SHATTERED_GRASS);
	
	public static final Structure BULBINE = new TallPlant(new BlockState(BlockListener.getBlock("tall_glow_nether_plant"),
		TallGlowNetherPlantType.BULBINE
	), 3, 5F);
	
	public static final Structure SOUL_BULBITE = makeGrass(SoulPlantType.SOUL_BULBITE);
	public static final Structure BONE_PEAKS = makeGrass(SoulPlantType.BONE_PEAKS);
	public static final Structure SOUL_HEART = new BlockScatter(new RangedBlockState(BlockListener.getBlock("soul_heart"), 3), 2F);
	
	public static final Structure CRIMSON_FUNGUS = new BlockScatter(new BlockState(BlockListener.getBlock("nether_fungus"), NetherFungusType.CRIMSON_FUNGUS), 1F);
	public static final Structure WARPED_FUNGUS = new BlockScatter(new BlockState(BlockListener.getBlock("nether_fungus"), NetherFungusType.WARPED_FUNGUS), 1F);
	
	public static final Structure CRIMSON_COCOON = new BlockScatter(new BlockState(BlockListener.getBlock("spider_cocoon"), 0), 1F);
	public static final Structure WARPED_COCOON = new BlockScatter(new BlockState(BlockListener.getBlock("spider_cocoon"), 1), 1F);
	public static final Structure POISON_COCOON = new BlockScatter(new BlockState(BlockListener.getBlock("spider_cocoon"), 2), 1F);
	
	public static final Structure CRIMSON_VINE = new NetherVine(new BlockState(BlockListener.getBlock("nether_vine"), NetherVineType.CRIMSON_VINE));
	public static final Structure WARPED_VINE = new NetherVine(new BlockState(BlockListener.getBlock("nether_vine"), NetherVineType.WARPED_VINE));
	public static final Structure VIRID_VINE = new NetherVine(new BlockState(BlockListener.getBlock("nether_vine"), NetherVineType.VIRID_VINE));
	
	//public static final Structure CRIMSON_GLOWING_FUR = new NetherGroupedVine(new BlockState(BlockListener.getBlock("nether_tree_fur"), NetherTreeFur.CRIMSON_GLOWING_FUR));
	//public static final Structure WARPED_GLOWING_FUR = new NetherGroupedVine(new BlockState(BlockListener.getBlock("nether_tree_fur"), NetherTreeFur.WARPED_GLOWING_FUR));
	//public static final Structure POISON_GLOWING_FUR = new NetherGroupedVine(new BlockState(BlockListener.getBlock("nether_tree_fur"), NetherTreeFur.POISON_GLOWING_FUR));
	
	public static final Structure BASALT_PEAK = new PeakStructure(
		new BlockState(BlockListener.getBlock("basalt")),
		10,
		6,
		blockState -> {
			if (blockState.getBlockID() == BlockListener.getBlockID("basalt") && blockState.getMeta() == BasaltBlockType.FLAMING_BASALT.getMeta()) {
				return false;
			}
			return BlockUtil.isTerrain(blockState.getBlockID());
		}
	);
	
	public static final Structure FLAMING_BASALT_PEAK = new PeakWithTopStructure(
		new BlockState(BlockListener.getBlock("basalt"), BasaltBlockType.BASALT),
		10,
		6,
		blockState -> {
			if (blockState.getBlockID() == BlockListener.getBlockID("basalt") && blockState.getMeta() == BasaltBlockType.FLAMING_BASALT.getMeta()) {
				return false;
			}
			return BlockUtil.isTerrain(blockState.getBlockID());
		},
		new BlockState(BlockListener.getBlock("basalt"), BasaltBlockType.FLAMING_BASALT),
		new BlockState(BlockListener.getBlock("nether_lantern"), NetherLanternType.CRIMSON_LANTERN)
	);
	
	public static final NetherOre ORICHALCUM_ORE = makeOre(NetherOreType.ORICHALCUM_ORE, 8);
	
	private static Structure makeGrass(NetherPlantType variant) {
		return new BlockScatter(new BlockState(BlockListener.getBlock("nether_grass"), variant), 3F);
	}
	
	private static Structure makeGrass(SoulPlantType variant) {
		return new BlockScatter(new BlockState(BlockListener.getBlock("soul_grass"), variant), 5F);
	}
	
	private static NetherOre makeOre(NetherOreType variant, int size) {
		return new NetherOre(new BlockState(BlockListener.getBlock("nether_ore"), variant), size);
	}
}
