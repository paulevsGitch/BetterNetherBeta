package paulevs.bnb.block;

import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import paulevs.bnb.BNB;

public class BNBBlockTags {
	public static final TagKey<Block> NETHERRACK_TERRAIN = get("netherrack_terrain");
	public static final TagKey<Block> ORGANIC_TERRAIN = get("organic_terrain");
	
	private static TagKey<Block> get(String name) {
		return TagKey.of(BlockRegistry.KEY, BNB.id(name));
	}
}
