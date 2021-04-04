package paulevs.bnb.block.model;

import net.minecraft.client.render.QuadPoint;
import net.modificationstation.stationloader.api.common.util.BlockFaces;
import net.modificationstation.stationloader.impl.client.model.CustomTexturedQuad;

public class CustomFacedTexturedQuad extends CustomTexturedQuad {
	private final BlockFaces side;
	
	public CustomFacedTexturedQuad(QuadPoint[] args, BlockFaces side) {
		super(args);
		this.side = side;
	}
	
	@Override
	public BlockFaces getSide() {
		return this.side;
	}
}
