package paulevs.bnb.item;

import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import paulevs.bnb.BNB;

public class BNBItemTags {
	public static final TagKey<Item> NETHER_FIBER_SOURCE = get("nether_fiber_source");
	public static final TagKey<Item> FIREPROOF_ARMOR = get("fireproof_armor");
	
	private static TagKey<Item> get(String name) {
		return TagKey.of(ItemRegistry.KEY, BNB.id(name));
	}
}
