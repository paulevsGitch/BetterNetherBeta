package paulevs.bnb.listeners;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemInstance;
import paulevs.bhcreative.api.SimpleTab;
import paulevs.bhcreative.registry.TabRegistryEvent;
import paulevs.bnb.BNB;

public class CreativeTabListener {
	@EventListener
	public void registerTab(TabRegistryEvent event) {
		SimpleTab tab = new SimpleTab(BNB.id("bnb_tab"), new ItemInstance(BlockListener.get("crimson_nylium").getBlock()));
		event.register(tab);
		BlockListener.BLOCK_LIST.forEach(block -> tab.addItem(new ItemInstance(block)));
	}
}
