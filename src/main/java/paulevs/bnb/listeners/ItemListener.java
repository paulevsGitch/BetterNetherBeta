package paulevs.bnb.listeners;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationloader.api.common.event.item.ItemRegister;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.interfaces.ItemInit;
import paulevs.bnb.item.SimpleNetherItem;
import paulevs.bnb.util.ItemUtil;

public class ItemListener implements ItemRegister {
	private static final Map<String, ItemBase> ITEMS = Maps.newHashMap();
	private static final List<ItemBase> ITEMS_TAB = Lists.newArrayList();
	private static Set<Integer> occupiedIDs;
	private static int startID = 200;
	
	@Override
	public void registerItems() {
		BetterNetherBeta.configItems.load();
		occupiedIDs = BetterNetherBeta.configItems.getSet("items");
		
		register("netherrack_brick", SimpleNetherItem::new);
		
		occupiedIDs = null;
		BetterNetherBeta.configItems.save();
	}
	
	private static int getID(String name) {
		while ((ItemUtil.itemByID(startID) != null || occupiedIDs.contains(startID)) && startID < ItemUtil.getMaxID()) {
			startID++;
		}
		return BetterNetherBeta.configBlocks.getInt("items." + name, startID);
	}
	
	private static <T extends ItemBase> void register(String name, ItemInit<String, Integer, T> init) {
		ITEMS.put(name, init.apply(name, getID(name)));
		ITEMS_TAB.add(ITEMS.get(name));
	}
	
	public static Collection<ItemBase> getModItems() {
		return ITEMS_TAB;
	}
}
