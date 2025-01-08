package paulevs.bnb.listener;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.achievement.Achievement;
import net.minecraft.block.Block;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeRegistry;
import net.minecraft.stat.Stat;
import net.modificationstation.stationapi.api.event.achievement.AchievementRegisterEvent;
import net.modificationstation.stationapi.api.event.block.BlockEvent.BeforePlacedByItem;
import net.modificationstation.stationapi.api.event.block.entity.BlockEntityRegisterEvent;
import net.modificationstation.stationapi.api.event.entity.EntityRegister;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent.Vanilla;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.event.world.biome.BiomeRegisterEvent;
import net.modificationstation.stationapi.api.recipe.FuelRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNB;
import paulevs.bnb.achievement.BNBAchievementPage;
import paulevs.bnb.achievement.BNBAchievements;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.ShardsBlock;
import paulevs.bnb.block.entity.CocoonSpawnerBlockEntity;
import paulevs.bnb.block.entity.SpinningWheelBlockEntity;
import paulevs.bnb.block.property.BNBBlockMaterials;
import paulevs.bnb.entity.CrimsonSpiderEntity;
import paulevs.bnb.entity.ObsidianBoatEntity;
import paulevs.bnb.entity.PirozenSpiderEntity;
import paulevs.bnb.entity.PoisonSpiderEntity;
import paulevs.bnb.item.BNBItems;
import paulevs.bnb.packet.BNBWeatherPacket;
import paulevs.bnb.world.biome.BNBBiomes;

import java.util.ArrayList;
import java.util.List;

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
		event.register(CocoonSpawnerBlockEntity.class, "bnb_cocoon_spawner");
		event.register(SpinningWheelBlockEntity.class, "bnb_spinning_wheel");
	}
	
	@EventListener
	public void onEntityRegister(EntityRegister event) {
		event.register(CrimsonSpiderEntity.class, "bnb_falurian_spider");
		event.register(PirozenSpiderEntity.class, "bnb_pirozen_spider");
		event.register(PoisonSpiderEntity.class, "bnb_chlorophate_spider");
		event.register(ObsidianBoatEntity.class, "bnb_obsidian_boat");
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
		if (event.recipeId == RecipeRegisterEvent.Vanilla.SMELTING.type()) {
			for (Block block : BNBBlocks.BLOCKS_WITH_ITEMS) {
				if (block.material == BNBBlockMaterials.NETHER_LOG) {
					FuelRegistry.addFuelItem(block.asItem(), block.isFullCube() ? 300 : 100);
				}
				if (block.material == BNBBlockMaterials.NETHER_PLANT || block.material == BNBBlockMaterials.NETHER_LEAVES) {
					FuelRegistry.addFuelItem(block.asItem(), block.isFullCube() ? 100 : 50);
				}
			}
		}
		
		if (event.recipeId == Vanilla.CRAFTING_SHAPED.type()) {
			List<Recipe> bnbRecipes = new ArrayList<>();
			List<Recipe> otherRecipes = new ArrayList<>();
			
			@SuppressWarnings("unchecked")
			List<Recipe> recipes = RecipeRegistry.getInstance().getRecipes();
			
			for (Recipe recipe : recipes) {
				Identifier id = ItemRegistry.INSTANCE.getId(recipe.getOutput().getType());
				if (id != null && id.namespace == BNB.NAMESPACE) bnbRecipes.add(recipe);
				else otherRecipes.add(recipe);
			}
			
			for (int i = 0; i < bnbRecipes.size(); i++) {
				recipes.set(i, bnbRecipes.get(i));
			}
			
			for (int i = 0; i < otherRecipes.size(); i++) {
				recipes.set(i + bnbRecipes.size(), otherRecipes.get(i));
			}
		}
	}
	
	@EventListener
	public void beforeItemPlace(BeforePlacedByItem event) {
		if (!(event.block instanceof ShardsBlock shards)) return;
		if (!shards.isSupport(event.world, event.x, event.y, event.z, event.side.getOpposite())) {
			event.placeFunction = () -> false;
		}
	}
	
	@EventListener
	public void registerPackets(PacketRegisterEvent event) {
		Registry.register(PacketTypeRegistry.INSTANCE, BNBWeatherPacket.ID, BNBWeatherPacket.TYPE);
	}
}
