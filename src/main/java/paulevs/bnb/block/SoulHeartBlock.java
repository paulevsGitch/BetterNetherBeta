package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.util.BlockUtil;

public class SoulHeartBlock extends NetherCropBlock implements BlockWithLight {
	public SoulHeartBlock(String name, int id) {
		super(name, id, 3);
	}
	
	@Override
	protected boolean canPlantOnTopOf(int id) {
		return BlockUtil.isSoulTerrain(id);
	}
	
	@Environment(EnvType.CLIENT)
	public int method_1621() {
		return 1;
	}

	@Override
	public float getEmissionIntensity() {
		return 3;
	}
}
