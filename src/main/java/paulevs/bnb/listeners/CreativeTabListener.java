package paulevs.bnb.listeners;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemInstance;
import paulevs.bhcreative.api.SimpleTab;
import paulevs.bhcreative.registry.TabRegistryEvent;
import paulevs.bnb.BNB;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.item.BNBItems;

public class CreativeTabListener {
	@EventListener
	public void registerTab(TabRegistryEvent event) {
		System.out.println("Adding BNB tab");
		SimpleTab tab = new SimpleTab(BNB.id("creative_tab"), new ItemInstance(BNBBlocks.CRIMSON_NYLIUM));
		event.register(tab);
		BNBBlocks.BLOCKS_WITH_ITEMS.forEach(block -> tab.addItem(new ItemInstance(block)));
		BNBItems.ITEMS.forEach(item -> tab.addItem(new ItemInstance(item)));
	}
}
