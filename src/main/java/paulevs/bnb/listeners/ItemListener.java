package paulevs.bnb.listeners;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationloader.api.common.event.item.ItemRegister;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.interfaces.PentaFunction;
import paulevs.bnb.interfaces.TriFunction;
import paulevs.bnb.item.HeartFruitItem;
import paulevs.bnb.item.NetherArmourItem;
import paulevs.bnb.item.NetherAxeItem;
import paulevs.bnb.item.NetherHoeItem;
import paulevs.bnb.item.NetherPickaxeItem;
import paulevs.bnb.item.NetherShearsItem;
import paulevs.bnb.item.NetherShovelItem;
import paulevs.bnb.item.NetherSpawnEggItem;
import paulevs.bnb.item.NetherSwordItem;
import paulevs.bnb.item.NetherToolItem;
import paulevs.bnb.item.SeedsTileEntity;
import paulevs.bnb.item.SimpleNetherItem;
import paulevs.bnb.item.material.NetherToolMaterial;
import paulevs.bnb.util.ItemUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public class ItemListener implements ItemRegister {
	private static final Map<String, ItemBase> ITEMS = Maps.newHashMap();
	private static final List<ItemBase> ITEMS_TAB = Lists.newArrayList();
	private static Set<Integer> occupiedIDs;
	private static int startID = 2100;
	
	@Override
	public void registerItems() {
		BetterNetherBeta.configItems.load();
		occupiedIDs = BetterNetherBeta.configItems.getSet("items");
		
		register("netherrack_brick", SimpleNetherItem::new);
		
		register("raw_orichalcum", SimpleNetherItem::new);
		register("orichalcum_ingot", SimpleNetherItem::new);
		
		register("orichalcum_helmet", NetherArmourItem::new, 3, 2, 0);
		register("orichalcum_chestplate", NetherArmourItem::new, 3, 2, 1);
		register("orichalcum_leggings", NetherArmourItem::new, 3, 2, 2);
		register("orichalcum_boots", NetherArmourItem::new, 3, 2, 3);
		
		register("orichalcum_sword", NetherSwordItem::new, NetherToolMaterial.ORICHALCUM);
		register("orichalcum_shovel", NetherShovelItem::new, NetherToolMaterial.ORICHALCUM);
		register("orichalcum_pickaxe", NetherPickaxeItem::new, NetherToolMaterial.ORICHALCUM);
		register("orichalcum_axe", NetherAxeItem::new, NetherToolMaterial.ORICHALCUM);
		register("orichalcum_hoe", NetherHoeItem::new, NetherToolMaterial.ORICHALCUM);
		register("orichalcum_shears", NetherShearsItem::new, NetherToolMaterial.ORICHALCUM);
		
		register("heart_fruit", HeartFruitItem::new);
		register("soul_heart_seeds", SeedsTileEntity::new, BlockListener.getBlock("soul_heart"));
		
		register("spawn_egg", NetherSpawnEggItem::new);
		
		occupiedIDs = null;
		BetterNetherBeta.configItems.save();
	}
	
	private static int getID(String name) {
		while ((ItemUtil.itemByID(startID + BlockBase.BY_ID.length) != null || occupiedIDs.contains(startID)) && startID < ItemUtil.getMaxID()) {
			startID++;
		}
		return BetterNetherBeta.configBlocks.getInt("items." + name, startID);
	}
	
	private static <T extends ItemBase> void register(String name, BiFunction<String, Integer, T> init) {
		T item = init.apply(name, getID(name));
		ITEMS.put(name, item);
		ITEMS_TAB.add(item);
	}
	
	private static <T extends NetherToolItem> void register(String name, TriFunction<String, Integer, NetherToolMaterial, T> init, NetherToolMaterial material) {
		T item = init.apply(name, getID(name), material);
		ITEMS.put(name, item);
		ITEMS_TAB.add(item);
	}
	
	private static <T extends SeedsTileEntity> void register(String name, TriFunction<String, Integer, BlockBase, T> init, BlockBase plant) {
		T item = init.apply(name, getID(name), plant);
		ITEMS.put(name, item);
		ITEMS_TAB.add(item);
	}
	
	private static <T extends NetherArmourItem> void register(String name, PentaFunction<String, Integer, Integer, Integer, Integer, T> init, int level, int protection, int slot) {
		T item = init.apply(name, getID(name), level, protection, slot);
		ITEMS.put(name, item);
		ITEMS_TAB.add(item);
	}
	
	public static Collection<ItemBase> getModItems() {
		return ITEMS_TAB;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends ItemBase> T getItem(String name) {
		return (T) ITEMS.get(name);
	}
}
