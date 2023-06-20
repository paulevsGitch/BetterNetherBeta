package paulevs.bnb.listeners;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.bnb.BNB;
import paulevs.bnb.NetherLeavesTransparent;
import paulevs.bnb.block.EmberWoodBlock;
import paulevs.bnb.block.GhostPumpkinBlock;
import paulevs.bnb.block.NetherLanternBlock;
import paulevs.bnb.block.NetherLeavesBlock;
import paulevs.bnb.block.NetherTerrainBlock;
import paulevs.bnb.block.NetherVineBlock;
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
		register("ember_wood", EmberWoodBlock::new);
		register("flame_bamboo_block", NetherWoodBlock::new);
		
		register("crimson_leaves", NetherLeavesBlock::new);
		register("warped_leaves", NetherLeavesBlock::new);
		register("poison_leaves", NetherLeavesBlock::new);
		register("pale_leaves", NetherLeavesTransparent::new);
		register("ember_leaves", NetherLeavesTransparent::new);
		
		register("crimson_lantern", NetherLanternBlock::new);
		register("warped_lantern", NetherLanternBlock::new);
		register("poison_lantern", NetherLanternBlock::new);
		register("ghost_pumpkin", GhostPumpkinBlock::new);
		
		register("crimson_weeping_vine", NetherVineBlock::new);
		register("warped_weeping_vine", NetherVineBlock::new);
		register("poison_weeping_vine", NetherVineBlock::new);
		register("pale_tree_weeping_vine", NetherVineBlock::new);
		register("ember_tree_weeping_vine", NetherVineBlock::new);
	}
	
	private void register(String name, Function<Identifier, BlockBase> constructor) {
		Identifier id = BNB.id(name);
		BlockBase block = constructor.apply(id);
		block.setTranslationKey(id.toString());
		BLOCKS.put(id, block);
	}
}
