package paulevs.bnb.listeners;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import paulevs.bnb.BNB;

public class TextureListener {
	@EventListener
	public void registerTextures(TextureRegisterEvent event) {
		final ExpandableAtlas blockAtlas = Atlases.getTerrain();
		BlockBase.NETHERRACK.texture = blockAtlas.addTexture(BNB.id("block/netherrack")).index;
		BlockBase.GLOWSTONE.texture = blockAtlas.addTexture(BNB.id("block/glowstone")).index;
		BlockBase.SOUL_SAND.texture = blockAtlas.addTexture(BNB.id("block/soul_sand")).index;
	}
}
