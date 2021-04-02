package paulevs.bnb.listeners;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationloader.api.common.event.recipe.RecipeRegister;
import net.modificationstation.stationloader.api.common.recipe.CraftingRegistry;
import paulevs.bnb.block.types.NetherPlanks;
import paulevs.bnb.block.types.NetherWood;

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
			
			CraftingRegistry.INSTANCE.addShapedRecipe(
				new ItemInstance(BlockBase.WORKBENCH),
				"##",
				"##",
				'#', new ItemInstance(netherPlanks, 1, -1)
			);
			
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
			
			for (NetherPlanks plank: NetherPlanks.values()) {
				CraftingRegistry.INSTANCE.addShapedRecipe(
					new ItemInstance(BlockListener.getBlock("stairs_" + plank.getName()), 4),
					"#  ",
					"## ",
					"###",
					'#', new ItemInstance(netherPlanks, 1, plank.getMeta())
				);
			}
		}
		else if (type == RecipeRegister.Vanilla.CRAFTING_SHAPELESS) {
			CraftingRegistry.INSTANCE.addShapelessRecipe(new ItemInstance(BlockBase.BUTTON), new ItemInstance(netherPlanks, 1, -1));
			
			for (NetherWood wood: NetherWood.values()) {
				CraftingRegistry.INSTANCE.addShapelessRecipe(new ItemInstance(netherPlanks, 4, wood.getMeta()), new ItemInstance(netherWood, 1, wood.getMeta()));
			}
		}
	}
}
