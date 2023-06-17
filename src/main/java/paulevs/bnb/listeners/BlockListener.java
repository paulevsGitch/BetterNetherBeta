package paulevs.bnb.listeners;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.bnb.BNB;
import paulevs.bnb.block.NetherTerrainBlock;
import paulevs.bnb.block.NetherWoodBlock;
import paulevs.bnb.block.SoulTerrainBlock;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BlockListener {
	public static final Map<Identifier, BlockBase> BLOCKS = new HashMap<>();
	
	@EventListener
	public void onBlockRegister(BlockRegistryEvent event) {
		register("crimson_nylium", NetherTerrainBlock::new);
		register("warped_nylium", NetherTerrainBlock::new);
		register("poison_nylium", NetherTerrainBlock::new);
		register("corrupted_nylium", NetherTerrainBlock::new);
		register("soul_nylium", SoulTerrainBlock::new);
		register("dark_nylium", NetherTerrainBlock::new);
		
		register("crimson_wood", NetherWoodBlock::new);
		register("warped_wood", NetherWoodBlock::new);
		register("poison_wood", NetherWoodBlock::new);
		register("pale_wood", NetherWoodBlock::new);
		register("ember_wood", NetherWoodBlock::new);
		register("flame_bamboo_block", NetherWoodBlock::new);
	}
	
	private void register(String name, Function<Identifier, BlockBase> constructor) {
		Identifier id = BNB.id(name);
		BlockBase block = constructor.apply(id);
		block.setTranslationKey(id.toString());
		BLOCKS.put(id, block);
	}
}
