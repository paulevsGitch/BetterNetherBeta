package paulevs.bnb.listeners;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import paulevs.bnb.BNB;
import paulevs.bnb.interfaces.AutoTextured;
import paulevs.bnb.registries.BNBBlocks;

public class TextureListener {
	@EventListener
	public void registerTextures(TextureRegisterEvent event) {
		final ExpandableAtlas blockAtlas = Atlases.getTerrain();
		
		BNBBlocks.BLOCKS_WITH_ITEMS.forEach(block -> {
			if (block instanceof AutoTextured) {
				((AutoTextured) block).registerTextures(blockAtlas);
			}
		});
		
		BlockBase.NETHERRACK.texture = blockAtlas.addTexture(BNB.id("block/netherrack")).index;
		BlockBase.GLOWSTONE.texture = blockAtlas.addTexture(BNB.id("block/glowstone")).index;
		BlockBase.SOUL_SAND.texture = blockAtlas.addTexture(BNB.id("block/soul_sand")).index;
	}
}
