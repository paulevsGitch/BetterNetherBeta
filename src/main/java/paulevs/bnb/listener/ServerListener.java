package paulevs.bnb.listener;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import paulevs.bnb.command.BNBCommandManager;

@Environment(EnvType.SERVER)
public class ServerListener {
	@EventListener
	public void onInit(InitEvent event) {
		BNBCommandManager.registerServer();
	}
}
