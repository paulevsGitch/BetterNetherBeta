package paulevs.bnb.listeners;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import paulevs.bnb.interfaces.AutoTextured;

public class TextureListener {
	@EventListener
	public void registerTextures(TextureRegisterEvent event) {
		final ExpandableAtlas blockAtlas = Atlases.getTerrain();
		BlockListener.BLOCKS.values().forEach(block -> {
			if (block instanceof AutoTextured) {
				((AutoTextured) block).registerTextures(blockAtlas);
			}
		});
	}
}
