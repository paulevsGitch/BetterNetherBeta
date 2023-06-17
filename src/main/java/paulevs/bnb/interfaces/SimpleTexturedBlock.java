package paulevs.bnb.interfaces;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public interface SimpleTexturedBlock extends AutoTextured {
	@Override
	default void registerTextures(ExpandableAtlas atlas) {
		BlockBase block = (BlockBase) this;
		Identifier id = BlockRegistry.INSTANCE.getId(block);
		if (id == null) return;
		block.texture = atlas.addTexture(Identifier.of(id.modID + ":block/" + id.id)).index;
	}
}
