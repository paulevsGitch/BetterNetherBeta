package paulevs.bnb.listeners;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationloader.api.common.event.recipe.RecipeRegister;
import net.modificationstation.stationloader.api.common.recipe.CraftingRegistry;
import net.modificationstation.stationloader.api.common.recipe.SmeltingRegistry;
import paulevs.bnb.block.types.BasaltBlockType;
import paulevs.bnb.block.types.FlameBambooType;
import paulevs.bnb.block.types.NetherPlanksType;
import paulevs.bnb.block.types.NetherWoodType;
import paulevs.bnb.block.types.NetherrackBricksType;
import paulevs.bnb.block.types.UmbralithType;

public class RecipeListener implements RecipeRegister {
	@Override
	public void registerRecipes(String recipeType) {
		RecipeRegister.Vanilla type = RecipeRegister.Vanilla.fromType(recipeType);
		BlockBase netherWood = BlockListener.getBlock("nether_wood");
		BlockBase netherPlanks = BlockListener.getBlock("nether_planks");
		if (type == RecipeRegister.Vanilla.CRAFTING_SHAPED) {
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(ItemBase.stick, 4),
				"#",
				"#",
				'#', new ItemInstance(netherPlanks, 1, -1)
			);
			
			addSquareRecipe(new ItemInstance(netherPlanks, 1, -1), new ItemInstance(BlockBase.WORKBENCH));
			
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(BlockBase.CHEST),
				"###",
				"# #",
				"###",
				'#', new ItemInstance(netherPlanks, 1, -1)
			);
			
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(BlockBase.WOODEN_PRESSURE_PLATE),
				"##",
				'#', new ItemInstance(netherPlanks, 1, -1)
			);
			
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(ItemBase.woodDoor),
				"##",
				"##",
				"##",
				'#', new ItemInstance(netherPlanks, 1, -1)
			);
			
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(ItemBase.woodPickaxe),
				"###",
				" I ",
				" I ",
				'#', new ItemInstance(netherPlanks, 1, -1),
				'I', new ItemInstance(ItemBase.stick)
			);
			
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(ItemBase.woodAxe),
				"##",
				"#I",
				" I",
				'#', new ItemInstance(netherPlanks, 1, -1),
				'I', new ItemInstance(ItemBase.stick)
			);
			
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(ItemBase.woodHoe),
				"##",
				" I",
				" I",
				'#', new ItemInstance(netherPlanks, 1, -1),
				'I', new ItemInstance(ItemBase.stick)
			);
			
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(ItemBase.woodShovel),
				"#",
				"I",
				"I",
				'#', new ItemInstance(netherPlanks, 1, -1),
				'I', new ItemInstance(ItemBase.stick)
			);
			
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(ItemBase.woodSword),
				"#",
				"#",
				"I",
				'#', new ItemInstance(netherPlanks, 1, -1),
				'I', new ItemInstance(ItemBase.stick)
			);
			
			for (NetherPlanksType plank: NetherPlanksType.values()) {
				addStairsRecipe(netherPlanks, plank.getMeta(), BlockListener.getBlock("stairs_" + plank.getName()));
				addSlabRecipe(netherPlanks, plank.getMeta(), BlockListener.getBlock("slab_" + plank.getName()));
			}
			
			BlockBase block = BlockListener.getBlock("netherrack_bricks");
			addSquareRecipe(new ItemInstance(ItemListener.getItem("netherrack_brick")), new ItemInstance(block));
			addStairsRecipe(block, 0, BlockListener.getBlock("netherrack_brick_stairs"));
			addSlabRecipe(block, 0, BlockListener.getBlock("netherrack_brick_slab"));
			addSquareRecipe(
				new ItemInstance(BlockListener.getBlock("netherrack_brick_slab"), 1, -1),
				new ItemInstance(block, 2, NetherrackBricksType.NETHERRACK_BRICK_LARGE_TILE.getMeta())
			);
			addSquareRecipe(
				new ItemInstance(block, 1, NetherrackBricksType.NETHERRACK_BRICK_LARGE_TILE.getMeta()),
				new ItemInstance(block, 4, NetherrackBricksType.NETHERRACK_BRICK_SMALL_TILE.getMeta())
			);
			addSlabRecipe(
				new ItemInstance(block, 1, NetherrackBricksType.NETHERRACK_BRICK_SMALL_TILE.getMeta()),
				BlockListener.getBlock("netherrack_tile_slab")
			);
			addPillarRecipe(
				new ItemInstance(BlockListener.getBlock("netherrack_brick_slab"), 1, -1),
				new ItemInstance(block, 1, NetherrackBricksType.NETHERRACK_BRICK_PILLAR_Y.getMeta())
			);
			
			block = BlockListener.getBlock("basalt");
			addStairsRecipe(block, 0, BlockListener.getBlock("basalt_stairs"));
			addSlabRecipe(block, 0, BlockListener.getBlock("basalt_slab"));
			addSquareRecipe(
				new ItemInstance(block, 1, BasaltBlockType.BASALT.getMeta()),
				new ItemInstance(block, 4, BasaltBlockType.BASALT_SMOOTH.getMeta())
			);
			addSquareRecipe(
				new ItemInstance(block, 1, BasaltBlockType.BASALT_SMOOTH.getMeta()),
				new ItemInstance(block, 4, BasaltBlockType.BASALT_BRICKS.getMeta())
			);
			addPillarRecipe(
				new ItemInstance(BlockListener.getBlock("basalt_slab"), 1, -1),
				new ItemInstance(block, 1, BasaltBlockType.BASALT_PILLAR_Y.getMeta())
			);
			addStairsRecipe(block, BasaltBlockType.BASALT_BRICKS.getMeta(), BlockListener.getBlock("basalt_brick_stairs"));
			addSlabRecipe(block, BasaltBlockType.BASALT_BRICKS.getMeta(), BlockListener.getBlock("basalt_brick_slab"));
			
			block = BlockListener.getBlock("umbralith");
			addStairsRecipe(block, 0, BlockListener.getBlock("umbralith_stairs"));
			addSlabRecipe(block, 0, BlockListener.getBlock("umbralith_slab"));
			addSquareRecipe(
				new ItemInstance(block, 1, UmbralithType.UMBRALITH.getMeta()),
				new ItemInstance(block, 4, UmbralithType.UMBRALITH_POLISHED.getMeta())
			);
			addSquareRecipe(
				new ItemInstance(block, 1, UmbralithType.UMBRALITH_POLISHED.getMeta()),
				new ItemInstance(block, 4, UmbralithType.UMBRALITH_BRICKS.getMeta())
			);
			addSquareRecipe(
				new ItemInstance(BlockListener.getBlock("umbralith_brick_slab"), 1, -1),
				new ItemInstance(block, 2, UmbralithType.UMBRALITH_TILES.getMeta())
			);
			addPillarRecipe(
				new ItemInstance(BlockListener.getBlock("umbralith_brick_slab"), 1, -1),
				new ItemInstance(block, 1, UmbralithType.UMBRALITH_PILLAR_Y.getMeta())
			);
			addStairsRecipe(block, UmbralithType.UMBRALITH_BRICKS.getMeta(), BlockListener.getBlock("umbralith_brick_stairs"));
			addSlabRecipe(block, UmbralithType.UMBRALITH_BRICKS.getMeta(), BlockListener.getBlock("umbralith_brick_slab"));
			
			ItemBase item = ItemListener.getItem("orichalcum_ingot");
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(ItemListener.getItem("orichalcum_pickaxe")),
				"###",
				" I ",
				" I ",
				'#', new ItemInstance(item),
				'I', new ItemInstance(ItemBase.stick)
			);
			
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(ItemListener.getItem("orichalcum_axe")),
				"##",
				"#I",
				" I",
				'#', new ItemInstance(item),
				'I', new ItemInstance(ItemBase.stick)
			);
			
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(ItemListener.getItem("orichalcum_hoe")),
				"##",
				" I",
				" I",
				'#', new ItemInstance(item),
				'I', new ItemInstance(ItemBase.stick)
			);
			
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(ItemListener.getItem("orichalcum_shovel")),
				"#",
				"I",
				"I",
				'#', new ItemInstance(item),
				'I', new ItemInstance(ItemBase.stick)
			);
			
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(ItemListener.getItem("orichalcum_sword")),
				"#",
				"#",
				"I",
				'#', new ItemInstance(item),
				'I', new ItemInstance(ItemBase.stick)
			);
			
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(ItemListener.getItem("orichalcum_shears")),
				"# ",
				" #",
				'#', new ItemInstance(item)
			);
			
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(ItemListener.getItem("orichalcum_helmet")),
				"###",
				"# #",
				'#', new ItemInstance(item)
			);
			
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(ItemListener.getItem("orichalcum_chestplate")),
				"# #",
				"###",
				"###",
				'#', new ItemInstance(item)
			);
			
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(ItemListener.getItem("orichalcum_leggings")),
				"###",
				"# #",
				"# #",
				'#', new ItemInstance(item)
			);
			
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(ItemListener.getItem("orichalcum_boots")),
				"# #",
				"# #",
				'#', new ItemInstance(item)
			);
			
			addSquareRecipe(new ItemInstance(BlockListener.getBlock("soul_spire")), new ItemInstance(BlockListener.getBlock("soul_spire_block")));
			
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(BlockListener.getBlock("flame_bamboo"), 6, FlameBambooType.LADDER.getMeta()),
				"#B#",
				"#B#",
				"#B#",
				'B', new ItemInstance(BlockListener.getBlock("flame_bamboo"), 1, FlameBambooType.UNNATURAL_STEM.getMeta()),
				'#', new ItemInstance(ItemBase.stick)
			);
			
			addSquareRecipe(
				new ItemInstance(BlockListener.getBlock("flame_bamboo"), 1, FlameBambooType.UNNATURAL_STEM.getMeta()),
				new ItemInstance(BlockListener.getBlock("nether_wood"), 1, NetherWoodType.FLAME_BAMBOO_BLOCK.getMeta())
			);
		}
		else if (type == RecipeRegister.Vanilla.CRAFTING_SHAPELESS) {
			for (NetherPlanksType plank: NetherPlanksType.values()) {
				CraftingRegistry.INSTANCE.addShapelessRecipe(new ItemInstance(netherPlanks, 4, plank.getMeta()), new ItemInstance(netherWood, 1, plank.getMeta()));
			}
			
			CraftingRegistry.INSTANCE.addShapelessRecipe(new ItemInstance(ItemBase.stick, 4, 0), new ItemInstance(BlockListener.getBlockID("flame_bamboo"), 1, 5));
			CraftingRegistry.INSTANCE.addShapelessRecipe(
				new ItemInstance(BlockListener.getBlockID("flame_bamboo"), 4, FlameBambooType.UNNATURAL_STEM.getMeta()),
				new ItemInstance(BlockListener.getBlock("nether_wood"), 1, NetherWoodType.FLAME_BAMBOO_BLOCK.getMeta())
			);
		}
		else if (type == RecipeRegister.Vanilla.SMELTING) {
			SmeltingRegistry.INSTANCE.addSmeltingRecipe(new ItemInstance(BlockBase.NETHERRACK), new ItemInstance(ItemListener.getItem("netherrack_brick")));
			SmeltingRegistry.INSTANCE.addSmeltingRecipe(new ItemInstance(ItemListener.getItem("raw_orichalcum")), new ItemInstance(ItemListener.getItem("orichalcum_ingot")));
		}
	}
	
	private static void addSquareRecipe(ItemInstance src, ItemInstance res) {
		CraftingRegistry.INSTANCE.addShapedRecipe(res, "##", "##", '#', src);
	}
	
	private static void addPillarRecipe(ItemInstance src, ItemInstance res) {
		CraftingRegistry.INSTANCE.addShapedRecipe(res, "#", "#", '#', src);
	}
	
	private static void addStairsRecipe(BlockBase src, int meta, BlockBase res) {
		CraftingRegistry.INSTANCE.addShapedRecipe(new ItemInstance(res, 4), "#  ", "## ", "###", '#', new ItemInstance(src, 1, meta));
	}
	
	private static void addSlabRecipe(BlockBase src, int meta, BlockBase res) {
		CraftingRegistry.INSTANCE.addShapedRecipe(new ItemInstance(res, 6), "###", '#', new ItemInstance(src, 1, meta));
	}
	
	private static void addSlabRecipe(ItemInstance src, BlockBase res) {
		CraftingRegistry.INSTANCE.addShapedRecipe(new ItemInstance(res, 6), "###", '#', src);
	}
}
