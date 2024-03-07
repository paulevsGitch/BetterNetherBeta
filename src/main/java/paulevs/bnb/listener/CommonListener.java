package paulevs.bnb.listener;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.achievement.Achievement;
import net.minecraft.block.Block;
import net.minecraft.stat.Stat;
import net.modificationstation.stationapi.api.event.achievement.AchievementRegisterEvent;
import net.modificationstation.stationapi.api.event.block.BlockEvent.BeforePlacedByItem;
import net.modificationstation.stationapi.api.event.block.entity.BlockEntityRegisterEvent;
import net.modificationstation.stationapi.api.event.entity.EntityRegister;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.event.world.biome.BiomeRegisterEvent;
import net.modificationstation.stationapi.api.recipe.FuelRegistry;
import paulevs.bnb.BNB;
import paulevs.bnb.achievement.BNBAchievementPage;
import paulevs.bnb.achievement.BNBAchievements;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.GlowstoneShards;
import paulevs.bnb.block.entity.CocoonSpawnerBlockEntity;
import paulevs.bnb.block.entity.NetherrackFurnaceBlockEntity;
import paulevs.bnb.block.property.BNBBlockMaterials;
import paulevs.bnb.entity.CrimsonSpiderEntity;
import paulevs.bnb.entity.PoisonSpiderEntity;
import paulevs.bnb.entity.WarpedSpiderEntity;
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
	public void onBlockEntityRegister(BlockEntityRegisterEvent event) {
		event.register(NetherrackFurnaceBlockEntity.class, "bnb_netherrack_furnace");
		event.register(CocoonSpawnerBlockEntity.class, "bnb_cocoon_spawner");
	}
	
	@EventListener
	public void onEntityRegister(EntityRegister event) {
		event.register(CrimsonSpiderEntity.class, "bnb_crimson_spider");
		event.register(WarpedSpiderEntity.class, "bnb_warped_spider");
		event.register(PoisonSpiderEntity.class, "bnb_poison_spider");
	}
	
	@EventListener
	public void registerAchievements(AchievementRegisterEvent event) {
		BNBAchievementPage page = new BNBAchievementPage(BNB.id("achievements"));
		event.achievements.addAll(BNBAchievements.ACHIEVEMENTS);
		page.addAchievements(BNBAchievements.ACHIEVEMENTS.toArray(Achievement[]::new));
		BNBAchievements.ACHIEVEMENTS.forEach(Stat::register);
	}
	
	@EventListener
	public void onRecipesRegister(RecipeRegisterEvent event) {
		if (event.recipeId != RecipeRegisterEvent.Vanilla.SMELTING.type()) return;
		for (Block block : BNBBlocks.BLOCKS_WITH_ITEMS) {
			if (block.material == BNBBlockMaterials.NETHER_WOOD) {
				FuelRegistry.addFuelItem(block.asItem(), block.isFullCube() ? 300 : 100);
			}
			if (block.material == BNBBlockMaterials.NETHER_PLANT || block.material == BNBBlockMaterials.NETHER_LEAVES) {
				FuelRegistry.addFuelItem(block.asItem(), block.isFullCube() ? 100 : 50);
			}
		}
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
