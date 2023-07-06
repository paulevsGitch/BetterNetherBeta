package paulevs.bnb.listeners;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.level.biome.BiomeRegisterEvent;
import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.bnb.BNB;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.world.biome.NetherBiome;
import paulevs.bnb.world.biome.SimpleNetherBiome;
import paulevs.bnb.world.structures.BNBStructures;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BiomeListener {
	public static final Map<Identifier, NetherBiome> BIOMES = new HashMap<>();
	
	@EventListener
	public void onBiomeRegister(BiomeRegisterEvent event) {
		register("crimson_forest", SimpleNetherBiome::new)
			.setSurface(BNBBlocks.CRIMSON_NYLIUM.getDefaultState())
			.addStructure(BNBStructures.FIREWEED_STRUCTURE_PLACER)
			.addStructure(BNBStructures.NETHER_DAISY_PLACER)
			.addStructure(BNBStructures.CRIMSON_ROOTS_PLACER)
			.addStructure(BNBStructures.FLAME_BULBS_PLACER);
	}
	
	private <B extends NetherBiome> B register(String name, Function<Identifier, B> constructor) {
		Identifier id = BNB.id(name);
		B biome = constructor.apply(id);
		BIOMES.put(id, biome);
		return biome;
	}
}
