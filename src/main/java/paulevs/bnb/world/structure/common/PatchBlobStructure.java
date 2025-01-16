package paulevs.bnb.world.structure.common;

import net.modificationstation.stationapi.api.block.BlockState;
import paulevs.bnb.world.structure.terrain.BoulderStructure;

public class PatchBlobStructure extends BoulderStructure {
	private final BlockState filter;
	
	public PatchBlobStructure(BlockState state, float minRadius, float maxRadius, BlockState filter) {
		super(state, minRadius, maxRadius);
		this.filter = filter;
	}
	
	@Override
	protected boolean canReplace(BlockState state) {
		return state == filter;
	}
}
