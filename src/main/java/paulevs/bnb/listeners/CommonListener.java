package paulevs.bnb.listeners;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.event.block.BlockEvent.BeforePlacedByItem;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.event.world.biome.BiomeRegisterEvent;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.GlowstoneShards;
import paulevs.bnb.item.BNBItems;
import paulevs.bnb.world.biome.BNBBiomes;

public class CommonListener {
	@EventListener
	public void onBlockRegister(BlockRegistryEvent event) {
		BNBBlocks.init();
		Block.PORTAL.setLightEmittance(1F);
	}
	
	@EventListener
	public void onItemRegister(ItemRegistryEvent event) {
		BNBItems.init();
	}
	
	@EventListener
	public void onBiomeRegister(BiomeRegisterEvent event) {
		BNBBiomes.init();
	}
	
	@EventListener
	public void beforeItemPlace(BeforePlacedByItem event) {
		if (!(event.block instanceof GlowstoneShards shards)) return;
		if (!shards.isSupport(event.world, event.x, event.y, event.z, event.side.getOpposite())) {
			event.placeFunction = () -> false;
		}
	}
	
	/*@SuppressWarnings("unchecked")
	@EventListener(priority = ListenerPriority.LOWEST)
	public void afterRecipeRegister(AfterBlockAndItemRegisterEvent event) {
		final int planksID = BaseBlock.WOOD.asItem().id;
		Field[] fields = ShapedRecipe.class.getDeclaredFields();
		List<Recipe> recipes = new ArrayList<>();
		
		fields[0].setAccessible(true);
		fields[1].setAccessible(true);
		
		RecipeRegistry.getInstance().getRecipes().forEach(obj -> {
			if (obj instanceof ShapedRecipe recipe) {
				ItemStack[] ingredients = ((StationRecipe) recipe).getIngredients();
				boolean add = false;
				
				for (ItemStack stack : ingredients) {
					if (stack != null && stack.itemId == planksID) {
						add = true;
						break;
					}
				}
				
				if (!add) return;
				
				int width = 0;
				int height = 0;
				
				try {
					width = (int) fields[0].get(recipe);
					height = (int) fields[1].get(recipe);
				}
				catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
				ItemStack[] newIngredients = new ItemStack[ingredients.length];
				
				for (int i = 0; i < newIngredients.length; i++) {
					ItemStack stack = ingredients[i];
					if (stack == null) continue;
					if (stack.itemId == planksID) {
						stack = new ItemStack(BNBBlocks.CRIMSON_PLANKS);
					}
					else stack = stack.copy();
					newIngredients[i] = stack;
				}
				
				System.out.println("Recipe " + width + " " + height + " " + newIngredients.length + " " + recipe.getOutput() + " " + Arrays.toString(newIngredients));
				recipes.add(new ShapedRecipe(width, height, newIngredients, recipe.getOutput().copy()));
			}
		});
		
		//RecipeRegistry.getInstance().getRecipes().addAll(recipes);
		//RecipeRegistry.getInstance().re
	}*/
}
