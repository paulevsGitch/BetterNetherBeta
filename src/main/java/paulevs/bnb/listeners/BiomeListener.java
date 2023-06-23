package paulevs.bnb.listeners;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.event.level.biome.BiomeRegisterEvent;
import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.bnb.BNB;
import paulevs.bnb.world.biome.NetherBiome;
import paulevs.bnb.world.biome.SimpleNetherBiome;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BiomeListener {
	public static final Map<Identifier, NetherBiome> BIOMES = new HashMap<>();
	
	@EventListener
	public void onBiomeRegister(BiomeRegisterEvent event) {
		register("crimson_forest", SimpleNetherBiome::new).setSurface(getState("crimson_nylium"));
	}
	
	private <B extends NetherBiome> B register(String name, Function<Identifier, B> constructor) {
		Identifier id = BNB.id(name);
		B biome = constructor.apply(id);
		BIOMES.put(id, biome);
		return biome;
	}
	
	private BlockState getState(String name) {
		return BlockListener.BLOCKS.get(BNB.id(name)).getDefaultState();
	}
}
