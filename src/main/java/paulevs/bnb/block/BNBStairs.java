package paulevs.bnb.block;

import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.template.block.TemplateStairsBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNB;
import paulevs.bnb.rendering.BlockTextureUpdate;

public class BNBStairs extends TemplateStairsBlock implements BlockTextureUpdate {
	private final Block source;
	
	public BNBStairs(Identifier id, Block block) {
		super(id, block);
		setLightOpacity(0);
		source = block;
	}
	
	@Override
	public void updateTextures(ExpandableAtlas blockAtlas) {
		Identifier id = BlockRegistry.INSTANCE.getId(source);
		assert id != null;
		source.texture = blockAtlas.addTexture(BNB.id("block/" + id.path)).index;
	}
}
