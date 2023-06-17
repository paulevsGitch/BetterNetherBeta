package paulevs.bnb.interfaces;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public interface MultiTexturedBlock extends AutoTextured {
	@Environment(EnvType.CLIENT)
	int[] getTextureStorage();
	
	@Environment(EnvType.CLIENT)
	Identifier[] getTextureNames(Identifier id);
	
	@Override
	@Environment(EnvType.CLIENT)
	default void registerTextures(ExpandableAtlas atlas) {
		BlockBase block = (BlockBase) this;
		Identifier id = BlockRegistry.INSTANCE.getId(block);
		if (id == null) return;
		int[] storage = getTextureStorage();
		Identifier[] names = getTextureNames(id);
		for (int i = 0; i < storage.length; i++) {
			storage[i] = atlas.addTexture(names[i]).index;
		}
		block.texture = storage[0];
	}
}
