package paulevs.bnb.util;

import net.minecraft.item.ItemBase;

public class ItemUtil {
	public static ItemBase itemByID(int id) {
		return ItemBase.byId[id];
	}
	
	public static int getMaxID() {
		return ItemBase.byId.length;
	}
}
