package paulevs.bnb.interfaces;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;

public interface AutoTextured {
	@Environment(EnvType.CLIENT)
	void registerTextures(ExpandableAtlas atlas);
}
