package paulevs.bnb.block.model;

import net.modificationstation.stationloader.api.client.model.CustomCuboidRenderer;
import net.modificationstation.stationloader.api.client.model.CustomTexturedQuad;
import paulevs.bnb.BetterNetherBeta;

public class OBJCuboidRenderer implements CustomCuboidRenderer {
	private final CustomTexturedQuad[] quads;
	
	public OBJCuboidRenderer(CustomTexturedQuad[] quads) {
		this.quads = quads;
	}

	@Override
	public CustomTexturedQuad[] getCubeQuads() {
		return quads;
	}

	@Override
	public String getModID() {
		return BetterNetherBeta.MOD_ID;
	}
}
