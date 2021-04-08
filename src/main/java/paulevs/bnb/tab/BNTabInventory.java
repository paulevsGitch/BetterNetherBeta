package paulevs.bnb.tab;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.block.MultiBlock;
import paulevs.bnb.block.SpiderCocoonBlock;
import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.listeners.ItemListener;
import paulevs.creative.api.CreativeTabs;
import paulevs.creative.api.SimpleTab;
import paulevs.creative.api.TabRegister;

public class BNTabInventory {
	public static void createTab() {
		TabRegister.EVENT.register(new TabRegister() {
			@Override
			public void registerTabs() {
				SimpleTab tab = CreativeTabs.register(new SimpleTab("better_nether", BetterNetherBeta.MOD_ID, BlockListener.getBlock("nether_terrain")) {
					public String getTranslatedName() {
						return "Better Nether Beta";
					}
				});
				for (BlockBase block: BlockListener.getModBlocks()) {
					if (block instanceof MultiBlock) {
						BlockEnum[] variants = ((MultiBlock) block).getVariants();
						for (BlockEnum variant: variants) {
							if (variant.isInCreative()) {
								tab.addItem(new ItemInstance(block, 1, variant.getMeta()));
							}
						}
					}
					else if (block instanceof SpiderCocoonBlock) {
						for (int meta = 0; meta < 3; meta++) {
							tab.addItem(new ItemInstance(block, 1, meta));
						}
					}
					else {
						tab.addItem(new ItemInstance(block));
					}
				}
				for (ItemBase item: ItemListener.getModItems()) {
					tab.addItem(new ItemInstance(item));
				}
			}
		});
	}
}
