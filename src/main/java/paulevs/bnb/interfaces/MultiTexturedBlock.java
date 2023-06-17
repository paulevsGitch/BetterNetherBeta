package paulevs.bnb.interfaces;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

import java.util.Arrays;

public interface MultiTexturedBlock extends AutoTextured {
	int[] getTextureStorage();
	
	Identifier[] getTextureNames(Identifier id);
	
	@Override
	default void registerTextures(ExpandableAtlas atlas) {
		BlockBase block = (BlockBase) this;
		Identifier id = BlockRegistry.INSTANCE.getId(block);
		if (id == null) return;
		int[] storage = getTextureStorage();
		Identifier[] names = getTextureNames(id);
		for (int i = 0; i < storage.length; i++) {
			storage[i] = atlas.addTexture(names[i]).index;
		}
		System.out.println("Textures " + Arrays.toString(storage));
		block.texture = storage[0];
	}
}
