package paulevs.bnb.block;

import paulevs.bnb.block.types.UmbraPlantType;
import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.listeners.BlockListener;

public class UmbraPlantBlock extends NetherPlantBlock {
	public <T extends BlockEnum> UmbraPlantBlock(String name, int id) {
		super(name, id, UmbraPlantType.class, true);
	}
	
	@Override
	protected boolean canPlantOnTopOf(int id) {
		return id == BlockListener.getBlockID("umbralith");
	}
}
