package paulevs.bnb.listeners;

import net.modificationstation.stationloader.api.common.event.block.TileEntityRegister;
import paulevs.bnb.block.tileentity.CocoonSpawner;

import java.util.Map;

public class TileEntityListener implements TileEntityRegister {
	@Override
	public void registerTileEntities(Map<Class<?>, String> map) {
		map.put(CocoonSpawner.class, "bnb_cocoon_spawner");
	}
}
