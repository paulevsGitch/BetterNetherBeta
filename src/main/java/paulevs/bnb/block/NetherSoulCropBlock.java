package paulevs.bnb.block;

import paulevs.bnb.interfaces.CropBlockEnum;
import paulevs.bnb.util.BlockUtil;

public class NetherSoulCropBlock extends NetherPlantBlock {
	public NetherSoulCropBlock(String name, int id, Class<CropBlockEnum> type) {
		super(name, id, type, true);
	}
	
	@Override
	protected boolean canPlantOnTopOf(int id) {
		return BlockUtil.isSoulTerrain(id);
	}
}
