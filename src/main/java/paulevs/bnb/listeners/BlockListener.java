package paulevs.bnb.listeners;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import paulevs.bnb.registries.BNBBlocks;

public class BlockListener {
	@EventListener
	public void onBlockRegister(BlockRegistryEvent event) {
		BNBBlocks.init();
		BlockBase.PORTAL.setLightEmittance(1F);
	}
}
