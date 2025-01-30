package paulevs.bnb.listener;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemStack;
import paulevs.bhcreative.api.SimpleTab;
import paulevs.bhcreative.registry.TabRegistryEvent;
import paulevs.bnb.BNB;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.item.BNBItems;

public class CreativeTabListener {
	@EventListener
	public void registerTab(TabRegistryEvent event) {
		BNB.LOGGER.info("Adding BNB tab");
		
		SimpleTab blocksTab = new SimpleTab(BNB.id("blocks"), new ItemStack(BNBBlocks.NETHERRACK_MYCORRUM));
		BNBBlocks.BLOCKS_WITH_ITEMS.forEach(block -> blocksTab.addItem(new ItemStack(block)));
		event.register(blocksTab);
		
		SimpleTab itemsTab = new SimpleTab(BNB.id("items"), new ItemStack(BNBItems.ORICHALCUM_INGOT));
		BNBItems.ITEMS.forEach(block -> itemsTab.addItem(new ItemStack(block)));
		event.register(itemsTab);
	}
}
