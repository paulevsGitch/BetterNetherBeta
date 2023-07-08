package paulevs.bnb.listeners;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.event.level.biome.BiomeRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.item.BNBItems;
import paulevs.bnb.world.biome.BNBBiomes;

public class CommonListener {
	@EventListener
	public void onBlockRegister(BlockRegistryEvent event) {
		BNBBlocks.init();
		BlockBase.PORTAL.setLightEmittance(1F);
	}
	
	@EventListener
	public void onItemRegister(ItemRegistryEvent event) {
		BNBItems.init();
	}
	
	@EventListener
	public void onBiomeRegister(BiomeRegisterEvent event) {
		BNBBiomes.init();
	}
}
