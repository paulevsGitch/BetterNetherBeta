package paulevs.bnb.block;

import net.minecraft.block.BaseBlock;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import paulevs.bnb.BNB;

public class BNBBlockTags {
	public static final TagKey<BaseBlock> NETHERRACK_TERRAIN = get("netherrack_terrain");
	
	private static TagKey<BaseBlock> get(String name) {
		return TagKey.of(BlockRegistry.KEY, BNB.id(name));
	}
}
